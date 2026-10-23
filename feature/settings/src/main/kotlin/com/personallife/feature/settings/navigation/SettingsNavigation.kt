package com.personallife.feature.settings.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.personallife.core.navigation.Screen
import com.personallife.feature.settings.presentation.SettingsScreen
import com.personallife.feature.settings.presentation.SettingsViewModel

fun NavController.navigateToSettings() {
    navigate(Screen.Settings.route)
}

fun NavGraphBuilder.settingsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToModuleManager: () -> Unit,
    onLoggedOut: () -> Unit
) {
    composable(Screen.Settings.route) {
        val viewModel: SettingsViewModel = hiltViewModel()
        SettingsScreen(
            viewModel = viewModel,
            onNavigateBack = onNavigateBack,
            onNavigateToModuleManager = onNavigateToModuleManager,
            onLoggedOut = onLoggedOut
        )
    }
}
