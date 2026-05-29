(ns react-native.google-mobile-ads.core
  (:require
   ["react-native-google-mobile-ads$default" :as mobileAds]
   ["react-native-google-mobile-ads" :refer [AdEventType AdsConsent AdsConsentDebugGeography
                                             AdsConsentPrivacyOptionsRequirementStatus BannerAd
                                             BannerAdSize InterstitialAd NativeAd NativeAdChoicesPlacement
                                             NativeAdEventType NativeAssetType NativeMediaAspectRatio
                                             TestIds]]
   [applied-science.js-interop :as j]
   [reagent.core :as r]))

(def mobile-ads mobileAds)
(def ads-consent AdsConsent)
(def ads-consent-debug-geography AdsConsentDebugGeography)
(def ads-consent-privacy-options-requirement-status AdsConsentPrivacyOptionsRequirementStatus)
(def banner-ad (r/adapt-react-class BannerAd))
(def interstitial-ad InterstitialAd)
(def ad-event-type AdEventType)

(def native-ad NativeAd)
(def native-media-aspect-ratio (js->clj NativeMediaAspectRatio :keywordize-keys true))

(def native-asset-type
  {:icon           (.-ICON NativeAssetType)
   :headline       (.-HEADLINE NativeAssetType)
   :body           (.-BODY NativeAssetType)
   :call-to-action (.-CALL_TO_ACTION NativeAssetType)})

(def native-ad-choices-placement NativeAdChoicesPlacement)
(def native-ad-event-type NativeAdEventType)

(def test-ids
  {:banner          (.-BANNER TestIds)
   :adaptive-banner (.-ADAPTIVE_BANNER TestIds)
   :interstitial    (.-INTERSTITIAL TestIds)
   :native          (.-NATIVE TestIds)})

(def banner-ad-size
  {:banner                   (.-BANNER BannerAdSize)
   :full-banner              (.-FULL_BANNER BannerAdSize)
   :anchored-adaptive-banner (.-ANCHORED_ADAPTIVE_BANNER BannerAdSize)})

(def gdpr-consent-enabled? true)
(def gdpr-debug-geography-enabled? false)

(defonce initialized? (atom false))

(defn privacy-options-required? [consent-info]
  (and gdpr-consent-enabled?
       (= (j/get consent-info :privacyOptionsRequirementStatus)
          (.-REQUIRED ads-consent-privacy-options-requirement-status))))

(defn- adapter-ready? [adapter-status]
  (= (j/get adapter-status :state) 1))

(defn consent-options [{:keys [development?]}]
  (if (and (if (some? development?) development? js/__DEV__)
           gdpr-debug-geography-enabled?)
    #js {:debugGeography (.-EEA ads-consent-debug-geography)}
    #js {}))

(defn initialize-sdk! [{:keys [on-ready on-error]}]
  (-> (mobile-ads)
      (.initialize)
      (.then (fn [adapter-statuses]
               (reset! initialized? true)
               (when on-ready
                 (on-ready adapter-statuses))
               (if (some adapter-ready? adapter-statuses)
                 (js/console.log "ADS INITIALIZED!, let's make money 🤑")
                 (js/console.warn "ADS INITIALIZED, but no adapter reported ready" adapter-statuses))))
      (.catch (fn [err]
                (when on-error
                  (on-error err))
                (js/console.error err)
                (js/alert (prn-str err))))))

(defn- store-consent-info! [on-consent-info consent-info]
  (when on-consent-info
    (on-consent-info consent-info))
  consent-info)

(defn initialize-if-allowed!
  ([] (initialize-if-allowed! {}))
  ([{:keys [on-consent-info] :as opts}]
   (when-not @initialized?
     (-> (j/call ads-consent :getConsentInfo)
         (.then (fn [consent-info]
                  (store-consent-info! on-consent-info consent-info)
                  (when (j/get consent-info :canRequestAds)
                    (initialize-sdk! opts))))
         (.catch (fn [err]
                   (js/console.log "Failed to get ads consent info" err)))))))

(defn initialize!
  ([] (initialize! {}))
  ([opts]
   (if gdpr-consent-enabled?
     (-> (j/call ads-consent :gatherConsent (consent-options opts))
         (.then (fn [_]
                  (initialize-if-allowed! opts)))
         (.catch (fn [err]
                   (js/console.log "Consent gathering failed" err)
                   (initialize-if-allowed! opts))))
     (initialize-sdk! opts))))

(defn show-privacy-options!
  ([] (show-privacy-options! {}))
  ([{:keys [on-consent-info] :as opts}]
   (when gdpr-consent-enabled?
     (-> (j/call ads-consent :requestInfoUpdate (consent-options opts))
         (.then (fn [consent-info]
                  (store-consent-info! on-consent-info consent-info)
                  (when (privacy-options-required? consent-info)
                    (-> (j/call ads-consent :showPrivacyOptionsForm)
                        (.then (fn [updated-consent-info]
                                 (store-consent-info! on-consent-info updated-consent-info)
                                 (initialize-if-allowed! opts)))))))
         (.catch (fn [err]
                   (js/console.log "Failed to show ads privacy options" err)))))))
