(ns react-native.runtimes.fn
  (:require [applied-science.js-interop :as j]
            [react-native.runtimes.core :as runtimes]))

(defn register-executor! [k f]
  (let [fn-id     (j/get k :fqn)
        tagged-fn (runtimes/runtime-function-named fn-id f)]
    (runtimes/register-runtime-function! fn-id (fn [] tagged-fn))))

(defn get-caller!
  "Returns a function which `k`(id) will be executed on `target-runtime`.
   `k` must be previously registered on `target-runtime`."
  [k target-runtime]
  (let [fn-id      (j/get k :fqn)
        runtime-id (name target-runtime)
        tagged-fn  (runtimes/runtime-function-named fn-id (fn dummy-fn []))]
    (fn [clj-params]
      (let [js-params (clj->js clj-params)
            f         (j/call (runtimes/call tagged-fn) :on runtime-id)]
        (f js-params)))))
