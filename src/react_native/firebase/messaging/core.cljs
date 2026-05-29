(ns react-native.firebase.messaging.core
  (:require ["@react-native-firebase/messaging" :refer [getMessaging
                                                        getToken
                                                        onMessage
                                                        setBackgroundMessageHandler
                                                        subscribeToTopic
                                                        unsubscribeFromTopic]]))

(defn messaging []
  (getMessaging))

(defn ^:async get-token []
  (await (getToken (messaging))))

(defn set-background-message-handler [f]
  (setBackgroundMessageHandler (messaging) f))

(defn on-message [f]
  (onMessage (messaging) f))

(defn subscribe-to-topic! [topic]
  (subscribeToTopic (messaging) topic))

(defn unsubscribe-from-topic! [topic]
  (unsubscribeFromTopic (messaging) topic))
