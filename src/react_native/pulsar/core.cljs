(ns react-native.pulsar.core
  (:require ["react-native-pulsar" :refer [Presets]]
            [applied-science.js-interop :as j]))

(defn preset! [{:keys [platform preset]}]
  (let [preset-path (case platform
                      :platform/android [:System :Android preset]
                      :platform/ios     [:System preset]
                      [preset])]
    (j/call-in Presets preset-path)))
