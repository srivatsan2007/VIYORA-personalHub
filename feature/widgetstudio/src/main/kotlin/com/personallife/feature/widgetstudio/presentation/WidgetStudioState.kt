package com.personallife.feature.widgetstudio.presentation

import com.personallife.core.widgetengine.model.WidgetCategory
import com.personallife.core.widgetengine.model.WidgetConfiguration
import com.personallife.core.widgetengine.model.WidgetDefinition

/**
 * UI State for the Widget Marketplace / Studio catalog screen.
 */
data class WidgetStudioUiState(
    val searchQuery: String = "",
    val selectedCategory: WidgetCategory? = null,
    val showFavoritesOnly: Boolean = false,
    val favoriteWidgetIds: Set<String> = emptySet(),
    val filteredWidgets: List<WidgetDefinition> = emptyList(),
    val featuredWidgets: List<WidgetDefinition> = emptyList(),
    val recentConfigurations: List<WidgetConfiguration> = emptyList(),
    val isLoading: Boolean = false,
    val pinStatusMessage: String? = null
)

/**
 * UI State for the Widget Customization Studio editor screen.
 */
data class WidgetCustomizationUiState(
    val definition: WidgetDefinition? = null,
    val configuration: WidgetConfiguration? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val isPinSupported: Boolean = true,
    val showManualAddDialog: Boolean = false,
    val showPermissionDialog: Boolean = false,
    val statusMessage: String? = null
)
