package com.personallife.core.data.repository

import com.personallife.core.data.datastore.PreferencesManager
import com.personallife.core.model.AppModule
import com.personallife.core.model.DashboardConfiguration
import com.personallife.core.model.DashboardWidget
import com.personallife.core.model.ModuleId
import com.personallife.core.model.ThemeMode
import com.personallife.core.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepositoryImpl @Inject constructor(
    private val preferencesManager: PreferencesManager
) : UserPreferencesRepository {

    override val userPreferences: Flow<UserPreferences> = preferencesManager.userPreferences

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        preferencesManager.setThemeMode(themeMode)
    }

    override suspend fun setBiometricEnabled(enabled: Boolean) {
        preferencesManager.setBiometricEnabled(enabled)
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        preferencesManager.setNotificationsEnabled(enabled)
    }

    override suspend fun setFlagSecureEnabled(enabled: Boolean) {
        preferencesManager.setFlagSecureEnabled(enabled)
    }
}

@Singleton
class ModuleRepositoryImpl @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ModuleRepository {

    private val baseModules = listOf(
        AppModule(
            id = ModuleId.DASHBOARD,
            title = "Dashboard",
            description = "Centralized overview of your daily focus and widgets",
            isEnabled = true,
            isImplemented = true,
            iconName = "dashboard",
            category = "Core"
        ),
        AppModule(
            id = ModuleId.PERSONAL_VAULT,
            title = "Personal Vault",
            description = "Zero-leak encrypted storage for documents and sensitive records",
            isEnabled = true,
            isImplemented = false,
            iconName = "lock",
            category = "Security"
        ),
        AppModule(
            id = ModuleId.HEALTH_TRACKER,
            title = "Health Tracker",
            description = "Daily habits, water intake, sleep, and fitness trends",
            isEnabled = true,
            isImplemented = false,
            iconName = "favorite",
            category = "Wellness"
        ),
        AppModule(
            id = ModuleId.EDUCATION_HUB,
            title = "Education Hub",
            description = "Study planner, course tracking, and spaced-repetition notes",
            isEnabled = true,
            isImplemented = false,
            iconName = "school",
            category = "Growth"
        ),
        AppModule(
            id = ModuleId.EXPENSE_TRACKER,
            title = "Expense Tracker",
            description = "Encrypted budgets, category limits, and spending analytics",
            isEnabled = true,
            isImplemented = false,
            iconName = "payments",
            category = "Finance"
        ),
        AppModule(
            id = ModuleId.SCREEN_TIME,
            title = "Screen Time Controller",
            description = "Digital wellbeing, app usage stats, and focus limiters",
            isEnabled = true,
            isImplemented = false,
            iconName = "timer",
            category = "Wellbeing"
        ),
        AppModule(
            id = ModuleId.AI_ASSISTANT,
            title = "AI Assistant",
            description = "On-device private assistant for personal summaries and insights",
            isEnabled = true,
            isImplemented = false,
            iconName = "psychology",
            category = "Intelligence"
        ),
        AppModule(
            id = ModuleId.CRM,
            title = "Personal CRM",
            description = "Manage relationships, contacts, and interaction timelines",
            isEnabled = false, // Disabled by default
            isImplemented = false,
            iconName = "contacts",
            category = "Productivity"
        )
    )

    override val modules: Flow<List<AppModule>> = preferencesManager.disabledModuleKeys.map { disabledKeys ->
        baseModules.map { module ->
            val isExplicitlyDisabled = disabledKeys.contains(module.id.key)
            module.copy(isEnabled = !isExplicitlyDisabled)
        }
    }

    override suspend fun setModuleEnabled(moduleId: ModuleId, isEnabled: Boolean) {
        preferencesManager.setModuleEnabled(moduleId, isEnabled)
    }
}

@Singleton
class DashboardRepositoryImpl @Inject constructor(
    private val widgetDao: com.personallife.core.data.database.WidgetDao
) : DashboardRepository {

    private val repositoryScope = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO)

    init {
        // Initialize default widgets if database is empty
        repositoryScope.launch {
            val existing = widgetDao.getAllWidgets()
            if (existing.isEmpty()) {
                val defaults = DashboardConfiguration.defaultWidgets().map {
                    com.personallife.core.data.database.WidgetEntity.fromDomain(it)
                }
                widgetDao.insertWidgets(defaults)
            }
        }
    }

    override val dashboardConfiguration: Flow<DashboardConfiguration> =
        widgetDao.observeWidgets().map { entities ->
            if (entities.isEmpty()) {
                DashboardConfiguration(widgets = DashboardConfiguration.defaultWidgets())
            } else {
                DashboardConfiguration(widgets = entities.map { it.toDomain() })
            }
        }

    override suspend fun setWidgetVisibility(widgetId: String, isVisible: Boolean) {
        widgetDao.updateVisibility(widgetId, isVisible)
    }

    override suspend fun setWidgetSize(widgetId: String, size: com.personallife.core.model.WidgetSize) {
        widgetDao.updateSize(widgetId, size.name)
    }

    override suspend fun updateWidgets(widgets: List<DashboardWidget>) {
        val entities = widgets.mapIndexed { index, w ->
            com.personallife.core.data.database.WidgetEntity.fromDomain(w.copy(orderIndex = index))
        }
        widgetDao.insertWidgets(entities)
    }

    override suspend fun addWidget(widget: DashboardWidget) {
        val current = widgetDao.getAllWidgets()
        val entity = com.personallife.core.data.database.WidgetEntity.fromDomain(
            widget.copy(orderIndex = current.size)
        )
        widgetDao.insertWidget(entity)
    }

    override suspend fun removeWidget(widgetId: String) {
        widgetDao.deleteWidget(widgetId)
    }

    override suspend fun resetToDefaults() {
        widgetDao.clearWidgets()
        val defaults = DashboardConfiguration.defaultWidgets().map {
            com.personallife.core.data.database.WidgetEntity.fromDomain(it)
        }
        widgetDao.insertWidgets(defaults)
    }

    override suspend fun logWater(amountMl: Int) {
        val widgets = widgetDao.getAllWidgets()
        val healthWidget = widgets.firstOrNull { it.type == com.personallife.core.model.WidgetType.HEALTH_SUMMARY.name }
        if (healthWidget != null) {
            val newAmount = (healthWidget.waterIntakeMl + amountMl).coerceIn(0, 10000)
            widgetDao.updateWater(healthWidget.id, newAmount)
        }
    }

    override suspend fun toggleTask(taskName: String) {
        val widgets = widgetDao.getAllWidgets()
        val glanceWidget = widgets.firstOrNull { it.type == com.personallife.core.model.WidgetType.QUICK_GLANCE.name }
        if (glanceWidget != null) {
            val domain = glanceWidget.toDomain()
            val completed = domain.completedTasks.toMutableList()
            val pending = domain.pendingTasks.toMutableList()

            if (completed.contains(taskName)) {
                completed.remove(taskName)
                pending.add(taskName)
            } else {
                pending.remove(taskName)
                completed.add(taskName)
            }

            widgetDao.updateTasks(
                id = glanceWidget.id,
                completed = completed.joinToString("|||"),
                pending = pending.joinToString("|||")
            )
        }
    }

    override suspend fun saveQuickNote(content: String) {
        val widgets = widgetDao.getAllWidgets()
        val noteWidget = widgets.firstOrNull { it.type == com.personallife.core.model.WidgetType.QUICK_NOTE.name }
        if (noteWidget != null) {
            widgetDao.updateNote(noteWidget.id, content)
        }
    }

    override suspend fun reorderWidget(widgetId: String, moveUp: Boolean) {
        val currentEntities = widgetDao.getAllWidgets().toMutableList()
        val index = currentEntities.indexOfFirst { it.id == widgetId }
        if (index != -1) {
            val targetIndex = if (moveUp) index - 1 else index + 1
            if (targetIndex in 0 until currentEntities.size) {
                val current = currentEntities[index]
                val target = currentEntities[targetIndex]

                widgetDao.updateOrder(current.id, targetIndex)
                widgetDao.updateOrder(target.id, index)
            }
        }
    }

    override suspend fun customizeWidget(widgetId: String, accentHex: String) {
        widgetDao.updateAccent(widgetId, accentHex)
    }

    override suspend fun updateCustomTargets(
        widgetId: String,
        waterTargetMl: Int?,
        dailyBudget: Double?,
        screenTimeLimitMinutes: Int?
    ) {
        val widget = widgetDao.getWidgetById(widgetId) ?: return
        val updatedWater = waterTargetMl ?: widget.waterTargetMl
        val updatedBudget = dailyBudget ?: widget.dailyBudget
        val updatedScreen = screenTimeLimitMinutes ?: widget.screenTimeLimitMinutes
        widgetDao.updateTargets(widgetId, updatedWater, updatedBudget, updatedScreen)
    }
}
