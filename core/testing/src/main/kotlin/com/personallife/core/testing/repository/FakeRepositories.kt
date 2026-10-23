package com.personallife.core.testing.repository

import com.personallife.core.common.result.Result
import com.personallife.core.data.repository.AuthRepository
import com.personallife.core.data.repository.AuthState
import com.personallife.core.data.repository.DashboardRepository
import com.personallife.core.data.repository.ModuleRepository
import com.personallife.core.data.repository.UserPreferencesRepository
import com.personallife.core.data.repository.UserRepository
import com.personallife.core.model.AppModule
import com.personallife.core.model.DashboardConfiguration
import com.personallife.core.model.DashboardWidget
import com.personallife.core.model.ModuleId
import com.personallife.core.model.ThemeMode
import com.personallife.core.model.UserPreferences
import com.personallife.core.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update

class FakeAuthRepository : AuthRepository {
    private val _currentUser = MutableStateFlow<UserProfile?>(null)
    override val currentUser: Flow<UserProfile?> = _currentUser.asStateFlow()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    override val authState: Flow<AuthState> = _authState.asStateFlow()

    var shouldFail: Boolean = false
    var failureMessage: String = "Test failure"

    override fun isFirebaseConfigured(): Boolean = true

    override suspend fun login(email: String, password: String): Flow<Result<UserProfile>> = flow {
        emit(Result.Loading)
        if (shouldFail) {
            emit(Result.Error(RuntimeException(failureMessage)))
        } else {
            val user = UserProfile(uid = "test-uid", email = email, displayName = "Test User")
            _currentUser.value = user
            _authState.value = AuthState.Authenticated(user)
            emit(Result.Success(user))
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        displayName: String
    ): Flow<Result<UserProfile>> = flow {
        emit(Result.Loading)
        if (shouldFail) {
            emit(Result.Error(RuntimeException(failureMessage)))
        } else {
            val user = UserProfile(uid = "test-uid", email = email, displayName = displayName)
            _currentUser.value = user
            _authState.value = AuthState.Authenticated(user)
            emit(Result.Success(user))
        }
    }

    override suspend fun logout(): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        _currentUser.value = null
        _authState.value = AuthState.Unauthenticated
        emit(Result.Success(Unit))
    }
}

class FakeUserRepository : UserRepository {
    var storedProfile = UserProfile(uid = "test-uid", displayName = "Test User", email = "test@example.com")
    var shouldFail: Boolean = false

    override fun getUserProfile(uid: String): Flow<Result<UserProfile>> = flow {
        emit(Result.Loading)
        if (shouldFail) {
            emit(Result.Error(RuntimeException("Failed to load profile")))
        } else {
            emit(Result.Success(storedProfile))
        }
    }

    override suspend fun updateUserProfile(profile: UserProfile): Result<Unit> {
        return if (shouldFail) {
            Result.Error(RuntimeException("Failed to update profile"))
        } else {
            storedProfile = profile
            Result.Success(Unit)
        }
    }
}

class FakeUserPreferencesRepository : UserPreferencesRepository {
    private val _preferences = MutableStateFlow(UserPreferences())
    override val userPreferences: Flow<UserPreferences> = _preferences.asStateFlow()

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        _preferences.update { it.copy(themeMode = themeMode) }
    }

    override suspend fun setBiometricEnabled(enabled: Boolean) {
        _preferences.update { it.copy(isBiometricEnabled = enabled) }
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        _preferences.update { it.copy(areNotificationsEnabled = enabled) }
    }

    override suspend fun setFlagSecureEnabled(enabled: Boolean) {
        _preferences.update { it.copy(isFlagSecureEnabled = enabled) }
    }
}

class FakeModuleRepository : ModuleRepository {
    private val _modules = MutableStateFlow(
        listOf(
            AppModule(ModuleId.DASHBOARD, "Dashboard", "Central overview", isEnabled = true, isImplemented = true, iconName = "dashboard", category = "Core"),
            AppModule(ModuleId.PERSONAL_VAULT, "Personal Vault", "Encrypted storage", isEnabled = true, isImplemented = false, iconName = "lock", category = "Security"),
            AppModule(ModuleId.CRM, "Personal CRM", "Relationship manager", isEnabled = false, isImplemented = false, iconName = "contacts", category = "Productivity")
        )
    )
    override val modules: Flow<List<AppModule>> = _modules.asStateFlow()

    override suspend fun setModuleEnabled(moduleId: ModuleId, isEnabled: Boolean) {
        _modules.update { list ->
            list.map { if (it.id == moduleId) it.copy(isEnabled = isEnabled) else it }
        }
    }
}

class FakeDashboardRepository : DashboardRepository {
    private val _config = MutableStateFlow(DashboardConfiguration())
    override val dashboardConfiguration: Flow<DashboardConfiguration> = _config.asStateFlow()

    override suspend fun setWidgetVisibility(widgetId: String, isVisible: Boolean) {
        _config.update { cfg ->
            val updated = cfg.widgets.map { if (it.id == widgetId) it.copy(isVisible = isVisible) else it }
            cfg.copy(widgets = updated)
        }
    }

    override suspend fun setWidgetSize(widgetId: String, size: com.personallife.core.model.WidgetSize) {
        _config.update { cfg ->
            val updated = cfg.widgets.map { if (it.id == widgetId) it.copy(size = size) else it }
            cfg.copy(widgets = updated)
        }
    }

    override suspend fun updateWidgets(widgets: List<DashboardWidget>) {
        _config.update { it.copy(widgets = widgets) }
    }

    override suspend fun addWidget(widget: DashboardWidget) {
        _config.update { cfg ->
            val updated = cfg.widgets + widget.copy(orderIndex = cfg.widgets.size)
            cfg.copy(widgets = updated)
        }
    }

    override suspend fun removeWidget(widgetId: String) {
        _config.update { cfg ->
            val updated = cfg.widgets.filterNot { it.id == widgetId }
            cfg.copy(widgets = updated)
        }
    }

    override suspend fun resetToDefaults() {
        _config.value = DashboardConfiguration(widgets = DashboardConfiguration.defaultWidgets())
    }

    override suspend fun logWater(amountMl: Int) {
        _config.update { cfg ->
            val updated = cfg.widgets.map {
                if (it.type == com.personallife.core.model.WidgetType.HEALTH_SUMMARY) {
                    it.copy(waterIntakeMl = (it.waterIntakeMl + amountMl).coerceIn(0, 5000))
                } else it
            }
            cfg.copy(widgets = updated)
        }
    }

    override suspend fun toggleTask(taskName: String) {
        _config.update { cfg ->
            val updated = cfg.widgets.map {
                if (it.type == com.personallife.core.model.WidgetType.QUICK_GLANCE) {
                    val comp = it.completedTasks.toMutableList()
                    val pend = it.pendingTasks.toMutableList()
                    if (comp.contains(taskName)) {
                        comp.remove(taskName)
                        pend.add(taskName)
                    } else {
                        pend.remove(taskName)
                        comp.add(taskName)
                    }
                    it.copy(completedTasks = comp, pendingTasks = pend)
                } else it
            }
            cfg.copy(widgets = updated)
        }
    }

    override suspend fun saveQuickNote(content: String) {
        _config.update { cfg ->
            val updated = cfg.widgets.map {
                if (it.type == com.personallife.core.model.WidgetType.QUICK_NOTE) {
                    it.copy(quickNoteContent = content)
                } else it
            }
            cfg.copy(widgets = updated)
        }
    }

    override suspend fun reorderWidget(widgetId: String, moveUp: Boolean) {
        _config.update { cfg ->
            val list = cfg.widgets.toMutableList()
            val index = list.indexOfFirst { it.id == widgetId }
            if (index != -1) {
                val target = if (moveUp) index - 1 else index + 1
                if (target in 0 until list.size) {
                    val temp = list[index]
                    list[index] = list[target]
                    list[target] = temp
                }
            }
            cfg.copy(widgets = list)
        }
    }

    override suspend fun customizeWidget(widgetId: String, accentHex: String) {
        _config.update { cfg ->
            val updated = cfg.widgets.map {
                if (it.id == widgetId) it.copy(accentColorHex = accentHex) else it
            }
            cfg.copy(widgets = updated)
        }
    }

    override suspend fun updateCustomTargets(
        widgetId: String,
        waterTargetMl: Int?,
        dailyBudget: Double?,
        screenTimeLimitMinutes: Int?
    ) {
        _config.update { cfg ->
            val updated = cfg.widgets.map {
                if (it.id == widgetId) {
                    it.copy(
                        waterTargetMl = waterTargetMl ?: it.waterTargetMl,
                        dailyBudget = dailyBudget ?: it.dailyBudget,
                        screenTimeLimitMinutes = screenTimeLimitMinutes ?: it.screenTimeLimitMinutes
                    )
                } else it
            }
            cfg.copy(widgets = updated)
        }
    }
}
