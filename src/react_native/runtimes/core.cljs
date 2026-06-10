(ns react-native.runtimes.core
  (:require
   ["@react-native-runtimes/core" :as runtimes]
   [applied-science.js-interop :as j]))

(def main-runtime-name (j/get runtimes :MAIN_RUNTIME_NAME))

(def get-current-runtime (j/get runtimes :getCurrentRuntime))
(def get-current-runtime-name (j/get runtimes :getCurrentRuntimeName))
(def main-runtime? (j/get runtimes :isMainRuntime))

(def threaded-component (j/get runtimes :threadedComponent))
(def register-threaded-component! (j/get runtimes :registerThreadedComponent))

(def register-lazy-threaded-component! (j/get runtimes :registerLazyThreadedComponent))
(def register-threaded-headless-task! (j/get runtimes :registerThreadedHeadlessTask))

(def runtime-function (j/get runtimes :runtimeFunction))
(def runtime-function-with-id (j/get runtime-function :withId))
(def runtime-function-named (j/get runtime-function :named))
(def register-runtime-function! (j/get runtimes :registerRuntimeFunction))
(def call (j/get runtimes :call))

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
