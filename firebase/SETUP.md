# Firebase Setup Guide for Personal Hub

This guide walks you through connecting your Firebase project to Personal Hub.

---

## Step 1: Create a Firebase Project

1. Open the [Firebase Console](https://console.firebase.google.com/).
2. Click **Add Project** and name it `Personal Hub` (or your preferred name).
3. Disable or enable Google Analytics (optional, for maximum privacy disable analytics).
4. Click **Create Project**.

---

## Step 2: Register the Android App

1. In the Project Overview, click the **Android** icon to add an Android application.
2. Enter the exact Android package name:
   ```text
   com.personallife.app
   ```
3. Enter an App nickname (e.g., `Personal Hub Android`).
4. (Optional) Provide your Debug signing certificate SHA-1:
   ```bash
   keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
   ```
5. Click **Register app**.

---

## Step 3: Download and Place `google-services.json`

1. Download the generated `google-services.json` file.
2. Place the file inside the `app/` directory of this repository:
   ```text
   Personal Hub/
   └── app/
       ├── google-services.json   <-- PLACE HERE
       ├── build.gradle.kts
       └── src/
   ```
3. Rebuild the project in Android Studio or via Gradle:
   ```bash
   ./gradlew assembleDebug
   ```

---

## Step 4: Enable Firebase Authentication

1. In the Firebase Console, go to **Build -> Authentication**.
2. Click **Get Started**.
3. Under the **Sign-in method** tab, click **Email/Password**.
4. Enable the **Email/Password** toggle (keep Email link disabled).
5. Click **Save**.

---

## Step 5: Enable Cloud Firestore & Apply Security Rules

1. In the Firebase Console, go to **Build -> Firestore Database**.
2. Click **Create Database**.
3. Select a location close to your region.
4. Choose **Start in production mode**.
5. Once created, navigate to the **Rules** tab, paste the contents of `firebase/firestore.rules`, and click **Publish**.

---

## Safe Offline / Diagnostic Mode

If `google-services.json` is not present, the app automatically detects this and gracefully presents an informative banner rather than crashing with unhandled exceptions.
