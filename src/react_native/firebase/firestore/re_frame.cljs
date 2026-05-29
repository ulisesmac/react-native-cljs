(ns react-native.firebase.firestore.re-frame
  (:require
   [re-frame.core :as rf]
   [react-native.firebase.firestore.core :as firestore]
   [react-native.re-frame.utils :as rf-utils]))

(rf/reg-fx
 :fx.firebase.firestore/get-doc
 (rf-utils/fx-call-promise firestore/get-doc))

(rf/reg-fx
 :fx.firebase.firestore/get-docs
 (rf-utils/fx-call-promise firestore/get-docs))

(rf/reg-fx
 :fx.firebase.firestore/add-doc
 (rf-utils/fx-call-promise firestore/add-doc!))

(rf/reg-fx
 :fx.firebase.firestore/set-doc
 (rf-utils/fx-call-promise firestore/set-doc!))

(rf/reg-fx
 :fx.firebase.firestore/update-doc
 (rf-utils/fx-call-promise firestore/update-doc!))

(rf/reg-fx
 :fx.firebase.firestore/delete-doc
 (rf-utils/fx-call-promise firestore/delete-doc!))
