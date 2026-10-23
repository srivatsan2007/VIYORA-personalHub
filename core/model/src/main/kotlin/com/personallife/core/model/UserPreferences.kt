package com.personallife.core.model

/**
 * Theme selection modes.
 */
enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

/**
 * User persistent preferences.
 */
data class UserPreferences(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val isBiometricEnabled: Boolean = false,
    val areNotificationsEnabled: Boolean = true,
    val languageCode: String = "en",
    val isFlagSecureEnabled: Boolean = true
)
