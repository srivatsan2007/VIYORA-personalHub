package com.personallife.core.model

/**
 * Identifiers for life management modules.
 */
enum class ModuleId(val key: String) {
    DASHBOARD("dashboard"),
    PERSONAL_VAULT("personal_vault"),
    HEALTH_TRACKER("health_tracker"),
    EDUCATION_HUB("education_hub"),
    EXPENSE_TRACKER("expense_tracker"),
    SCREEN_TIME("screen_time"),
    AI_ASSISTANT("ai_assistant"),
    CRM("crm");

    companion object {
        fun fromKey(key: String): ModuleId = entries.firstOrNull { it.key == key } ?: DASHBOARD
    }
}

/**
 * Metadata & status for an application module.
 */
data class AppModule(
    val id: ModuleId,
    val title: String,
    val description: String,
    val isEnabled: Boolean,
    val isImplemented: Boolean,
    val iconName: String,
    val category: String
) {
    val statusLabel: String
        get() = when {
            !isImplemented -> "Planned"
            isEnabled -> "Active"
            else -> "Disabled"
        }
}
