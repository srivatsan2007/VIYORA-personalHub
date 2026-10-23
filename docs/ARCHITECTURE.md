# Personal Hub — System Architecture (Phase 1 Foundation)

## 1. Architectural Philosophy

Personal Hub is engineered according to **Clean Architecture** principles and **Google's recommended Android app architecture**, enforcing strict separation of concerns, high testability, and a **Zero-Data-Leak** security posture.

```
┌─────────────────────────────────────────────────────────────┐
│                      :app Module                            │
│        (Application, MainActivity, AppNavHost DI)           │
└──────────────┬───────────────────────────────┬──────────────┘
               │                               │
┌──────────────▼───────────────────────────────▼──────────────┐
│                    Feature Modules                          │
│  :feature:auth    :feature:dashboard   :feature:profile     │
│  :feature:settings :feature:modulemanager                   │
└──────────────┬───────────────────────────────┬──────────────┘
               │                               │
┌──────────────▼───────────────────────────────▼──────────────┐
│                      Core Modules                           │
│  :core:designsystem :core:navigation   :core:data           │
│  :core:model        :core:common       :core:testing        │
└─────────────────────────────────────────────────────────────┘
```

---

## 2. Layer Responsibilities

### Presentation Layer (MVVM + Jetpack Compose)
- **UI Screens & Components**: Pure Jetpack Compose functions, stateless wherever possible, observing unidirectional immutable `UiState`.
- **ViewModels**: Powered by AndroidX ViewModel and Hilt. Manages screen logic, validation, and communicates strictly through domain models and repositories.
- **Design System (`:core:designsystem`)**: Shared Material 3 design system, themes (System/Light/Dark), and reusable atoms/molecules (`AppCard`, `PrimaryButton`, `GlassmorphicCard`, `LoadingStateView`).

### Domain & Contract Layer (`:core:model`, `:core:navigation`)
- **Domain Models**: Data models (`UserProfile`, `AppModule`, `DashboardWidget`, `UserPreferences`) containing IDs, timestamps, and validation logic.
- **Navigation Contracts**: Centralized routes and contract lambdas avoiding circular dependencies between feature modules.

### Data Layer (`:core:data`)
- **Repositories**: Exposes reactive Kotlin `Flow<Result<T>>` streams to presentation layers.
- **Local Storage**: 
  - **Jetpack Preferences DataStore**: Stores active theme mode, biometric lock state, disabled modules, and app preferences.
  - **Encrypted Room / SQLCipher (`AppDatabase`, `WidgetDao`)**: Active hardware-backed AES-256 encrypted database storing dashboard widgets, order, sizes, metrics, notes, and targets via `DatabasePassphraseProvider`.
- **Cloud Infrastructure**:
  - **Firebase Authentication**: User identity management with session restoration.
  - **Cloud Firestore**: Cloud sync for user records with strict per-user ownership rules.
  - **FirebaseDiagnostics**: Safe isolation providing clear instructions when `google-services.json` has not yet been added.

---

## 3. Zero-Data-Leak Security Blueprint

1. **Hardware-Backed Cryptography (`KeystoreHelper`)**:
   - AES-256-GCM encryption keys are generated and stored inside the Android Hardware Keystore (TEE / StrongBox). Keys are never extractable in software.
2. **Local-First & Offline Privacy**:
   - All core user data stays on device; network communication is guarded and only transmitted to authenticated user-owned paths.
3. **Biometric Guard (`BiometricAuthManager`)**:
   - Integrated with `androidx.biometric.BiometricPrompt`. Protects sensitive modules and application access.
4. **Anti-Snooping UI (`WindowSecurityHelper`)**:
   - Toggles `WindowManager.LayoutParams.FLAG_SECURE` to prevent screenshots, screen recording, and blank out app previews in the Android Recent Apps switcher.

---

## 4. Firestore Database Structure

```text
users/
└── {uid}/
    ├── profile: {
    │     uid: String,
    │     displayName: String,
    │     email: String,
    │     photoUrl: String?,
    │     bio: String,
    │     createdAt: Timestamp,
    │     updatedAt: Timestamp
    │   }
    ├── preferences: {
    │     themeMode: String ("SYSTEM" | "LIGHT" | "DARK"),
    │     isBiometricEnabled: Boolean,
    │     areNotificationsEnabled: Boolean,
    │     isFlagSecureEnabled: Boolean
    │   }
    ├── enabledModules: {
    │     disabledKeys: List<String>
    │   }
    └── dashboardConfiguration: {
          hiddenWidgetIds: List<String>,
          lastUpdated: Timestamp
        }
```

---

## 5. Module Independence & Scalability

Every feature is developed in isolation within its own Gradle module. Adding future modules (such as `:feature:health`, `:feature:expense`, `:feature:vault`) requires:
1. Creating the new feature module under `feature/<new_module>/`.
2. Implementing the standard package structure (`presentation`, `navigation`).
3. Adding the module entry to `settings.gradle.kts` and the module catalog registry.
4. Hooking the navigation destination into `AppNavHost` without touching existing features.

---

## 6. All-in-One Android Widget Studio Architecture

The Widget Studio subsystem introduces four decoupled modules dedicated to home-screen personalization:

```text
┌─────────────────────────────────────────────────────────────┐
│                  :feature:widgetstudio                      │
│      (Marketplace Gallery, Live Customization Studio)       │
└──────────────┬───────────────────────────────┬──────────────┘
               │                               │
┌──────────────▼──────────────┐ ┌──────────────▼──────────────┐
│     :core:widgetpreview     │ │      :core:widgetdata       │
│   LiquidGlass Components    │ │  Data Providers, Per-Id     │
│   Universal Preview Renderer│ │  Preferences Persistence    │
└──────────────┬──────────────┘ └──────────────┬──────────────┘
               │                               │
┌──────────────▼───────────────────────────────▼──────────────┐
│                    :core:widgetengine                       │
│      Domain Models, Registry, Presets & Capabilities        │
└─────────────────────────────────────────────────────────────┘
```

### Core Subsystem Responsibilities:
1. **`:core:widgetengine`**: Pure domain specifications. Defines 16 widget categories, presets (`Olive & Cream`, `Mint & Lavender`, `Frosted Glass`, `AMOLED Neon`), `WidgetSizePreset`, capabilities, and the central `WidgetRegistry`.
2. **`:core:widgetdata`**: Data layer. Provides reactive battery snapshots, offline inspiration quotes, usage statistics metrics, and instance-specific preferences persistence (`WidgetConfigurationRepository`).
3. **`:core:widgetpreview`**: High-fidelity UI engine. Implements the original iOS-inspired Liquid Glass design language (`LiquidGlassCard`, `LiquidGlassChip`, `LiquidGlassSlider`) and renders real-time previews inside the studio.
4. **`:feature:widgetstudio`**: User-facing studio. Houses the searchable catalog, category filter carousel, favorites filtering, interactive customization controls, launcher pinning requests (`AppWidgetManager.requestPinAppWidget`), and fallback manual placement guidance.
5. **`:app` Widget Providers**: Native Android OS integration. Implements `AnalogClockWidgetProvider`, `DigitalClockWidgetProvider`, `HybridClockWidgetProvider`, `BatteryWidgetProvider`, `QuotesWidgetProvider`, `ScreenTimeWidgetProvider`, `MusicWidgetProvider`, and `WidgetConfigurationActivity`.

