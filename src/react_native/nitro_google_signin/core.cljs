(ns react-native.nitro-google-signin.core
  (:require ["react-native-nitro-google-signin" :refer [GoogleOneTapSignIn
                                                        isNoSavedCredentialFoundResponse
                                                        isSuccessResponse]]
            [applied-science.js-interop :as j]
            [cljs-bean.core :refer [->clj]]))

(defn ^:async sign-in! []
  (let [_                 (await (.checkPlayServices GoogleOneTapSignIn))
        signin-response   (await (.signIn GoogleOneTapSignIn))
        account-response  (when (some-> signin-response (isNoSavedCredentialFoundResponse))
                            (await (.createAccount GoogleOneTapSignIn)))
        explicit-response (when (some-> account-response (isNoSavedCredentialFoundResponse))
                            (await (.presentExplicitSignIn GoogleOneTapSignIn)))
        response          (or explicit-response account-response signin-response)]
    (when (isSuccessResponse response)
      (->clj (j/get response :data)))))

(defn ^:async get-tokens! []
  (->clj (await (.getTokens GoogleOneTapSignIn))))

(defn ^:async request-scopes! [scopes]
  (->clj (await (.requestScopes GoogleOneTapSignIn (to-array scopes)))))

(defn ^:async search-directory! [access-token query]
  (let [url      (str "https://people.googleapis.com/v1/people:searchDirectoryPeople"
                      "?query=" (js/encodeURIComponent query)
                      "&readMask=names,emailAddresses,photos"
                      "&sources=DIRECTORY_SOURCE_TYPE_DOMAIN_PROFILE"
                      "&pageSize=500")
        response (await
                  (js/fetch
                   url
                   #js{:headers #js{:Authorization (str "Bearer " access-token)}}))]
    (when-not (j/get response :ok)
      (throw (js/Error. (str "People API request failed with status "
                             (j/get response :status)))))
    (->clj (await (j/call response :json)))))

(defn ^:async sign-out! []
  (.signOut GoogleOneTapSignIn))


(comment
 ;; Request directory scope
 (do
   (def scopes (atom nil))
   (-> (.requestScopes GoogleOneTapSignIn #js["https://www.googleapis.com/auth/directory.readonly"])
       (.then (fn [r]
                (reset! scopes r)))))

 ;; search query
 (defn ^:async search-directory! [access-token query]
   (let [url      (str "https://people.googleapis.com/v1/people:searchDirectoryPeople"
                       "?query=" (js/encodeURIComponent query)
                       "&readMask=names,emailAddresses,photos"
                       "&sources=DIRECTORY_SOURCE_TYPE_DOMAIN_PROFILE"
                       "&pageSize=20")
         response (await
                   (js/fetch
                    url
                    #js{:headers #js{:Authorization (str "Bearer " access-token)}}))]
     (when-not (j/get response :ok)
       (throw (js/Error. (str "People API request failed with status "
                              (j/get response :status)))))
     (->clj (await (j/call response :json)))))

 ;; query
 (do
   (def query-res (atom nil))
   (-> (search-directory! (j/get @scopes :accessToken) "")
       (.then (fn [r]
                (reset! query-res r)))))

 )
