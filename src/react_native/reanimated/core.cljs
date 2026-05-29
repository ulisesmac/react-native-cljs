(ns react-native.reanimated.core
  (:require ["react-native-reanimated" :refer [Easing FadeIn FadeInDown FadeInLeft FadeInRight FadeInUp
                                               FadeOut FadeOutDown FadeOutLeft FadeOutRight LinearTransition
                                               SlideInUp SlideOutUp cubicBezier makeMutable useAnimatedRef
                                               useScrollOffset useSharedValue withSequence withSpring withTiming]]
            [applied-science.js-interop :as j]))

(def linear-easing (.-linear Easing))

(def linear-transition LinearTransition)
(def slide-in-up SlideInUp)
(def slide-out-up SlideOutUp)

(def fade-in FadeIn)
(def fade-in-up FadeInUp)
(def fade-in-down FadeInDown)
(def fade-in-left FadeInLeft)
(def fade-in-right FadeInRight)

(def fade-out FadeOut)
(def fade-out-down FadeOutDown)
(def fade-out-left FadeOutLeft)
(def fade-out-right FadeOutRight)

(def use-shared-value useSharedValue)
(def use-animated-ref useAnimatedRef)
(def use-scroll-offset useScrollOffset)
(def make-mutable makeMutable)

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

(defn- easing-base [kind params]
  (let [easing-fn (j/get Easing kind)]
    (if params
      (apply easing-fn params)
      easing-fn)))

(defn easing
  ([kind]
   (easing kind {}))
  ([kind {easing-type :type
          params      :params
          :or         {easing-type :in}}]
   (j/call Easing
           (case easing-type
             :out    :out
             :in-out :inOut
             :in)
           (easing-base kind params))))

(defn appear-in []
  (j/call fade-in :duration appear-in-duration))

(defn disappear-out []
  (j/call fade-out :duration disappear-out-duration))
