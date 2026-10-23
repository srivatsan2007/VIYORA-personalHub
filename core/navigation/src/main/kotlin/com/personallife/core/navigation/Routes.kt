package com.personallife.core.navigation

/**
 * Top-level navigation destinations.
 */
sealed class Screen(val route: String) {
    data object AuthLogin : Screen("auth/login")
    data object AuthRegister : Screen("auth/register")
    data object Dashboard : Screen("dashboard")
    data object Profile : Screen("profile")
    data object EditProfile : Screen("profile/edit")
    data object Settings : Screen("settings")
    data object ModuleManager : Screen("module_manager")
    data object WidgetStudio : Screen("widget_studio")
    data object WidgetCustomization : Screen("widget_customization/{widgetId}") {
        fun createRoute(widgetId: String) = "widget_customization/$widgetId"
    }
    
    // Future placeholders
    data object ModulePlaceholder : Screen("module_placeholder/{moduleId}") {
        fun createRoute(moduleId: String) = "module_placeholder/$moduleId"
    }
}
