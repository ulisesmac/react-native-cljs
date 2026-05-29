(ns react-native.firebase.app-check.re-frame
  (:require
   [re-frame.core :as rf]
   [react-native.firebase.app-check.core :as app-check]
   [react-native.re-frame.utils :as rf-utils]))

(defn- initialize [config]
  (rf-utils/handle-promise (app-check/initialize! config) config))

(rf/reg-fx
 :fx.firebase.app-check/initialize
 initialize)
