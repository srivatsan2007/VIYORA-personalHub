package com.personallife.feature.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personallife.core.data.repository.AuthRepository
import com.personallife.core.data.repository.DashboardRepository
import com.personallife.core.data.repository.ModuleRepository
import com.personallife.core.model.AppModule
import com.personallife.core.model.DashboardWidget
import com.personallife.core.model.ModuleId
import com.personallife.core.model.WidgetRegistry
import com.personallife.core.model.WidgetSize
import com.personallife.core.model.WidgetTemplate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val moduleRepository: ModuleRepository,
    private val dashboardRepository: DashboardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState(isLoading = true))
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        combine(
            authRepository.currentUser,
            moduleRepository.modules,
            dashboardRepository.dashboardConfiguration
        ) { user, modules, config ->
            val enabled = modules.filter { it.isEnabled && it.id != ModuleId.DASHBOARD }
            _uiState.update {
                it.copy(
                    userProfile = user,
                    enabledModules = enabled,
                    dashboardConfig = config,
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setWidgetVisibility(widgetId: String, isVisible: Boolean) {
        viewModelScope.launch {
            dashboardRepository.setWidgetVisibility(widgetId, isVisible)
        }
    }

    fun setWidgetSize(widgetId: String, size: WidgetSize) {
        viewModelScope.launch {
            dashboardRepository.setWidgetSize(widgetId, size)
        }
    }

    fun openWidgetManager(initialTab: Int = 0) {
        _uiState.update { it.copy(isWidgetManagerOpen = true, widgetManagerTab = initialTab) }
    }

    fun closeWidgetManager() {
        _uiState.update { it.copy(isWidgetManagerOpen = false) }
    }

    fun setWidgetManagerTab(tab: Int) {
        _uiState.update { it.copy(widgetManagerTab = tab) }
    }

    fun addWidgetFromTemplate(template: WidgetTemplate) {
        viewModelScope.launch {
            val newWidget = WidgetRegistry.createWidgetFromTemplate(template)
            dashboardRepository.addWidget(newWidget)
            _uiState.update { it.copy(widgetManagerTab = 0) }
        }
    }

    fun removeWidget(widgetId: String) {
        viewModelScope.launch {
            dashboardRepository.removeWidget(widgetId)
        }
    }

    fun resetWidgetsToDefaults() {
        viewModelScope.launch {
            dashboardRepository.resetToDefaults()
        }
    }

    fun selectPlaceholderModule(module: AppModule) {
        _uiState.update { it.copy(selectedPlaceholderModule = module) }
    }

    fun dismissPlaceholderDialog() {
        _uiState.update { it.copy(selectedPlaceholderModule = null) }
    }

    // Phase 2: Interactive In-App Widget Actions
    fun logWater(amountMl: Int) {
        viewModelScope.launch {
            dashboardRepository.logWater(amountMl)
        }
    }

    fun toggleTask(taskName: String) {
        viewModelScope.launch {
            dashboardRepository.toggleTask(taskName)
        }
    }

    fun saveQuickNote(content: String) {
        viewModelScope.launch {
            dashboardRepository.saveQuickNote(content)
            _uiState.update { it.copy(isQuickNoteEditorOpen = false) }
        }
    }

    fun openQuickNoteEditor(initialText: String) {
        _uiState.update {
            it.copy(
                isQuickNoteEditorOpen = true,
                editingQuickNoteText = initialText
            )
        }
    }

    fun closeQuickNoteEditor() {
        _uiState.update { it.copy(isQuickNoteEditorOpen = false) }
    }

    fun onQuickNoteTextChanged(text: String) {
        _uiState.update { it.copy(editingQuickNoteText = text) }
    }

    fun reorderWidget(widgetId: String, moveUp: Boolean) {
        viewModelScope.launch {
            dashboardRepository.reorderWidget(widgetId, moveUp)
        }
    }

    fun openWidgetCustomizer(widget: DashboardWidget) {
        _uiState.update {
            it.copy(
                selectedWidgetForCustomization = widget,
                customizingAccentHex = widget.accentColorHex,
                customizingSize = widget.size,
                customizingWaterTarget = widget.waterTargetMl.toString(),
                customizingDailyBudget = String.format(java.util.Locale.US, "%.0f", widget.dailyBudget),
                customizingScreenLimit = widget.screenTimeLimitMinutes.toString()
            )
        }
    }

    fun closeWidgetCustomizer() {
        _uiState.update { it.copy(selectedWidgetForCustomization = null) }
    }

    fun onCustomizingAccentChanged(hex: String) {
        _uiState.update { it.copy(customizingAccentHex = hex) }
    }

    fun onCustomizingSizeChanged(size: WidgetSize) {
        _uiState.update { it.copy(customizingSize = size) }
    }

    fun onCustomizingWaterTargetChanged(target: String) {
        _uiState.update { it.copy(customizingWaterTarget = target) }
    }

    fun onCustomizingBudgetChanged(budget: String) {
        _uiState.update { it.copy(customizingDailyBudget = budget) }
    }

    fun onCustomizingScreenLimitChanged(limit: String) {
        _uiState.update { it.copy(customizingScreenLimit = limit) }
    }

    fun saveWidgetCustomization() {
        val currentWidget = _uiState.value.selectedWidgetForCustomization ?: return
        val accent = _uiState.value.customizingAccentHex.ifBlank { currentWidget.accentColorHex }
        val size = _uiState.value.customizingSize
        val waterTarget = _uiState.value.customizingWaterTarget.toIntOrNull()
        val budget = _uiState.value.customizingDailyBudget.toDoubleOrNull()
        val screenLimit = _uiState.value.customizingScreenLimit.toIntOrNull()

        viewModelScope.launch {
            dashboardRepository.customizeWidget(currentWidget.id, accent)
            dashboardRepository.setWidgetSize(currentWidget.id, size)
            dashboardRepository.updateCustomTargets(
                widgetId = currentWidget.id,
                waterTargetMl = waterTarget,
                dailyBudget = budget,
                screenTimeLimitMinutes = screenLimit
            )
            _uiState.update { it.copy(selectedWidgetForCustomization = null) }
        }
    }

    fun customizeWidgetAccent(widgetId: String, accentHex: String) {
        viewModelScope.launch {
            dashboardRepository.customizeWidget(widgetId, accentHex)
            _uiState.update { it.copy(selectedWidgetForCustomization = null) }
        }
    }
}
