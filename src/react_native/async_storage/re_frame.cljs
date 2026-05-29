(ns react-native.async-storage.re-frame
  (:require
   [re-frame.core :as rf]
   [react-native.async-storage.core :as async-storage]
   [react-native.re-frame.utils :as rf-utils]))

(rf/reg-fx
 :fx.async-storage/set-item
 (rf-utils/fx-call-promise async-storage/set-item!))

(rf/reg-fx
 :fx.async-storage/remove-item
 (rf-utils/fx-call-promise async-storage/remove-item!))

(rf/reg-fx
 :fx.async-storage/remove-raw-item
 (rf-utils/fx-call-promise async-storage/remove-raw-item!))

(rf/reg-fx
 :fx.async-storage/multi-set
 (rf-utils/fx-call-promise async-storage/multi-set!))

(rf/reg-fx
 :fx.async-storage/get-item
 (fn get-item [{:keys [storage-key] :as callbacks}]
   (rf-utils/handle-promise (async-storage/get-item storage-key) callbacks)))

(rf/reg-fx
 :fx.async-storage/multi-get
 (fn multi-get [{:keys [storage-keys] :as callbacks}]
   (rf-utils/handle-promise (async-storage/multi-get storage-keys) callbacks)))
