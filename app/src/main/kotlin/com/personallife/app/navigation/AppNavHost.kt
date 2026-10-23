package com.personallife.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.personallife.core.navigation.Screen
import com.personallife.feature.auth.navigation.authGraph
import com.personallife.feature.auth.navigation.navigateToLogin
import com.personallife.feature.auth.navigation.navigateToRegister
import com.personallife.feature.dashboard.navigation.dashboardScreen
import com.personallife.feature.dashboard.navigation.navigateToDashboard
import com.personallife.feature.modulemanager.navigation.moduleManagerScreen
import com.personallife.feature.modulemanager.navigation.navigateToModuleManager
import com.personallife.feature.profile.navigation.navigateToEditProfile
import com.personallife.feature.profile.navigation.navigateToProfile
import com.personallife.feature.profile.navigation.profileGraph
import com.personallife.feature.settings.navigation.navigateToSettings
import com.personallife.feature.settings.navigation.settingsScreen
import com.personallife.feature.widgetstudio.navigation.navigateToWidgetCustomization
import com.personallife.feature.widgetstudio.navigation.navigateToWidgetStudio
import com.personallife.feature.widgetstudio.navigation.widgetCustomizationScreen
import com.personallife.feature.widgetstudio.navigation.widgetStudioScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Authentication Feature Graph
        authGraph(
            onNavigateToRegister = { navController.navigateToRegister() },
            onNavigateToLogin = { navController.navigateToLogin() },
            onAuthSuccess = { navController.navigateToDashboard() }
        )

        // Dashboard Feature Screen
        dashboardScreen(
            onNavigateToProfile = { navController.navigateToProfile() },
            onNavigateToSettings = { navController.navigateToSettings() },
            onNavigateToWidgetStudio = { navController.navigateToWidgetStudio() }
        )

        // Profile Feature Graph
        profileGraph(
            onNavigateBack = { navController.popBackStack() },
            onNavigateToEdit = { navController.navigateToEditProfile() }
        )

        // Settings Feature Screen
        settingsScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateToModuleManager = { navController.navigateToModuleManager() },
            onLoggedOut = { navController.navigateToLogin() }
        )

        // Module Manager Screen
        moduleManagerScreen(
            onNavigateBack = { navController.popBackStack() }
        )

        // Widget Studio Catalog Screen
        widgetStudioScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateToCustomization = { widgetId ->
                navController.navigateToWidgetCustomization(widgetId)
            }
        )

        // Widget Customization Studio Screen
        widgetCustomizationScreen(
            onNavigateBack = { navController.popBackStack() }
        )
    }
}
