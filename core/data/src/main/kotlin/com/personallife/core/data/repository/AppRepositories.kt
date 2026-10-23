package com.personallife.core.data.repository

import com.personallife.core.model.AppModule
import com.personallife.core.model.DashboardConfiguration
import com.personallife.core.model.DashboardWidget
import com.personallife.core.model.ModuleId
import com.personallife.core.model.ThemeMode
import com.personallife.core.model.UserPreferences
import com.personallife.core.model.WidgetSize
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val userPreferences: Flow<UserPreferences>
    suspend fun setThemeMode(themeMode: ThemeMode)
    suspend fun setBiometricEnabled(enabled: Boolean)
    suspend fun setNotificationsEnabled(enabled: Boolean)
    suspend fun setFlagSecureEnabled(enabled: Boolean)
}

interface ModuleRepository {
    val modules: Flow<List<AppModule>>
    suspend fun setModuleEnabled(moduleId: ModuleId, isEnabled: Boolean)
}

interface DashboardRepository {
    val dashboardConfiguration: Flow<DashboardConfiguration>
    suspend fun setWidgetVisibility(widgetId: String, isVisible: Boolean)
    suspend fun setWidgetSize(widgetId: String, size: WidgetSize)
    suspend fun updateWidgets(widgets: List<DashboardWidget>)
    suspend fun addWidget(widget: DashboardWidget)
    suspend fun removeWidget(widgetId: String)
    suspend fun resetToDefaults()
    suspend fun logWater(amountMl: Int)
    suspend fun toggleTask(taskName: String)
    suspend fun saveQuickNote(content: String)
    suspend fun reorderWidget(widgetId: String, moveUp: Boolean)
    suspend fun customizeWidget(widgetId: String, accentHex: String)
    suspend fun updateCustomTargets(
        widgetId: String,
        waterTargetMl: Int? = null,
        dailyBudget: Double? = null,
        screenTimeLimitMinutes: Int? = null
    )
}
