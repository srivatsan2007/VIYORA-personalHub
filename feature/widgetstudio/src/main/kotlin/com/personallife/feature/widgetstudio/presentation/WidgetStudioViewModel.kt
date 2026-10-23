package com.personallife.feature.widgetstudio.presentation

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personallife.core.widgetdata.repository.WidgetConfigurationRepository
import com.personallife.core.widgetengine.model.WidgetCategory
import com.personallife.core.widgetengine.model.WidgetDefinition
import com.personallife.core.widgetengine.registry.WidgetRegistry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WidgetStudioViewModel @Inject constructor(
    private val widgetRepository: WidgetConfigurationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WidgetStudioUiState())
    val uiState: StateFlow<WidgetStudioUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow<WidgetCategory?>(null)
    private val _showFavoritesOnly = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            combine(
                _searchQuery,
                _selectedCategory,
                _showFavoritesOnly,
                widgetRepository.favoriteWidgetIds,
                widgetRepository.recentlyCustomized
            ) { query, category, favOnly, favorites, recent ->
                val allDefinitions = WidgetRegistry.definitions

                val filtered = allDefinitions.filter { def ->
                    val matchesCategory = (category == null || def.category == category)
                    val matchesQuery = query.isBlank() ||
                            def.name.contains(query, ignoreCase = true) ||
                            def.description.contains(query, ignoreCase = true) ||
                            def.category.title.contains(query, ignoreCase = true)
                    val matchesFavorite = !favOnly || favorites.contains(def.id)

                    matchesCategory && matchesQuery && matchesFavorite
                }

                val featured = allDefinitions.filter { it.status.isInstallable }.take(4)

                WidgetStudioUiState(
                    searchQuery = query,
                    selectedCategory = category,
                    showFavoritesOnly = favOnly,
                    favoriteWidgetIds = favorites,
                    filteredWidgets = filtered,
                    featuredWidgets = featured,
                    recentConfigurations = recent,
                    isLoading = false
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelect(category: WidgetCategory?) {
        _selectedCategory.value = if (_selectedCategory.value == category) null else category
    }

    fun onToggleFavoritesOnly() {
        _showFavoritesOnly.update { !it }
    }

    fun onToggleFavorite(widgetId: String) {
        viewModelScope.launch {
            widgetRepository.toggleFavorite(widgetId)
        }
    }

    fun requestPinWidget(context: Context, definition: WidgetDefinition): Boolean {
        val providerClass = definition.providerClassName
        if (providerClass.isNullOrBlank()) {
            _uiState.update { it.copy(pinStatusMessage = "Home screen provider in development for ${definition.name}.") }
            return false
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            if (appWidgetManager.isRequestPinAppWidgetSupported) {
                try {
                    val componentName = ComponentName(context, providerClass)
                    val success = appWidgetManager.requestPinAppWidget(componentName, null, null)
                    _uiState.update {
                        it.copy(
                            pinStatusMessage = if (success) {
                                "Pinned ${definition.name} to Home Screen!"
                            } else {
                                "Launcher could not pin widget automatically. You can add it manually from the home screen."
                            }
                        )
                    }
                    return success
                } catch (e: Exception) {
                    _uiState.update { it.copy(pinStatusMessage = "Pinning error: ${e.localizedMessage}") }
                    return false
                }
            }
        }

        _uiState.update {
            it.copy(pinStatusMessage = "Your launcher does not support direct widget pinning. Please add it from home screen widgets.")
        }
        return false
    }

    fun clearPinStatusMessage() {
        _uiState.update { it.copy(pinStatusMessage = null) }
    }
}
