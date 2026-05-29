(ns react-native.react-navigation.native-stack
  (:require
   ["@react-navigation/native-stack" :refer [createNativeStackNavigator]]
   [react-native.react-navigation.navigator.impl :as impl]
   [react-native.utils :as rn.utils]
   [reagent.core :as r]))

(defn- reactify-screens-map [m]
  (update m :screens impl/->js-screens-data))

(defn- reactify-groups-map [config]
  (update config :groups
          (fn [groups]
            (update-vals groups
                         (fn [group]
                           (cond-> group
                             (:screens group) reactify-screens-map))))))

(defn create-native-stack-navigator
  ([]
   (let [stack (createNativeStackNavigator)]
     {:navigator (r/adapt-react-class (.-Navigator stack))
      :screen    (r/adapt-react-class (.-Screen stack))}))
  ([config]
   (cond-> config
     (:screens config) reactify-screens-map
     (:groups config)  reactify-groups-map
     :always           rn.utils/->js-prop-obj
     :always           (createNativeStackNavigator))))
