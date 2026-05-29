(ns react-native.firebase.analytics.core
  (:require
   ["@react-native-firebase/analytics" :refer [getAnalytics logScreenView]]
   [cljs-bean.core :refer [->js]]))

(defn analytics
  "Returns a Firebase Analytics instance."
  []
  (getAnalytics))

(defn log-screen-view [screen-name screen-class]
  (logScreenView (analytics) (->js {:screen_name  screen-name
                                    :screen_class screen-class})))
