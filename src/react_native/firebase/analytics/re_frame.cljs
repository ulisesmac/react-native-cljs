(ns react-native.firebase.analytics.re-frame
  (:require
   [re-frame.core :as rf]
   [react-native.firebase.analytics.core :as analytics]
   [react-native.re-frame.utils :as rf-utils]))

(defn- log-screen-view [{:keys [screen-name screen-class] :as callbacks}]
  (rf-utils/handle-promise (analytics/log-screen-view screen-name screen-class) callbacks))

(rf/reg-fx :fx.firebase.analytics/log-screen-view log-screen-view)
