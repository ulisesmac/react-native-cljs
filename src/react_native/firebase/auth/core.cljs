(ns react-native.firebase.auth.core
  (:require ["@react-native-firebase/auth" :refer [GoogleAuthProvider getAuth signInAnonymously]]
            [applied-science.js-interop :as j]))

(defn auth
  "Returns an Auth instance."
  ([] (getAuth))
  ([app] (getAuth app)))

(defn login-anonymously!
  "Signs in anonymously."
  ([] (login-anonymously! (auth)))
  ([auth-instance] (signInAnonymously auth-instance)))

(defn current-user []
  (j/get (auth) :currentUser))

(defn get-id-token! []
  (if-let [user (current-user)]
    (j/call user :getIdToken true)
    (js/Promise.reject (js/Error. "Firebase Auth has no current user."))))
