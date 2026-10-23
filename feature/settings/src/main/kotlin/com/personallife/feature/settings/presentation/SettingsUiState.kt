package com.personallife.feature.settings.presentation

import com.personallife.core.model.ThemeMode
import com.personallife.core.model.UserPreferences
import com.personallife.core.model.UserProfile

data class SettingsUiState(
    val userPreferences: UserPreferences = UserPreferences(),
    val userProfile: UserProfile? = null,
    val isThemeDialogVisible: Boolean = false,
    val isLogoutDialogVisible: Boolean = false,
    val appVersion: String = "1.0.0 (Phase 1 Foundation)"
)
