(ns react-native.async-storage.core
  (:require ["@react-native-async-storage/async-storage" :default AsyncStorage]
            [applied-science.js-interop :as j]
            [react-native.encoding :as encoding]))

(defn- app-state? [storage-key]
  (= storage-key :app-state))

(defn- prepare-content [storage-key content]
  (if (and (app-state? storage-key) (map? content))
    (dissoc content :ads :notifications)
    content))

(defn set-item! [storage-key content]
  (try
    (j/call AsyncStorage
            :setItem
            (encoding/->transit storage-key)
            (encoding/->transit (prepare-content storage-key content)))
    (catch js/Object err
      (js/console.warn err))))

(defn set-raw-item! [storage-key content]
  (try
    (j/call AsyncStorage :setItem storage-key content)
    (catch js/Object err
      (js/console.warn err))))

(defn remove-item! [storage-key]
  (try
    (j/call AsyncStorage :removeItem (encoding/->transit storage-key))
    (catch js/Object err
      (js/console.warn err))))

(defn remove-raw-item! [storage-key]
  (try
    (j/call AsyncStorage :removeItem storage-key)
    (catch js/Object err
      (js/console.warn err))))

(defn- read-storage-value [value]
  (when value
    (encoding/->clj value)))

(defn- storage-pairs [storage-map]
  (into {}
        (keep (fn [[storage-key content]]
                (when (and storage-key content)
                  [(encoding/->transit storage-key)
                   (encoding/->transit (prepare-content storage-key content))])))
        storage-map))

(defn- storage-data->pairs [storage-keys storage-data]
  (mapv (fn [storage-key]
          [storage-key (read-storage-value (aget storage-data (encoding/->transit storage-key)))])
        storage-keys))

(defn multi-set! [storage-map]
  (try
    (when-let [storage-data (not-empty (storage-pairs storage-map))]
      (j/call AsyncStorage :setMany (clj->js storage-data)))
    (catch js/Object err
      (js/console.warn err))))

(defn ^:async get-item [storage-key]
  (try
    (let [item (await (j/call AsyncStorage :getItem (encoding/->transit storage-key)))]
      (read-storage-value item))
    (catch :default err
      (js/console.error err)
      (throw err))))

(defn ^:async get-raw-item [storage-key]
  (try
    (await (j/call AsyncStorage :getItem storage-key))
    (catch :default err
      (js/console.error err)
      nil)))

(defn ^:async multi-get [storage-keys]
  (try
    (if (seq storage-keys)
      (let [storage-data (await (j/call AsyncStorage
                                        :getMany
                                        (into-array (map encoding/->transit storage-keys))))]
        (storage-data->pairs storage-keys storage-data))
      [])
    (catch :default err
      (js/console.error err)
      (throw err))))
