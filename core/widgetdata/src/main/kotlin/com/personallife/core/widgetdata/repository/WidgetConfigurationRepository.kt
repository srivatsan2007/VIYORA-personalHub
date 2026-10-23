package com.personallife.core.widgetdata.repository

import android.content.Context
import android.content.SharedPreferences
import com.personallife.core.widgetengine.model.WidgetConfiguration
import com.personallife.core.widgetengine.model.WidgetSizePreset
import com.personallife.core.widgetengine.registry.WidgetRegistry
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

interface WidgetConfigurationRepository {
    fun getConfiguration(instanceId: String): WidgetConfiguration
    fun getConfigurationForAppWidget(appWidgetId: Int): WidgetConfiguration
    suspend fun saveConfiguration(config: WidgetConfiguration)
    suspend fun deleteConfiguration(instanceId: String)
    suspend fun deleteConfigurationForAppWidget(appWidgetId: Int)
    val favoriteWidgetIds: Flow<Set<String>>
    suspend fun toggleFavorite(widgetId: String)
    val recentlyCustomized: Flow<List<WidgetConfiguration>>
}

@Singleton
class WidgetConfigurationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : WidgetConfigurationRepository {

    private val prefs: SharedPreferences = context.getSharedPreferences("widget_configurations_v1", Context.MODE_PRIVATE)

    private val _favoritesFlow = MutableStateFlow(loadFavorites())
    override val favoriteWidgetIds: Flow<Set<String>> = _favoritesFlow.asStateFlow()

    private val _recentFlow = MutableStateFlow(loadRecent())
    override val recentlyCustomized: Flow<List<WidgetConfiguration>> = _recentFlow.asStateFlow()

    private fun loadFavorites(): Set<String> {
        return prefs.getStringSet("favorite_widget_ids", emptySet()) ?: emptySet()
    }

    private fun loadRecent(): List<WidgetConfiguration> {
        val keys = prefs.getString("recent_widget_instances", "") ?: ""
        if (keys.isBlank()) return emptyList()
        return keys.split(";;;").mapNotNull { id ->
            if (id.isNotBlank()) getConfiguration(id) else null
        }
    }

    override fun getConfiguration(instanceId: String): WidgetConfiguration {
        val prefix = "cfg_${instanceId}_"
        val widgetId = prefs.getString("${prefix}widgetId", null) ?: "clock_digital_cyber"

        val defaultCfg = WidgetRegistry.createDefaultConfiguration(widgetId, instanceId)

        val sizeName = prefs.getString("${prefix}size", defaultCfg.sizePreset.name)
        val size = try {
            WidgetSizePreset.valueOf(sizeName ?: defaultCfg.sizePreset.name)
        } catch (_: Exception) {
            defaultCfg.sizePreset
        }

        return WidgetConfiguration(
            instanceId = instanceId,
            widgetId = widgetId,
            sizePreset = size,
            themePresetName = prefs.getString("${prefix}theme", defaultCfg.themePresetName) ?: defaultCfg.themePresetName,
            backgroundColorHex = prefs.getString("${prefix}bg", defaultCfg.backgroundColorHex) ?: defaultCfg.backgroundColorHex,
            accentColorHex = prefs.getString("${prefix}accent", defaultCfg.accentColorHex) ?: defaultCfg.accentColorHex,
            textColorHex = prefs.getString("${prefix}text", defaultCfg.textColorHex) ?: defaultCfg.textColorHex,
            secondaryTextColorHex = prefs.getString("${prefix}subtext", defaultCfg.secondaryTextColorHex) ?: defaultCfg.secondaryTextColorHex,
            glassOpacity = prefs.getFloat("${prefix}opacity", defaultCfg.glassOpacity),
            cornerRadiusDp = prefs.getInt("${prefix}radius", defaultCfg.cornerRadiusDp),
            borderWidthDp = prefs.getFloat("${prefix}border", defaultCfg.borderWidthDp),
            showSeconds = prefs.getBoolean("${prefix}seconds", defaultCfg.showSeconds),
            is24Hour = prefs.getBoolean("${prefix}24h", defaultCfg.is24Hour),
            dateFormat = prefs.getString("${prefix}dateformat", defaultCfg.dateFormat) ?: defaultCfg.dateFormat,
            customTitle = prefs.getString("${prefix}title", defaultCfg.customTitle) ?: defaultCfg.customTitle,
            customQuoteText = prefs.getString("${prefix}quoteText", defaultCfg.customQuoteText) ?: defaultCfg.customQuoteText,
            customQuoteAuthor = prefs.getString("${prefix}quoteAuthor", defaultCfg.customQuoteAuthor) ?: defaultCfg.customQuoteAuthor,
            waterTargetMl = prefs.getInt("${prefix}waterTarget", defaultCfg.waterTargetMl),
            dailyBudget = prefs.getFloat("${prefix}budget", defaultCfg.dailyBudget.toFloat()).toDouble(),
            screenTimeLimitMinutes = prefs.getInt("${prefix}screenLimit", defaultCfg.screenTimeLimitMinutes),
            isFavorite = _favoritesFlow.value.contains(widgetId),
            lastModified = prefs.getLong("${prefix}modified", System.currentTimeMillis())
        )
    }

    override fun getConfigurationForAppWidget(appWidgetId: Int): WidgetConfiguration {
        return getConfiguration("appwidget_$appWidgetId")
    }

    override suspend fun saveConfiguration(config: WidgetConfiguration) {
        val prefix = "cfg_${config.instanceId}_"
        prefs.edit()
            .putString("${prefix}widgetId", config.widgetId)
            .putString("${prefix}size", config.sizePreset.name)
            .putString("${prefix}theme", config.themePresetName)
            .putString("${prefix}bg", config.backgroundColorHex)
            .putString("${prefix}accent", config.accentColorHex)
            .putString("${prefix}text", config.textColorHex)
            .putString("${prefix}subtext", config.secondaryTextColorHex)
            .putFloat("${prefix}opacity", config.glassOpacity)
            .putInt("${prefix}radius", config.cornerRadiusDp)
            .putFloat("${prefix}border", config.borderWidthDp)
            .putBoolean("${prefix}seconds", config.showSeconds)
            .putBoolean("${prefix}24h", config.is24Hour)
            .putString("${prefix}dateformat", config.dateFormat)
            .putString("${prefix}title", config.customTitle)
            .putString("${prefix}quoteText", config.customQuoteText)
            .putString("${prefix}quoteAuthor", config.customQuoteAuthor)
            .putInt("${prefix}waterTarget", config.waterTargetMl)
            .putFloat("${prefix}budget", config.dailyBudget.toFloat())
            .putInt("${prefix}screenLimit", config.screenTimeLimitMinutes)
            .putLong("${prefix}modified", System.currentTimeMillis())
            .apply()

        // Update recently customized list
        val currentRecent = loadRecent().toMutableList()
        currentRecent.removeAll { it.instanceId == config.instanceId }
        currentRecent.add(0, config)
        val trimmed = currentRecent.take(10)
        prefs.edit()
            .putString("recent_widget_instances", trimmed.joinToString(";;;") { it.instanceId })
            .apply()
        _recentFlow.value = trimmed
    }

    override suspend fun deleteConfiguration(instanceId: String) {
        val prefix = "cfg_${instanceId}_"
        val keysToRemove = prefs.all.keys.filter { it.startsWith(prefix) }
        val editor = prefs.edit()
        keysToRemove.forEach { editor.remove(it) }
        editor.apply()
    }

    override suspend fun deleteConfigurationForAppWidget(appWidgetId: Int) {
        deleteConfiguration("appwidget_$appWidgetId")
    }

    override suspend fun toggleFavorite(widgetId: String) {
        val current = loadFavorites().toMutableSet()
        if (current.contains(widgetId)) {
            current.remove(widgetId)
        } else {
            current.add(widgetId)
        }
        prefs.edit().putStringSet("favorite_widget_ids", current).apply()
        _favoritesFlow.value = current
    }
}
