(ns react-native.firebase.auth.re-frame
  (:require
   [re-frame.core :as rf]
   [react-native.firebase.auth.core :as auth]
   [react-native.re-frame.utils :as rf-utils]))

(rf/reg-fx
 :fx.firebase.auth/login-anonymously
 (rf-utils/fx-call-promise auth/login-anonymously!))
