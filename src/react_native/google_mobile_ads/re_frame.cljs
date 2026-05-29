(ns react-native.google-mobile-ads.re-frame
  (:require
   [re-frame.core :as rf]
   [react-native.google-mobile-ads.core :as ads]))

(defn- store-consent-info! [consent-info]
  (rf/dispatch [:evt.ads/set-consent-info consent-info])
  consent-info)

(defn- set-ready! [_]
  (rf/dispatch [:evt.ads/set-ready]))

(rf/reg-sub :sub.root/ads :-> :ads)

(rf/reg-sub
 :sub.ads/ready?
 :<- [:sub.root/ads]
 :<- [:sub.iap/ad-free?]
 (fn [[ads ad-free?]]
   (and (:ready ads) (not ad-free?))))

(rf/reg-sub
 :sub.ads/privacy-options-required?
 (fn [db]
   (and ads/gdpr-consent-enabled?
        (get-in db [:ads :privacy-options-required?] false))))

(rf/reg-event-fx
 :evt.ads/set-ready
 (fn [{db :db} _]
   {:db (assoc-in db [:ads :ready] true)}))

(rf/reg-event-fx
 :evt.ads/set-consent-info
 (fn [{db :db} [_ consent-info]]
   {:db (assoc-in db [:ads :privacy-options-required?] (ads/privacy-options-required? consent-info))}))

(defn initialize! []
  (ads/initialize! {:on-consent-info store-consent-info!
                    :on-ready        set-ready!}))

(rf/reg-fx :fx.ads/initialize initialize!)

(defn show-privacy-options! []
  (ads/show-privacy-options! {:on-consent-info store-consent-info!
                              :on-ready        set-ready!}))
