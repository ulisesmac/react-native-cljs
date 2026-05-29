(ns react-native.reanimated.core
  (:require ["react-native-reanimated" :refer [Easing FadeIn FadeInUp FadeOut LinearTransition SlideInUp
                                               SlideOutUp cubicBezier useSharedValue withSequence withSpring
                                               withTiming]]
            [applied-science.js-interop :as j]))

(def linear-easing (.-linear Easing))

(def linear-transition LinearTransition)
(def slide-in-up SlideInUp)
(def slide-out-up SlideOutUp)

(def fade-in FadeIn)
(def fade-in-up FadeInUp)
(def fade-out FadeOut)

(def use-shared-value useSharedValue)

(def appear-in-duration 220)
(def disappear-out-duration 120)

(defn sv-get! [shared-value]
  (j/call shared-value :get))

(defn sv-set! [shared-value v]
  (j/call shared-value :set v))

(def with-timing withTiming)
(def with-spring withSpring)
(def with-sequence withSequence)
(def cubic-bezier cubicBezier)

(defn appear-in []
  (j/call fade-in :duration appear-in-duration))

(defn disappear-out []
  (j/call fade-out :duration disappear-out-duration))
