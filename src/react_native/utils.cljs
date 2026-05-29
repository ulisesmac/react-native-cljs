(ns react-native.utils
  (:require-macros [react-native.utils])
  (:require [react-native.react.core :as react]
            [react-native.reagent-compiler.prop-converter :as prop-converter]
            [reagent.impl.template :as template]))

(defn map-array [f coll]
  (let [js-array ^js (array)]
    (doseq [e coll]
      (.push js-array (f e)))
    js-array))

(defn ->js-prop-obj
  "Transforms a cljs map into a JS object with keys as props style.
   Recursive. Runtime. Fast - Cached."
  [m]
  (prop-converter/convert-custom-prop-value template/*current-default-compiler* m))

(defn- compose-styles-process [result entry]
  (cond
    (vector? entry) (reduce compose-styles-process result entry)
    (nil? entry)    result
    :else           (conj! result entry)))

(defn add-styles
  "Compose a vector of style entries from various runtime values. Accepts
  maps, JS objects, vectors (recursively flattened), and ignores nil/false
  entries. Returns a vector suitable for React Native's style prop.

  Optimized to minimize intermediate allocations when flattening nested
  style vectors."
  [& styles]
  (persistent!
   (reduce compose-styles-process
           (transient [])
           styles)))

(defn use-pass-clj-data
  "Specific for Reagent: memoize a collection but keep items as-is."
  [coll]
  (react/use-memo #(with-meta coll {:keep-items true})
                  [(hash coll)]))
