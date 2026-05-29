# react-native-cljs

ClojureScript bindings and Reagent tooling for React Native.

`react-native-cljs` lets CLJS apps use React Native with Clojure data,
namespaced Hiccup tags, macro-generated JS props, and small wrappers around the
native modules apps reach for every day.

## Highlights

- Write native views as Hiccup: `:rn/view`, `:rn/text`, `:rn/pressable`.
- Keep props in kebab-case Clojure maps; emit camelCased JS at the boundary.
- Build navigation from keywords and Clojure route params.
- Use focused wrappers for React Native, React hooks, React Navigation,
  AsyncStorage, Firebase, Reanimated, safe areas, IAP, ads, clipboard,
  geolocation, and splash screens.
- Register ready-to-use re-frame effects for common async native calls.

## Install

Local development:

```clojure
{:deps {react-native/react-native {:local/root "react-native"}}}
```

Git dependency:

```clojure
{:deps {ulisesmac/react-native-cljs
        {:git/url "git@github.com:ulisesmac/react-native-cljs.git"
         :git/sha "<sha>"}}}
```

Install the matching npm/native packages in the host React Native app.

## Quick Start

Install the Reagent compiler once during app startup:

```clojure
(ns app.$init
  (:require ["react-native" :as rn]
            [react-native.reagent-compiler.core :as compiler]
            [reagent.core :as r]))

(r/set-default-compiler!
 (compiler/create {:js-component-libs {:rn rn}
                   :kebab-case-component-names? true}))
```

Use React Native components from Hiccup:

```clojure
(ns app.view
  (:require [react-native.utils :refer [defstyle]]))

(defstyle root-style
  {:flex 1
   :padding-horizontal 16
   :justify-content :center})

(defstyle title-style
  {:font-size 20
   :font-weight "600"})

(defn root []
  ;; defstyle vars are JS objects.
  [:rn/view {:style root-style}
   [:rn/text {:style title-style}
    "React Native, shaped for ClojureScript"]])
```

## Core API

- `react-native.core`: platform, linking, sharing, permissions, keyboard,
  dimensions, app state, alerts, hooks, and headless tasks.
- `react-native.utils`: `style`, `prop`, `defstyle`, `defprop`,
  `asset-require`, `add-styles`, and JS prop conversion.
- `react-native.reagent-compiler.*`: namespaced Hiccup tags and Clojure prop
  conversion for native components.
- `react-native.react.*`: React hooks with Clojure-friendly dependency arrays.
- `react-native.react-navigation.*`: keyword navigation, static navigators,
  route params, navigation refs, and re-frame effects.
- `react-native.async-storage.*`: EDN-backed AsyncStorage helpers and effects.
- `react-native.firebase.*`: Analytics, Auth, Firestore, Messaging, and
  matching re-frame effects.
- `react-native.reanimated.core`: shared values, timing, springs, sequences,
  easing, and simple entering/exiting animations.
- `react-native.safe-area-context.core`: safe-area insets and window frames.

## Style

The library keeps native interop explicit and Clojure-shaped:

```clojure
(defstyle card-style
  {:padding-top 12
   :background-color "#ffffff"})

(prop {:header-shown false
       :content-style {:background-color "#ffffff"}})
```

Use literal maps with `defstyle`, `style`, and `prop` whenever possible. Use
`react-native.utils/->js-prop-obj` when a runtime Clojure map must become JS
props.
