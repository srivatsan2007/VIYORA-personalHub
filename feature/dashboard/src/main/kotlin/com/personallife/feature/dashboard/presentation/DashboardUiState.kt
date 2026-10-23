package com.personallife.feature.dashboard.presentation

import com.personallife.core.model.AppModule
import com.personallife.core.model.DashboardConfiguration
import com.personallife.core.model.DashboardWidget
import com.personallife.core.model.UserProfile
import com.personallife.core.model.WidgetSize

data class DashboardUiState(
    val userProfile: UserProfile? = null,
    val enabledModules: List<AppModule> = emptyList(),
    val dashboardConfig: DashboardConfiguration = DashboardConfiguration(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val isWidgetManagerOpen: Boolean = false,
    val widgetManagerTab: Int = 0, // 0 = Active Widgets, 1 = Template Catalog
    val selectedPlaceholderModule: AppModule? = null,
    val selectedWidgetForCustomization: DashboardWidget? = null,
    val customizingAccentHex: String = "",
    val customizingSize: WidgetSize = WidgetSize.FULL,
    val customizingWaterTarget: String = "",
    val customizingDailyBudget: String = "",
    val customizingScreenLimit: String = "",
    val isQuickNoteEditorOpen: Boolean = false,
    val editingQuickNoteText: String = ""
)
