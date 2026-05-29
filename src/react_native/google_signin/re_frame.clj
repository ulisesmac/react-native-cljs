(ns react-native.google-signin.re-frame
  (:require
   [re-frame.core :as rf]
   [react-native.google-signin.core :as google-signing]
   [react-native.re-frame.utils :as rf-utils]))

(rf/reg-fx
 :fx.firebase.auth/login-with-google
 (rf-utils/fx-call-promise google-signing/login!))

(rf/reg-fx
 :fx.firebase.auth/logout-with-google
 (rf-utils/fx-call-promise google-signing/logout!))
