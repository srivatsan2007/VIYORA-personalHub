# Personal Hub — Module Guide

This document describes all Gradle modules, their responsibilities, and dependency boundaries.

---

## 1. Module Catalog & Responsibilities

| Module | Type | Purpose | Dependencies |
| :--- | :--- | :--- | :--- |
| **`:app`** | Android Application | App initialization, Hilt setup, `MainActivity`, theme wrapper, and global navigation host (`AppNavHost`). | All `:feature:*` and `:core:*` |
| **`:core:common`** | Android Library | Common utilities, coroutine dispatchers, input validation, Android Keystore encryption, Biometric prompt, and `FLAG_SECURE`. | AndroidX Core, Biometric, Crypto |
| **`:core:designsystem`** | Android Library | Material 3 design system, color palette (Dark & Light), typography, shapes, and reusable UI components. | Compose BOM, Material 3, Icons |
| **`:core:model`** | Android Library | Pure domain models (`UserProfile`, `AppModule`, `DashboardWidget`, `UserPreferences`). | Kotlin Coroutines |
| **`:core:navigation`** | Android Library | Central navigation contracts, screens, routes, and route argument definitions. | Navigation Compose |
| **`:core:data`** | Android Library | Data sources, repositories (`AuthRepository`, `UserRepository`, `ModuleRepository`), DataStore, Room SQLCipher, and Firebase. | `:core:common`, `:core:model`, Firebase, Room, DataStore |
| **`:core:testing`** | Android Library | Test dispatchers (`MainDispatcherRule`) and in-memory test fakes for all repositories. | `:core:common`, `:core:model`, `:core:data`, Coroutines Test, JUnit |
| **`:feature:auth`** | Android Library | Registration, Email/Password login, logout, password visibility, and session restoration. | `:core:common`, `:core:model`, `:core:designsystem`, `:core:navigation`, `:core:data` |
| **`:feature:dashboard`**| Android Library | Primary hub dashboard, personalized greeting, quick actions, customizable in-app widgets, and module spaces. | `:core:common`, `:core:model`, `:core:designsystem`, `:core:navigation`, `:core:data` |
| **`:feature:profile`**  | Android Library | User profile display, editable profile (display name, bio), and account metadata. | `:core:common`, `:core:model`, `:core:designsystem`, `:core:navigation`, `:core:data` |
| **`:feature:settings`** | Android Library | App settings, theme selector (System/Light/Dark), Biometric toggle, Screen Shield toggle, and logout. | `:core:common`, `:core:model`, `:core:designsystem`, `:core:navigation`, `:core:data` |
| **`:feature:modulemanager`**| Android Library | Module management center where users can enable/disable modules. **CRM is disabled by default.** | `:core:common`, `:core:model`, `:core:designsystem`, `:core:navigation`, `:core:data` |

---

## 2. Phase 1 vs. Planned Modules

| Module ID | Module Title | Phase 1 Status | Target Phase |
| :--- | :--- | :--- | :--- |
| `dashboard` | Dashboard | **Active (Built)** | Phase 1 |
| `personal_vault` | Personal Vault | **Planned (Schema Ready)** | Phase 3 |
| `health_tracker` | Health Tracker | **Planned (Schema Ready)** | Phase 3 |
| `education_hub` | Education Hub | **Planned (Schema Ready)** | Phase 3 |
| `expense_tracker`| Expense Tracker | **Planned (Schema Ready)** | Phase 3 |
| `screen_time` | Screen Time Controller | **Planned (Schema Ready)** | Phase 4 |
| `ai_assistant` | AI Assistant | **Planned (Schema Ready)** | Phase 4 |
| `crm` | Personal CRM | **Planned & Disabled by default** | Phase 5 |

---

## 3. How to Add a New Feature Module

1. Create directory `feature/<feature_name>/`
2. Add `build.gradle.kts` referencing `:core:common`, `:core:model`, `:core:designsystem`, `:core:navigation`, and `:core:data`.
3. Add `include(":feature:<feature_name>")` to `settings.gradle.kts`.
4. Create package `com.personallife.feature.<feature_name>` with subpackages:
   - `presentation`: Composable screens, ViewModel, and UiState.
   - `navigation`: Route definition and `NavGraphBuilder` extension.
5. In `:app`, add the dependency and hook the composable into `AppNavHost.kt`.
