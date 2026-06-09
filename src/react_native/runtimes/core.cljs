(ns react-native.runtimes.core
  (:require
   ["@react-native-runtimes/core" :as runtimes]
   [applied-science.js-interop :as j]
   [reagent.core :as r]))

(def main-runtime-name (j/get runtimes :MAIN_RUNTIME_NAME))

(def get-current-runtime (j/get runtimes :getCurrentRuntime))
(def get-current-runtime-name (j/get runtimes :getCurrentRuntimeName))
(def main-runtime? (j/get runtimes :isMainRuntime))

(def on-runtime (r/adapt-react-class (j/get runtimes :OnRuntime)))
(def threaded (r/adapt-react-class (j/get runtimes :Threaded)))
(def threaded-screen (r/adapt-react-class (j/get runtimes :ThreadedScreen)))
(def threaded-react-surface (r/adapt-react-class (j/get runtimes :ThreadedReactSurface)))
(def threaded-component (j/get runtimes :ThreadedComponent))
(def register-threaded-component! (j/get runtimes :registerThreadedComponent))

(defn register-reagent-component! [component-name component-fn]
  (let [comp-name (name component-name)]
    (->> (fn [props] (r/as-element [@component-fn props]))
         (.threadedComponent runtimes comp-name)
         (.registerThreadedComponent runtimes comp-name))))

(def register-lazy-threaded-component! (j/get runtimes :registerLazyThreadedComponent))
(def register-threaded-headless-task! (j/get runtimes :registerThreadedHeadlessTask))

(def runtime-function (j/get runtimes :runtimeFunction))
(def runtime-function-with-id (j/get runtime-function :withId))
(def runtime-function-named (j/get runtime-function :named))
(def register-runtime-function! (j/get runtimes :registerRuntimeFunction))
(def call (j/get runtimes :call))
(def using-runtime (j/get runtimes :usingRuntime))

(def threaded-runtime (j/get runtimes :ThreadedRuntime))
(def preload! (j/get threaded-runtime :preload))
(def prewarm! (j/get threaded-runtime :prewarm))
(def prewarm-business-runtime! (j/get threaded-runtime :prewarmBusinessRuntime))
(def run-headless-task! (j/get threaded-runtime :runHeadlessTask))
;(def run! (j/get threaded-runtime :run))
(def call! (j/get threaded-runtime :call))
(def runtime (j/get threaded-runtime :runtime))
(def destroy! (j/get threaded-runtime :destroy))
(def destroy-all! (j/get threaded-runtime :destroyAll))
(def get-runtime-names (j/get threaded-runtime :getRuntimeNames))

(defn call-on [f runtime-name]
  (j/call (call f) :on runtime-name))
