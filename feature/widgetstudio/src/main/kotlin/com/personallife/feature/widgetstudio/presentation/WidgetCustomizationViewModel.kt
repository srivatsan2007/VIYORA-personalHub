package com.personallife.feature.widgetstudio.presentation

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.os.Build
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personallife.core.widgetdata.repository.WidgetConfigurationRepository
import com.personallife.core.widgetengine.model.WidgetConfiguration
import com.personallife.core.widgetengine.model.WidgetDefinition
import com.personallife.core.widgetengine.model.WidgetPermissionRequirement
import com.personallife.core.widgetengine.model.WidgetSizePreset
import com.personallife.core.widgetengine.model.WidgetThemePreset
import com.personallife.core.widgetengine.registry.WidgetRegistry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WidgetCustomizationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val widgetRepository: WidgetConfigurationRepository
) : ViewModel() {

    private val widgetId: String = checkNotNull(savedStateHandle["widgetId"])

    private val _uiState = MutableStateFlow(WidgetCustomizationUiState(isLoading = true))
    val uiState: StateFlow<WidgetCustomizationUiState> = _uiState.asStateFlow()

    init {
        loadWidget()
    }

    private fun loadWidget() {
        val definition = WidgetRegistry.findById(widgetId)
        if (definition != null) {
            val existingConfig = widgetRepository.getConfiguration(widgetId)
            _uiState.update {
                it.copy(
                    definition = definition,
                    configuration = existingConfig,
                    isLoading = false
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    statusMessage = "Widget definition '$widgetId' not found."
                )
            }
        }
    }

    fun updateThemePreset(preset: WidgetThemePreset) {
        _uiState.update { state ->
            val current = state.configuration ?: return@update state
            state.copy(
                configuration = current.copy(
                    themePresetName = preset.name,
                    backgroundColorHex = preset.backgroundColorHex,
                    accentColorHex = preset.accentColorHex,
                    textColorHex = preset.textColorHex,
                    secondaryTextColorHex = preset.secondaryTextColorHex,
                    glassOpacity = preset.glassOpacity,
                    cornerRadiusDp = preset.cornerRadiusDp,
                    borderWidthDp = preset.borderWidthDp
                ),
                isSaved = false
            )
        }
    }

    fun updateSizePreset(preset: WidgetSizePreset) {
        _uiState.update { state ->
            val current = state.configuration ?: return@update state
            state.copy(
                configuration = current.copy(sizePreset = preset),
                isSaved = false
            )
        }
    }

    fun updateGlassOpacity(opacity: Float) {
        _uiState.update { state ->
            val current = state.configuration ?: return@update state
            state.copy(
                configuration = current.copy(glassOpacity = opacity.coerceIn(0.2f, 1.0f)),
                isSaved = false
            )
        }
    }

    fun updateCornerRadius(radiusDp: Int) {
        _uiState.update { state ->
            val current = state.configuration ?: return@update state
            state.copy(
                configuration = current.copy(cornerRadiusDp = radiusDp.coerceIn(8, 40)),
                isSaved = false
            )
        }
    }

    fun updateBorderWidth(widthDp: Float) {
        _uiState.update { state ->
            val current = state.configuration ?: return@update state
            state.copy(
                configuration = current.copy(borderWidthDp = widthDp.coerceIn(0f, 4f)),
                isSaved = false
            )
        }
    }

    fun updateAccentColorHex(hex: String) {
        _uiState.update { state ->
            val current = state.configuration ?: return@update state
            state.copy(
                configuration = current.copy(accentColorHex = hex),
                isSaved = false
            )
        }
    }

    fun updateTimeFormat(is24Hour: Boolean, showSeconds: Boolean) {
        _uiState.update { state ->
            val current = state.configuration ?: return@update state
            state.copy(
                configuration = current.copy(is24Hour = is24Hour, showSeconds = showSeconds),
                isSaved = false
            )
        }
    }

    fun updateCustomQuote(text: String, author: String) {
        _uiState.update { state ->
            val current = state.configuration ?: return@update state
            state.copy(
                configuration = current.copy(customQuoteText = text, customQuoteAuthor = author),
                isSaved = false
            )
        }
    }

    fun updateTargetValues(waterMl: Int? = null, budget: Double? = null, screenMinutes: Int? = null) {
        _uiState.update { state ->
            val current = state.configuration ?: return@update state
            state.copy(
                configuration = current.copy(
                    waterTargetMl = waterMl ?: current.waterTargetMl,
                    dailyBudget = budget ?: current.dailyBudget,
                    screenTimeLimitMinutes = screenMinutes ?: current.screenTimeLimitMinutes
                ),
                isSaved = false
            )
        }
    }

    fun saveConfiguration() {
        val config = _uiState.value.configuration ?: return
        viewModelScope.launch {
            widgetRepository.saveConfiguration(config)
            _uiState.update {
                it.copy(
                    isSaved = true,
                    statusMessage = "Configuration saved successfully!"
                )
            }
        }
    }

    fun resetToDefaults() {
        val definition = _uiState.value.definition ?: return
        val freshConfig = WidgetRegistry.createDefaultConfiguration(definition.id, definition.id)
        _uiState.update {
            it.copy(
                configuration = freshConfig,
                isSaved = false,
                statusMessage = "Reset to default settings."
            )
        }
    }

    fun pinToHomeScreen(context: Context) {
        val definition = _uiState.value.definition ?: return
        val config = _uiState.value.configuration ?: return

        // Auto-save before pinning
        saveConfiguration()

        val providerClass = definition.providerClassName
        if (providerClass.isNullOrBlank()) {
            _uiState.update {
                it.copy(
                    showManualAddDialog = true,
                    statusMessage = "Home screen provider implementation in progress."
                )
            }
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            if (appWidgetManager.isRequestPinAppWidgetSupported) {
                try {
                    val componentName = ComponentName(context, providerClass)
                    val success = appWidgetManager.requestPinAppWidget(componentName, null, null)
                    if (success) {
                        _uiState.update {
                            it.copy(statusMessage = "Widget pinned to home screen!")
                        }
                    } else {
                        _uiState.update {
                            it.copy(showManualAddDialog = true)
                        }
                    }
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(
                            showManualAddDialog = true,
                            statusMessage = "Pinning error: ${e.localizedMessage}"
                        )
                    }
                }
            } else {
                _uiState.update { it.copy(showManualAddDialog = true) }
            }
        } else {
            _uiState.update { it.copy(showManualAddDialog = true) }
        }
    }

    fun dismissManualAddDialog() {
        _uiState.update { it.copy(showManualAddDialog = false) }
    }

    fun checkAndShowPermissionRequirement() {
        val definition = _uiState.value.definition ?: return
        if (definition.requiredPermission != WidgetPermissionRequirement.NONE) {
            _uiState.update { it.copy(showPermissionDialog = true) }
        }
    }

    fun dismissPermissionDialog() {
        _uiState.update { it.copy(showPermissionDialog = false) }
    }

    fun clearStatusMessage() {
        _uiState.update { it.copy(statusMessage = null) }
    }
}
