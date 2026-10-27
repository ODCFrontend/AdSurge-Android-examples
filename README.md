# AdSurge Java Demo

This is the official Java reference implementation for integrating the **AdSurge SDK** on Android. It demonstrates how to initialize the SDK and properly load, display, and handle lifecycle events for all supported ad formats.

## Features Included

- **Immersive UI**: Edge-to-edge support with window insets.
- **5 Ad Formats**: Complete lifecycle implementations for:
  - Interstitial Ad
  - Rewarded Ad
  - Banner Ad
  - Native Ad
  - App Open Ad

## Prerequisites

- **Android Studio** Hedgehog | 2023.1.1 or newer.
- **Android SDK**: API level 21 or higher.

## How to Build and Run

1. Open **Android Studio**.
2. Select **File > Open...** and navigate to the root directory of this repository (`JavaDemo`).
3. Allow Gradle to sync the dependencies (this will download the necessary AdSurge SDK and Material Components).
4. Connect an Android device (or start an Emulator) running Android 5.0 (API 21) or higher.
5. Click the **Run 'app'** button (the green play icon) in the toolbar, or run `./gradlew assembleDebug` in the terminal to build the APK manually.

## Code Structure Highlights

- `MainActivity.java`: Contains the core logic for initializing the AdSurge SDK (`AdSurgeAdSdk.getInstance().init(...)`) and auto-loading each of the 5 ad formats when initialization succeeds.
- `activity_main.xml`: The main dashboard featuring a modern, flat Material 3 layout.
- `item_native_ad.xml`: The custom UI layout template used exclusively for inflating Native Ads with proper aspect ratios and action button placement.

## Notes

- **SDK Version**: This demo depends on `com.adsurge.sdk:adn-sdk:2.0.1` (see `app/build.gradle.kts`).
- **Install Permission (SDK 2.0.1+)**: The SDK no longer declares `android.permission.REQUEST_INSTALL_PACKAGES` by default (Google Play sensitive permission). This demo does not use the APK direct-download-and-install ad flow, so no manifest change is needed here; if your own app relies on that flow, declare the permission yourself.
- **CMP Consent Page (SDK 2.0.0+)**: For App Open, Interstitial and Rewarded ads, `showAd()` may first present an in-SDK consent page. If the user declines or the page fails to load, `onAdShowFailed` is called with `ERROR_CODE_COMPLIANCE` and the ad instance is consumed; the demo disables the Show button so you can load a new ad.
- **Initialization Retry (SDK 2.0.0+)**: The SDK may retry initialization internally, so `onStartFailed` is only called once retries are exhausted. Server-side rejections are reported as `ERROR_CODE_INTERNAL_ERROR` with the message `ret=<value>, ret_msg=<server message>`.
- **AppOps/Attribution Warnings**: To suppress common system `AppOps` logs, this demo uses `createAttributionContext("AdSurgeAd")` on Android 12 (API 31) and higher.
- **Test Ad Units**: The project is currently configured with test Ad Unit IDs (e.g., APP_ID: `10034`). You must replace these with your actual production IDs before publishing your app.
