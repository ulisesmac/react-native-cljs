(ns react-native.firebase.auth.core
  (:require ["@react-native-firebase/auth" :refer [GoogleAuthProvider getAuth signInAnonymously]]
            [applied-science.js-interop :as j]
            [react-native.google-signin.core :as google-signin]))

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

(defn ^:async login-with-google! []
  (let [auth-instance (auth)
        id-token      (-> (await (google-signin/sign-in!)) :data :idToken)]
    (when-not id-token
      (throw (js/Error. "Google Sign-In did not return an id token.")))
    (j/call auth-instance :signInWithCredential (j/call GoogleAuthProvider :credential id-token))))

(defn ^:async logout-with-google! []
  (await (j/call (auth) :signOut))
  (await (google-signin/sign-out!)))
