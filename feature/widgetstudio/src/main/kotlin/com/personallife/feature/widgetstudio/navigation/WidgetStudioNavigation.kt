package com.personallife.feature.widgetstudio.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.personallife.core.navigation.Screen
import com.personallife.feature.widgetstudio.presentation.WidgetCustomizationStudioScreen
import com.personallife.feature.widgetstudio.presentation.WidgetStudioScreen

fun NavController.navigateToWidgetStudio() {
    navigate(Screen.WidgetStudio.route)
}

fun NavController.navigateToWidgetCustomization(widgetId: String) {
    navigate(Screen.WidgetCustomization.createRoute(widgetId))
}

fun NavGraphBuilder.widgetStudioScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCustomization: (String) -> Unit
) {
    composable(Screen.WidgetStudio.route) {
        WidgetStudioScreen(
            onNavigateBack = onNavigateBack,
            onNavigateToCustomization = onNavigateToCustomization
        )
    }
}

fun NavGraphBuilder.widgetCustomizationScreen(
    onNavigateBack: () -> Unit
) {
    composable(
        route = Screen.WidgetCustomization.route,
        arguments = listOf(
            navArgument("widgetId") {
                type = NavType.StringType
            }
        )
    ) {
        WidgetCustomizationStudioScreen(
            onNavigateBack = onNavigateBack
        )
    }
}
