package com.personallife.core.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.personallife.core.model.DashboardConfiguration
import com.personallife.core.model.DashboardWidget
import com.personallife.core.model.ModuleId
import com.personallife.core.model.ThemeMode
import com.personallife.core.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "personal_life_prefs")

class PreferencesManager(private val context: Context) {

    private object PreferencesKeys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val FLAG_SECURE_ENABLED = booleanPreferencesKey("flag_secure_enabled")
        val DISABLED_MODULES = stringPreferencesKey("disabled_modules")
        val HIDDEN_WIDGETS = stringPreferencesKey("hidden_widgets")
        val WIDGET_ORDER = stringPreferencesKey("widget_order")
        val WATER_INTAKE_ML = intPreferencesKey("water_intake_ml")
        val QUICK_NOTE = stringPreferencesKey("quick_note")
        val COMPLETED_TASKS = stringPreferencesKey("completed_tasks")
        val WIDGET_ACCENTS = stringPreferencesKey("widget_accents")
    }

    val userPreferences: Flow<UserPreferences> = context.dataStore.data.map { preferences ->
        val themeString = preferences[PreferencesKeys.THEME_MODE] ?: ThemeMode.SYSTEM.name
        val themeMode = try {
            ThemeMode.valueOf(themeString)
        } catch (_: Exception) {
            ThemeMode.SYSTEM
        }
        val biometric = preferences[PreferencesKeys.BIOMETRIC_ENABLED] ?: false
        val notifications = preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] ?: true
        val flagSecure = preferences[PreferencesKeys.FLAG_SECURE_ENABLED] ?: true

        UserPreferences(
            themeMode = themeMode,
            isBiometricEnabled = biometric,
            areNotificationsEnabled = notifications,
            isFlagSecureEnabled = flagSecure
        )
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { it[PreferencesKeys.THEME_MODE] = mode.name }
    }

    suspend fun setBiometricEnabled(enabled: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.BIOMETRIC_ENABLED] = enabled }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.NOTIFICATIONS_ENABLED] = enabled }
    }

    suspend fun setFlagSecureEnabled(enabled: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.FLAG_SECURE_ENABLED] = enabled }
    }

    val disabledModuleKeys: Flow<Set<String>> = context.dataStore.data.map { preferences ->
        val raw = preferences[PreferencesKeys.DISABLED_MODULES] ?: ""
        if (raw.isBlank()) {
            // CRM is disabled by default!
            setOf(ModuleId.CRM.key)
        } else {
            raw.split(",").filter { it.isNotBlank() }.toSet()
        }
    }

    suspend fun setModuleEnabled(moduleId: ModuleId, enabled: Boolean) {
        context.dataStore.edit { preferences ->
            val currentRaw = preferences[PreferencesKeys.DISABLED_MODULES]
            val current = if (currentRaw == null) {
                // Initialize default with CRM disabled
                mutableSetOf(ModuleId.CRM.key)
            } else {
                currentRaw.split(",").filter { it.isNotBlank() }.toMutableSet()
            }

            if (enabled) {
                current.remove(moduleId.key)
            } else {
                current.add(moduleId.key)
            }
            preferences[PreferencesKeys.DISABLED_MODULES] = current.joinToString(",")
        }
    }

    val hiddenWidgetIds: Flow<Set<String>> = context.dataStore.data.map { preferences ->
        val raw = preferences[PreferencesKeys.HIDDEN_WIDGETS] ?: ""
        raw.split(",").filter { it.isNotBlank() }.toSet()
    }

    suspend fun setWidgetVisibility(widgetId: String, isVisible: Boolean) {
        context.dataStore.edit { preferences ->
            val raw = preferences[PreferencesKeys.HIDDEN_WIDGETS] ?: ""
            val current = raw.split(",").filter { it.isNotBlank() }.toMutableSet()
            if (isVisible) {
                current.remove(widgetId)
            } else {
                current.add(widgetId)
            }
            preferences[PreferencesKeys.HIDDEN_WIDGETS] = current.joinToString(",")
        }
    }

    val widgetOrder: Flow<List<String>> = context.dataStore.data.map { preferences ->
        val raw = preferences[PreferencesKeys.WIDGET_ORDER] ?: ""
        if (raw.isBlank()) emptyList() else raw.split(",").filter { it.isNotBlank() }
    }

    suspend fun setWidgetOrder(orderedIds: List<String>) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.WIDGET_ORDER] = orderedIds.joinToString(",")
        }
    }

    val waterIntakeMl: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.WATER_INTAKE_ML] ?: 1250
    }

    suspend fun logWater(amountMl: Int) {
        context.dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.WATER_INTAKE_ML] ?: 1250
            val updated = (current + amountMl).coerceIn(0, 5000)
            preferences[PreferencesKeys.WATER_INTAKE_ML] = updated
        }
    }

    val quickNote: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.QUICK_NOTE] ?: "Remember to review project milestone at 5 PM."
    }

    suspend fun saveQuickNote(content: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.QUICK_NOTE] = content
        }
    }

    val completedTasks: Flow<Set<String>> = context.dataStore.data.map { preferences ->
        val raw = preferences[PreferencesKeys.COMPLETED_TASKS] ?: "Morning Walk,Plan Day"
        raw.split(",").filter { it.isNotBlank() }.toSet()
    }

    suspend fun toggleTask(taskName: String) {
        context.dataStore.edit { preferences ->
            val raw = preferences[PreferencesKeys.COMPLETED_TASKS] ?: "Morning Walk,Plan Day"
            val set = raw.split(",").filter { it.isNotBlank() }.toMutableSet()
            if (set.contains(taskName)) {
                set.remove(taskName)
            } else {
                set.add(taskName)
            }
            preferences[PreferencesKeys.COMPLETED_TASKS] = set.joinToString(",")
        }
    }

    val widgetAccents: Flow<Map<String, String>> = context.dataStore.data.map { preferences ->
        val raw = preferences[PreferencesKeys.WIDGET_ACCENTS] ?: ""
        if (raw.isBlank()) emptyMap()
        else {
            raw.split(";").mapNotNull { entry ->
                val parts = entry.split(":")
                if (parts.size == 2) parts[0] to parts[1] else null
            }.toMap()
        }
    }

    suspend fun setWidgetAccent(widgetId: String, accentHex: String) {
        context.dataStore.edit { preferences ->
            val raw = preferences[PreferencesKeys.WIDGET_ACCENTS] ?: ""
            val map = if (raw.isBlank()) mutableMapOf()
            else {
                raw.split(";").mapNotNull { entry ->
                    val parts = entry.split(":")
                    if (parts.size == 2) parts[0] to parts[1] else null
                }.toMap().toMutableMap()
            }
            map[widgetId] = accentHex
            preferences[PreferencesKeys.WIDGET_ACCENTS] = map.entries.joinToString(";") { "${it.key}:${it.value}" }
        }
    }
}
