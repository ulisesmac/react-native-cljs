(ns react-native.firebase.app-check.core
  (:require ["@react-native-firebase/app-check" :refer [ReactNativeFirebaseAppCheckProvider initializeAppCheck]]))

(defn initialize!
  [{:keys [android-debug-token android-provider apple-debug-token apple-provider token-auto-refresh?]}]
  (let [provider (ReactNativeFirebaseAppCheckProvider.)]
    (.configure provider
                #js{:android #js{:provider   android-provider
                                 :debugToken android-debug-token}
                    :apple   #js{:provider   apple-provider
                                 :debugToken apple-debug-token}})
    (initializeAppCheck nil
                        #js{:provider                  provider
                            :isTokenAutoRefreshEnabled token-auto-refresh?})))
