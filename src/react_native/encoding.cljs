(ns react-native.encoding
  (:require [cljs-bean.transit :as bean-transit]
            [cognitect.transit :as transit]))

(defn- js-plain-object? [x]
  (and (object? x)
       (not (map? x))
       (identical? (.-constructor ^js x) js/Object)))

(defn- js-data? [x] (or (array? x) (js-plain-object? x)))

(defn- write-js-data [x] (js/JSON.stringify x))
(defn- read-js-data [x] (js/JSON.parse x))

(def transit-reader
  (transit/reader :json {:handlers {"js" read-js-data}}))

(def transit-writer
  (transit/writer :json {:handlers (merge (bean-transit/writer-handlers)
                                           {js/Object (transit/write-handler "js" write-js-data)
                                            js/Array  (transit/write-handler "js" write-js-data)})}))

(defn ->transit [x] (transit/write transit-writer x))
(defn ->clj [x] (transit/read transit-reader x))

(defn encode [x] (if (js-data? x) x (->transit x)))

(defn decode [x]
  (cond
    (nil? x)     nil
    (js-data? x) x
    :else        (->clj x)))
