# Viyora — Your Personal , Your Way

A privacy-first personal digital universe and All-in-One Android Widget Studio engineered with **Kotlin**, **Jetpack Compose**, **Material 3**, **Liquid Glass UI**, and a **Zero-Data-Leak** security architecture.

---

## 1. Phase 1 Overview

Phase 1 establishes the production-grade foundational architecture for the Personal Hub ecosystem. It includes a scalable multi-module Gradle structure, central version catalog, centralized navigation, hardware-backed security, authentication, personalized dashboard, profile editing, dynamic settings, and an extensible module management system.

### Key Pillars
- **Strict Modular Clean Architecture**: Decoupled feature modules and shared core infrastructure.
- **Zero-Data-Leak Security**: Android Keystore hardware-backed encryption, SQLCipher encrypted local database, BiometricPrompt, and `FLAG_SECURE` window shield.
- **Material 3 Design System**: Custom theme supporting dynamic System, Light, and Dark modes with glassmorphic cards and smooth micro-animations.
- **Module Manager**: Allows users to enable/disable life management modules. **Personal CRM is disabled by default.**
- **Safe Offline / Firebase Graceful Isolation**: Firebase Authentication with diagnostic handling if `google-services.json` has not yet been supplied.

---

## 2. Technology Stack

- **Language**: Kotlin 2.0.21
- **UI Framework**: Jetpack Compose + Material 3 (BOM 2024.10.01)
- **Architecture**: Clean Architecture with MVVM & unidirectional data flow
- **Dependency Injection**: Hilt 2.52 with KSP
- **Navigation**: Navigation Compose 2.8.3
- **Async & Streams**: Kotlin Coroutines & Flow
- **Authentication**: Firebase Authentication
- **Cloud Database**: Cloud Firestore
- **Local Persistence**: Jetpack Preferences DataStore & Room with SQLCipher
- **Security & Biometrics**: AndroidX Biometric 1.2.0 & AndroidX Security Crypto
- **Build System**: Gradle 8.11+ with Kotlin DSL and Version Catalog (`gradle/libs.versions.toml`)

---

## 3. Project Structure

```text
PersonalLifeApp/
├── settings.gradle.kts
├── build.gradle.kts
├── gradle.properties
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
│
├── app/                      # Application entry point, MainActivity & AppNavHost
│
├── core/
│   ├── common/               # Result models, dispatchers, validation & Keystore
│   ├── designsystem/         # Material 3 theme, typography, shapes & components
│   ├── model/                # Pure domain models (User, Module, Widget, Preferences)
│   ├── navigation/           # Central navigation routes and screen contracts
│   ├── data/                 # Repositories, DataStore, Firestore, SQLCipher & Hilt
│   └── testing/              # Testing dispatchers and repository fakes
│
├── feature/
│   ├── auth/                 # Login, Registration, Validation & Session
│   ├── dashboard/            # Focus overview, quick actions & in-app widgets
│   ├── profile/              # Profile display & editing
│   ├── settings/             # Themes, Biometrics, Screen Shield & App info
│   └── modulemanager/        # Life spaces manager (CRM disabled by default)
│
├── docs/
│   ├── ARCHITECTURE.md       # Architectural deep-dive & security model
│   └── MODULE_GUIDE.md       # Module catalog & expansion guide
│
├── firebase/
│   ├── SETUP.md              # Firebase project configuration steps
│   └── firestore.rules       # Production per-user security rules
│
└── README.md
```

---

## 4. Building and Running

### Prerequisites
- Android Studio Ladybug / Koala or later
- JDK 21 (bundled with Android Studio)
- Android SDK Platform 35 / 36

### Build Commands
To check project structure:
```bash
./gradlew projects
```

To compile and build the debug APK:
```bash
./gradlew assembleDebug
```

To run unit tests across all modules:
```bash
./gradlew test
```

---

## 5. Phase Feature Matrix

| Feature | Phase 1 & 2 Status | Roadmap Target |
| :--- | :--- | :--- |
| Multi-module Clean Architecture | **Completed** | Foundation (Phase 1) |
| Centralized Version Catalog | **Completed** | Foundation (Phase 1) |
| Material 3 Design System & Theme | **Completed** | Foundation (Phase 1) |
| Hardware Keystore & Biometric Lock | **Completed** | Foundation (Phase 1) |
| Screen Shield (`FLAG_SECURE`) | **Completed** | Foundation (Phase 1) |
| Firebase Auth & Session Restore | **Completed** | Foundation (Phase 1) |
| Profile Display & Editing | **Completed** | Foundation (Phase 1) |
| Module Enable/Disable (CRM off by default) | **Completed** | Foundation (Phase 1) |
| Unit Tests & Repository Fakes | **Completed** | Foundation (Phase 1) |
| SQLCipher Room DB Persistence | **Completed** | Phase 2 |
| Widget Framework & Central Registry | **Completed** | Phase 2 |
| Adaptive 2-Column Grid Dashboard | **Completed** | Phase 2 |
| Half vs Full Span Widget Layouts | **Completed** | Phase 2 |
| Interactive Predefined Widgets & Bar Chart | **Completed** | Phase 2 |
| Education Planner & Study SRS Widget | **Completed** | Phase 2 |
| Tabbed Widget Manager (Active & Catalog) | **Completed** | Phase 2 |
| Customizer Modal (Palette, Size, Goals) | **Completed** | Phase 2 |
| OS Home Screen Widgets (Analog, Digital, Hybrid, Battery, Quotes, Screen Time, Music) | **Completed** | Widget Studio |
| All-in-One Android Widget Studio & Marketplace | **Completed** | Widget Studio |
| Liquid Glass UI (Translucent, Opacity, Sliders, Gradients) | **Completed** | Widget Studio |
| Theme Presets (Olive & Cream, Mint & Lavender, Frosted Glass, AMOLED) | **Completed** | Widget Studio |
| Per-Instance Customization (`appWidgetId` Persistence) | **Completed** | Widget Studio |
| Launcher Pinning (`requestPinAppWidget`) + Manual Guide | **Completed** | Widget Studio |
| Personal Vault (Documents & Notes) | Planned (Schema Ready) | Phase 3 |
| Health & Fitness Tracker | Planned (Schema Ready) | Phase 3 |
| Education Hub & Timetable | Planned (Schema Ready) | Phase 3 |
| Expense & Budget Tracker | Planned (Schema Ready) | Phase 3 |
| Screen Time Controller & App Blocker | Planned (Schema Ready) | Phase 4 |
| AI Assistant & Private Summaries | Planned | Phase 4 |
| Personal CRM Implementation | Planned | Phase 5 |

---

## 6. All-in-One Android Widget Studio

The Widget Studio is accessible directly from the Dashboard header icon (`Widgets`) and provides a full-featured marketplace, customization engine, and home-screen launcher integration.

### Capabilities:
- **16 Widget Categories**: Clocks, Music, Battery, Quotes, Weather, Gallery, Shortcuts, Screen Time, Calendar, Tasks, Education, Finance, Health, Vault, System, and Utilities.
- **Liquid Glass Design Language**: Original iOS-inspired aesthetics with translucent glass layers, subtle borders, backdrop highlights, and smooth sliders.
- **Curated Theme Presets**:
  - `Olive & Cream` (Organic deep olive `#232A1C` with lime-mint `#A3E635` accents and warm cream typography)
  - `Mint & Lavender` (Violet-slate `#181829` with vivid mint `#34D399` and soft lavender `#EDE9FE`)
  - `Frosted Glass` (Slate `#1E293B` with sky cyan `#38BDF8`)
  - `AMOLED Pure Black` (True zero-nit black `#000000` with vibrant cyan `#00F5FF`)
  - `Sunset Amber`, `Rose Quartz`, `Deep Ocean`, `Clear Translucent`
- **Interactive Live Preview**: Instant visual feedback as users change grid sizes, surface opacity, corner radius, borders, and custom quotes.
- **Per-Instance Customization**: Every widget added to the home screen maintains independent configurations keyed to its `appWidgetId`.
- **Launcher Pinning & Configuration**: Uses `AppWidgetManager.requestPinAppWidget` (Android 8.0+) with fallback step-by-step guidance for custom launchers and standard `WidgetConfigurationActivity`.

