# Personal Hub — Permissions, Privacy & Security Guide

Personal Hub adheres to a **Zero-Cloud, On-Device Privacy Architecture**. Personal data, credentials, and usage statistics are never transmitted to external servers. Permissions are requested strictly on-demand when a user activates a widget that interacts with Android system services.

---

## 1. System Permissions Breakdown

| Permission | Android Constant | Required By | Reason for Request | Fallback Behavior if Denied |
| :--- | :--- | :--- | :--- | :--- |
| **Usage Stats** | `android.permission.PACKAGE_USAGE_STATS` | Screen Time Widgets (`screentime_progress_ring`) | Queries `UsageStatsManager` to aggregate daily minutes spent across apps and compare with your daily focus goal. | Shows demonstrative goal progress and displays an informational prompt guiding user to enable access. |
| **Media Controller** | `android.permission.MEDIA_CONTENT_CONTROL` | Music Controllers (`music_compact_controller`) | Reads current track metadata (artist, title) from active media sessions and relays Play/Pause controls. | Displays default ambient music status. |
| **Location** | `android.permission.ACCESS_FINE_LOCATION` | Weather Widgets (`weather_current_condition`) | Obtains coarse/fine location coordinates to retrieve accurate meteorological forecasts. | Allows manual city selection without requiring GPS. |
| **Biometric** | `android.permission.USE_BIOMETRIC` | Vault Shortcut (`vault_secure_shortcut`) | Hardware-backed fingerprint / facial authentication before revealing sensitive encrypted scratchpads or documents. | PIN/Passphrase fallback. |
| **Photo Access** | Android Photo Picker (System) | Photo Frames (`gallery_photo_frame`) | Uses Android Photo Picker (`ActivityResultContracts.PickVisualMedia`). | No broad `READ_EXTERNAL_STORAGE` permission required. Only user-selected images are accessible. |
| **Battery Monitoring** | Sticky Broadcast | Battery Gauges (`battery_circular_gauge`) | Reads `Intent.ACTION_BATTERY_CHANGED` broadcast from Android BatteryManager. | 100% permission-free passive broadcast with zero background polling. |

---

## 2. In-App User Transparency

When a user selects a widget requiring special access (such as Usage Access), the Widget Customization Studio displays `PermissionExplanationDialog`.

This dialog:
1. Explains why the metric is needed.
2. Affirms that no data is collected, logged, or uploaded.
3. Provides a single-tap button opening the exact Android system settings page (e.g. `Settings.ACTION_USAGE_ACCESS_SETTINGS`).

---

## 3. Data Protection & Keystore Encryption

- All persistent local databases utilize **SQLCipher AES-256 GCM** encryption.
- Passphrases are generated using the hardware-backed **Android KeyStore provider** (`KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT`).
- Home screen widgets never display unencrypted sensitive financial or personal vault documents without explicit user confirmation.
