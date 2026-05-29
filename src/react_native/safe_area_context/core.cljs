(ns react-native.safe-area-context.core
  (:require ["react-native-safe-area-context" :refer [useSafeAreaFrame useSafeAreaInsets] :as safe-area-context]
            [cljs-bean.core :refer [->clj]]))

;; TODO: extract dynamic safe areas

(def initial-window-metrics
  (js->clj (.-initialWindowMetrics safe-area-context) :keywordize-keys true))

(def ^{:deprecated "Use use-top instead."} top
  (-> initial-window-metrics :insets :top))

(def ^{:deprecated "Use use-bottom instead."} bottom
  (-> initial-window-metrics :insets :bottom))

(defn use-insets []
  (->clj (useSafeAreaInsets)))

(defn use-window
  "Returns the current SafeAreaProvider frame as a map."
  []
  (->clj (useSafeAreaFrame)))

(defn use-top []
  (:top (use-insets)))

(defn use-bottom []
  (:bottom (use-insets)))
