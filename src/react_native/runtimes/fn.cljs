(ns react-native.runtimes.fn
  (:require [applied-science.js-interop :as j]
            [cognitect.transit :as transit]
            [react-native.runtimes.core :as runtimes]))

(defn- js-plain-object? [x]
  (and (object? x)
       (not (map? x))
       (identical? (.-constructor ^js x) js/Object)))

(defn- js-data? [x] (or (array? x) (js-plain-object? x)))

(defn- write-js-data [x] (js/JSON.stringify x))
(defn- read-js-data [x] (js/JSON.parse x))

(def ^:private transit-reader
  (transit/reader :json {:handlers {"js" read-js-data}}))

(def ^:private transit-writer
  (transit/writer :json {:handlers {js/Object (transit/write-handler "js" write-js-data)
                                     js/Array  (transit/write-handler "js" write-js-data)}}))

(def ^:private ->transit (partial transit/write transit-writer))
(def ^:private ->clj (partial transit/read transit-reader))

(defn- encode [x] (if (js-data? x) x (->transit x)))
(defn- decode [x]
  (cond
    (nil? x)     nil
    (js-data? x) x
    :else        (->clj x)))

(defn- execution-error [e]
  {:error   (or (some-> e .-name) "Error")
   :details (or (some-> e .-message) (str e))})

(defn- execution-error? [result]
  (boolean
   (and (map? result)
        (:error result)
        (:details result))))

(defn register-executor! [k f-var]
  (let [fn-id     (.-fqn ^js k)
        tagged-fn (runtimes/runtime-function-named
                   fn-id
                   (^:async fn [params]
                    (try
                      (encode (await ((deref f-var) (decode params))))
                      (catch :default e
                        (encode (execution-error e))))))]
    (runtimes/register-runtime-function! fn-id (fn [] tagged-fn))))

(defn get-caller
  "Returns a function which `k`(id) will be executed on `target-runtime`.
   `k` must be previously registered on `target-runtime`.
   `:on-success` and `:on-error` callback params run on the caller runtime."
  [k target-runtime]
  (let [fn-id      (.-fqn ^js k)
        runtime-id (name target-runtime)
        tagged-fn  (runtimes/runtime-function-named fn-id (fn dummy-fn []))]
    (^:async fn caller [params]
      (let [on-success  (when (map? params) (:on-success params))
            on-error    (when (map? params) (:on-error params))
            call-params (if (map? params)
                          (dissoc params :on-success :on-error)
                          params)]
        (try
          (let [f      (j/call (runtimes/call tagged-fn) :on runtime-id)
                result (decode (await (f (encode call-params))))]
            (if (execution-error? result)
              (when on-error
                (on-error result))
              (when on-success
                (on-success result)))
            result)
          (catch :default e
            (when on-error
              (on-error e))
            (throw e)))))))
