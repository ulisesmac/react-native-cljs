(ns react-native.iap.re-frame
  (:require [react-native.iap.core :as iap]
            [react-native.utils :refer [prop]]
            [re-frame.core :as rf]))

(rf/reg-fx
 :fx.iap/finish-transaction
 (fn [purchase]
   (iap/finish-transaction (prop {:purchase      purchase
                                  :is-consumable false}))))
