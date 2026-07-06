(ns react-native.runtimes.fn
  (:require [applied-science.js-interop :as j]
            [react-native.encoding :as encoding]
            [react-native.runtimes.core :as runtimes]))

(defn- execution-error [^js/Object e]
  {:error   (or (some-> e .-name) "Error")
   :details (or (some-> e .-message) (str e))})

(defn- execution-error? [result]
  (boolean (and (:error result) (:details result))))

(defn register-executor! [k f]
  (let [fn-id     (.-fqn ^js k)
        tagged-fn (runtimes/runtime-function-named
                   fn-id
                   (^:async fn [params]
                    (try
                      (encoding/encode (await ((if (fn? f) f (deref f)) (encoding/decode params))))
                      (catch :default e
                        (js/console.error "Runtime executor failed" fn-id e)
                        (encoding/encode (execution-error e))))))]
    (runtimes/register-runtime-function! fn-id (fn [] tagged-fn))))

(defn get-caller
  "Returns a function which `k`(id) will be executed on `target-runtime`.
   `k` must be previously registered on `target-runtime`.
   `:on-success` and `:on-error` callback params run on the caller runtime."
  [k target-runtime]
  (let [fn-id      (.-fqn ^js k)
        runtime-id (name target-runtime)
        tagged-fn  (runtimes/runtime-function-named fn-id (fn dummy-fn []))]
    (^:async fn caller
      ([] (caller {}))
      ([params]
       (let [on-error    (:on-error params)
             call-params (dissoc params :on-success :on-error)]
         (try
           (let [f      (j/call (runtimes/call tagged-fn) :on runtime-id)
                 result (encoding/decode (await (f (encoding/encode call-params))))]
             (if (execution-error? result)
               (when on-error
                 (on-error result))
               (when-let [on-success (:on-success params)]
                 (on-success result)))
             result)
           (catch :default e
             (when on-error
               (on-error e))
             (throw e))))))))
