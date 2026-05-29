(ns react-native.react-navigation.stack
  (:require
   ["@react-navigation/stack" :refer [createStackNavigator TransitionSpecs TransitionPresets]]
   [react-native.react-navigation.navigator.impl :as impl]
   [react-native.utils :as rn.utils]))

(defn- reactify-screens-map [m]
  (update m :screens impl/->js-screens-data))

(defn create-stack-navigator [config]
  (let [screens-at-root?   (:screens config)
        screens-in-groups? (-> config :groups vals first :screens seq)]
    (cond-> config
      screens-at-root?   reactify-screens-map
      screens-in-groups? (update :groups update-vals reactify-screens-map)
      :always            rn.utils/->js-prop-obj
      :always            (createStackNavigator))))

(def transition-specs TransitionSpecs)

(def transition-presets TransitionPresets)
