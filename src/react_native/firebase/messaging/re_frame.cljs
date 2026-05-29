(ns react-native.firebase.messaging.re-frame
  (:require [react-native.firebase.messaging.core :as messaging]
            [react-native.re-frame.utils :as rf-utils]
            [re-frame.core :as rf]))

(rf/reg-fx
 :fx.firebase.messaging/subscribe-to-topic
 (rf-utils/fx-call-promise messaging/subscribe-to-topic!))

(rf/reg-fx
 :fx.firebase.messaging/unsubscribe-from-topic
 (rf-utils/fx-call-promise messaging/unsubscribe-from-topic!))
