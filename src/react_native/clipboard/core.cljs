(ns react-native.clipboard.core
  (:require ["@react-native-clipboard/clipboard" :default Clipboard]))

(def set-string! (.-setString Clipboard))

(defn get-string [f]
  (-> (.getString Clipboard)
      (.then f)))
