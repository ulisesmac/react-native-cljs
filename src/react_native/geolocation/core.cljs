(ns react-native.geolocation.core
  (:require ["@react-native-community/geolocation" :default Geolocation]))

(def geolocation Geolocation)

(when (.-setRNConfiguration geolocation)
  ;; One-off nearby offices lookup: foreground permission only.
  (.setRNConfiguration geolocation
                       #js {:authorizationLevel             "whenInUse"
                            :enableBackgroundLocationUpdates false
                            :locationProvider               "auto"}))

(defn get-current-position [{:keys [on-success on-failure]}]
  (.getCurrentPosition
    geolocation
    (fn [info]
      (when on-success
        (let [{:keys [latitude longitude]} (:coords (js->clj info :keywordize-keys true))
              user-location {:latitude  latitude
                             :longitude longitude}]
          (on-success user-location))))
    (fn [error]
      (when on-failure
        (on-failure (.-message error))))))
