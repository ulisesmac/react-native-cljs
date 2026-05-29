(ns react-native.iap.core
  (:require ["react-native-iap" :refer [fetchProducts finishTransaction getActiveSubscriptions getAvailablePurchases useIAP]]
            [cljs-bean.core :refer [->clj]]))

(def product-type
  #:iap{:in-app "in-app"
        :subs   "subs"
        :all    "all"})

(def finish-transaction finishTransaction)
(def get-active-subscriptions getActiveSubscriptions)
(def get-available-purchases getAvailablePurchases)

(defn use-iap
  ([]
   (->clj (useIAP)))
  ([{:keys [alternative-billing-mode-android enable-billing-program-android on-error
            on-promoted-product-ios on-purchase-error on-purchase-success
            on-subscription-billing-issue on-user-choice-billing-android
            purchase-updated-listener-options]}]
   (->clj (useIAP #js {:alternativeBillingModeAndroid   alternative-billing-mode-android
                       :enableBillingProgramAndroid     enable-billing-program-android
                       :onError                         on-error
                       :onPromotedProductIOS            on-promoted-product-ios
                       :onPurchaseError                 on-purchase-error
                       :onPurchaseSuccess               on-purchase-success
                       :onSubscriptionBillingIssue      on-subscription-billing-issue
                       :onUserChoiceBillingAndroid      on-user-choice-billing-android
                       :purchaseUpdatedListenerOptions  purchase-updated-listener-options}))))
