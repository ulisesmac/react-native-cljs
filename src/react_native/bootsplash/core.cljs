(ns react-native.bootsplash.core
  (:require
   ["react-native-bootsplash" :default BootSplash]
   [applied-science.js-interop :as j]))

(defn hide! [{:keys [fade?]}]
  (j/call BootSplash :hide #js{:fade fade?}))

(defn visible? []
  (j/call BootSplash :isVisible))
