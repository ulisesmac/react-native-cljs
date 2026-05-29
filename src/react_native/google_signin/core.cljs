(ns react-native.google-signin.core
  (:require ["@react-native-google-signin/google-signin" :refer [GoogleSignin isSuccessResponse]]
            [applied-science.js-interop :as j]
            [cljs-bean.core :refer [->clj]]
            [react-native.utils :refer [prop]]
            [react-native.firebase.auth.core :as auth]))

(defn configure! []
  (j/call GoogleSignin :configure
          (prop {:web-client-id "919811489221-5rjsc969mlt5sula76ns2nl3gltc3gbe.apps.googleusercontent.com"})))

(defn ^:async sign-in! []
  (configure!)
  (await (j/call GoogleSignin :hasPlayServices (prop {:show-play-services-update-dialog true})))
  (let [response (await (j/call GoogleSignin :signIn))]
    (when (isSuccessResponse response)
      (->clj response))))

(defn ^:async sign-out! []
  (configure!)
  (await (j/call GoogleSignin :signOut)))

(defn ^:async login! []
  (let [auth-instance (auth/auth)
        id-token      (-> (await (sign-in!)) :data :idToken)]
    (when-not id-token
      (throw (js/Error. "Google Sign-In did not return an id token.")))
    (j/call auth-instance :signInWithCredential (j/call GoogleAuthProvider :credential id-token))))

(defn ^:async logout! []
  (await (j/call (auth/auth) :signOut))
  (await (sign-out!)))
