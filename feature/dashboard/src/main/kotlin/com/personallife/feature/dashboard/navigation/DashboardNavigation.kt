package com.personallife.feature.dashboard.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.personallife.core.navigation.Screen
import com.personallife.feature.dashboard.presentation.DashboardScreen
import com.personallife.feature.dashboard.presentation.DashboardViewModel

fun NavController.navigateToDashboard() {
    navigate(Screen.Dashboard.route) {
        popUpTo(Screen.AuthLogin.route) { inclusive = true }
    }
}

fun NavGraphBuilder.dashboardScreen(
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToWidgetStudio: () -> Unit = {}
) {
    composable(Screen.Dashboard.route) {
        val viewModel: DashboardViewModel = hiltViewModel()
        DashboardScreen(
            viewModel = viewModel,
            onNavigateToProfile = onNavigateToProfile,
            onNavigateToSettings = onNavigateToSettings,
            onNavigateToWidgetStudio = onNavigateToWidgetStudio
        )
    }
}
