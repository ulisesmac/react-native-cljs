(ns react-native.utils
  (:require [cljs.analyzer :as ana]
            [cljs.env :as env]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [shadow.build.cljs-bridge :as cljs-bridge]
            [shadow.cljs.util :as shadow-util])
  (:import [java.nio.file CopyOption Files StandardCopyOption]))

(def ^:private asset-output-root "xquo-assets")

(defn- build-output-dir []
  (or (get-in (cljs-bridge/get-build-state) [:build-options :output-dir])
      (throw (ex-info "react-native.utils/asset-require requires a shadow-cljs :output-dir" {}))))

(defn- normalize-resource-path [path]
  (let [path     (cond-> (str/replace path #"\\" "/")
                   (str/starts-with? path "/") (subs 1))
        segments (str/split path #"/")]
    (when (or (str/blank? path)
              (some #{"." ".." ""} segments))
      (throw (ex-info "Invalid asset resource path" {:path path})))
    path))

(defn- record-resource-ref! [env resource-path resource-url]
  (when env/*compiler*
    (when-let [current-ns (-> env :ns :name)]
      (swap! env/*compiler*
             assoc-in
             [::ana/namespaces current-ns :shadow.resource/resource-refs resource-path]
             (shadow-util/url-last-modified resource-url)))))

(defn- copy-resource! [env resource-path]
  (let [resource-url (or (io/resource resource-path)
                         (throw (ex-info "Asset resource not found" {:path resource-path})))
        target       (io/file (build-output-dir) asset-output-root resource-path)]
    (io/make-parents target)
    (with-open [input (io/input-stream resource-url)]
      (Files/copy input
                  (.toPath target)
                  (into-array CopyOption [StandardCopyOption/REPLACE_EXISTING])))
    (record-resource-ref! env resource-path resource-url)))

(defmacro asset-require
  "Copies a classpath resource into Shadow's React Native output dir and
   expands to a static `js/require` path Metro can resolve."
  [path]
  (when-not (string? path)
    (throw (ex-info "react-native.utils/asset-require requires a literal string path" {:path path})))
  (let [resource-path (normalize-resource-path path)]
    (copy-resource! &env resource-path)
    `(~'js/require ~(str "./" asset-output-root "/" resource-path))))

(defn- capitalize-first [^String s]
  (if (seq s)
    (str (.toUpperCase (subs s 0 1)) (subs s 1))
    s))

(defn- kebab->camel-str [k]
  (let [s     (cond
                (keyword? k) (name k)
                (symbol? k)  (name k)
                :else        (str k))
        parts (str/split s #"-")]
    (if (seq (rest parts))
      (apply str (first parts) (map capitalize-first (rest parts)))
      s)))

(declare emit-style)

(defn- emit-style-map [m]
  (let [pairs (mapcat (fn [[k v]]
                        [(kebab->camel-str k) (emit-style v)])
                      m)]
    `(~'js-obj ~@pairs)))

(defn- emit-style [form]
  (cond
    (map? form)     (emit-style-map form)
    (vector? form)  `(~'array ~@(map emit-style form))
    (keyword? form) (name form)
    :else           form))

(defmacro style
  "Macro that takes a CLJS map and expands into an expression that creates a
   JS object with camelCased keys. All key-name conversions happen at macro
   time in CLJ; values are left as expressions for CLJS to evaluate."
  [m]
  (emit-style m))

(defmacro defstyle
  "Defines a CLJS var bound to `(style m)`.
   Usage: (defstyle my-style {:font-size 14})"
  [name m]
  `(def ~name ~(emit-style m)))

(defmacro prop
  "Macro that takes a CLJS map and expands into an expression that creates a
   JS object with camelCased keys. All key-name conversions happen at macro
   time in CLJ; values are left as expressions for CLJS to evaluate."
  [m]
  (emit-style m))

(defmacro defprop
  "Defines a CLJS var bound to `(style m)`.
   Usage: (defprop my-prop {:font-size 14})"
  [name m]
  `(def ~name ~(emit-style m)))
