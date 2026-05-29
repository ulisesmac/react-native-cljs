(ns react-native.firebase.firestore.core
  (:require
   ["@react-native-firebase/firestore" :as firestore]
   [cljs-bean.core :refer [->js]]))

(def db firestore/getFirestore)

(def collection firestore/collection)

(def doc firestore/doc)
(def get-doc firestore/getDoc)
(def get-docs firestore/getDocs)

(defn add-doc! [collection-ref data]
  (firestore/addDoc collection-ref (->js data)))

(defn set-doc!
  ([doc-ref data] (firestore/setDoc doc-ref (->js data)))
  ([doc-ref data options] (firestore/setDoc doc-ref (->js data) (->js options))))

(defn update-doc! [doc-ref data]
  (firestore/updateDoc doc-ref (->js data)))

(def delete-doc! firestore/deleteDoc)
(def wait-for-pending-writes firestore/waitForPendingWrites)

(defn on-snapshot!
  ([ref on-next] (firestore/onSnapshot ref on-next))
  ([ref on-next on-error] (firestore/onSnapshot ref on-next on-error)))

(def server-timestamp firestore/serverTimestamp)
(def array-union firestore/arrayUnion)
(def array-remove firestore/arrayRemove)
(def increment firestore/increment)
