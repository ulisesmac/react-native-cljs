(ns react-native.gesture-handler.core
  (:require ["react-native-gesture-handler" :refer [Gesture]]))

(def gesture Gesture)
(def gesture-pan (.-Pan Gesture))
