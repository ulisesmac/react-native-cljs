(ns react-native.runtimes.reagent
  (:require [cljs-bean.core :as bean]
            [react-native.runtimes.core :as runtimes]
            [reagent.core :as r]))

(defonce shadow-cljs-reload-count (r/atom 0))

(defn inc-reload! {:dev/after-load true} []
  (swap! shadow-cljs-reload-count inc))

(defn- hot-reload-wrapper [component-fn clj-props]
  (with-meta [component-fn clj-props]
             {:key (str "reloader-" @shadow-cljs-reload-count)}))

(defn reagent-wrapper [component-fn context-provider]
  (fn [props]
    (let [clj-props (bean/->clj props)]
      (r/as-element
       [context-provider
        [hot-reload-wrapper component-fn clj-props]]))))

(defn register-component! [component-name component-fn context-provider]
  (let [comp-name (name component-name)]
    (->> (reagent-wrapper component-fn context-provider)
         (runtimes/threaded-component comp-name)
         (runtimes/register-threaded-component! comp-name))))
