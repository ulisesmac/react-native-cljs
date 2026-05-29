(ns react-native.re-frame.utils
  (:require
   [cljs-bean.core :refer [->clj]]
   [re-frame.core :as rf]))

(defn invoke-callback [callback value]
  (cond
    (fn? callback)     (callback value)
    (vector? callback) (rf/dispatch (conj callback value))))

(defn ^:async handle-promise [promise {:keys [on-success on-error]}]
  (try
    (->> (await promise) ->clj (invoke-callback on-success))
    (catch :default err
      (->> err ->clj (invoke-callback on-error)))))

(defn fx-call-promise [f]
  (fn [{:keys [args] :as callbacks}]
    (handle-promise (apply f args) callbacks)))
