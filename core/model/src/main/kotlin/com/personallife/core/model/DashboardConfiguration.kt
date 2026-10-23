package com.personallife.core.model

/**
 * Supported display sizes for dashboard widgets.
 */
enum class WidgetSize {
    FULL,
    HALF
}

/**
 * Types of widgets available on the dashboard.
 */
enum class WidgetType {
    QUICK_GLANCE,
    SCREEN_TIME_PREVIEW,
    HEALTH_SUMMARY,
    EXPENSE_METRICS,
    QUICK_NOTE,
    EDUCATION_PLANNER
}

/**
 * Predefined accent themes for widget cards.
 */
enum class WidgetAccent(val hexColor: String, val label: String) {
    EMERALD("#10B981", "Emerald Green"),
    OCEAN("#3B82F6", "Ocean Blue"),
    VIOLET("#8B5CF6", "Royal Violet"),
    AMBER("#F59E0B", "Sunset Amber"),
    ROSE("#EC4899", "Rose Pink")
}

/**
 * An individual interactive customizable dashboard widget.
 */
data class DashboardWidget(
    val id: String,
    val type: WidgetType,
    val title: String,
    val description: String,
    val isVisible: Boolean = true,
    val orderIndex: Int = 0,
    val size: WidgetSize = WidgetSize.FULL,
    val accentColorHex: String = WidgetAccent.EMERALD.hexColor,
    
    // Interactive state: Health & Habits
    val waterIntakeMl: Int = 1250,
    val waterTargetMl: Int = 2500,
    val stepCount: Int = 5420,
    val stepTarget: Int = 10000,
    
    // Interactive state: Expense & Budget
    val spentToday: Double = 42.50,
    val dailyBudget: Double = 100.00,
    val recentCategory: String = "Coffee & Meals",
    
    // Interactive state: Digital Wellbeing
    val screenTimeMinutes: Int = 135,
    val screenTimeLimitMinutes: Int = 240,
    
    // Interactive state: Quick Note Vault
    val quickNoteContent: String = "Remember to review project milestone at 5 PM.",
    val isNoteEncrypted: Boolean = true,
    
    // Interactive state: Daily Focus
    val completedTasks: List<String> = listOf("Morning Walk", "Plan Day"),
    val pendingTasks: List<String> = listOf("Read 20 pages", "Workout session"),
    val dailyQuote: String = "Small daily improvements over time lead to stunning results.",

    // Interactive state: Education Planner
    val studyMinutesDone: Int = 45,
    val studyMinutesGoal: Int = 120,
    val flashcardsDue: Int = 18,
    val nextExamName: String = "Mobile Architecture",
    val nextExamDaysLeft: Int = 6
)

/**
 * Metadata template for widgets that can be browsed and instantiated.
 */
data class WidgetTemplate(
    val type: WidgetType,
    val defaultTitle: String,
    val defaultDescription: String,
    val defaultSize: WidgetSize,
    val defaultAccent: WidgetAccent,
    val category: String,
    val iconName: String
)

/**
 * Central Widget Registry catalog for Phase 2.
 */
object WidgetRegistry {
    val templates: List<WidgetTemplate> = listOf(
        WidgetTemplate(
            type = WidgetType.QUICK_GLANCE,
            defaultTitle = "Daily Focus",
            defaultDescription = "Daily greeting, focus agenda & key metrics",
            defaultSize = WidgetSize.FULL,
            defaultAccent = WidgetAccent.EMERALD,
            category = "Productivity",
            iconName = "check_circle"
        ),
        WidgetTemplate(
            type = WidgetType.SCREEN_TIME_PREVIEW,
            defaultTitle = "Digital Wellbeing",
            defaultDescription = "Today's screen time progress & app limits",
            defaultSize = WidgetSize.HALF,
            defaultAccent = WidgetAccent.OCEAN,
            category = "Wellbeing",
            iconName = "timer"
        ),
        WidgetTemplate(
            type = WidgetType.EXPENSE_METRICS,
            defaultTitle = "Budget & Expenses",
            defaultDescription = "Daily spending breakdown & remaining budget",
            defaultSize = WidgetSize.FULL,
            defaultAccent = WidgetAccent.AMBER,
            category = "Finance",
            iconName = "payments"
        ),
        WidgetTemplate(
            type = WidgetType.HEALTH_SUMMARY,
            defaultTitle = "Health & Habits",
            defaultDescription = "Water intake, steps, and habit streaks",
            defaultSize = WidgetSize.HALF,
            defaultAccent = WidgetAccent.ROSE,
            category = "Wellness",
            iconName = "favorite"
        ),
        WidgetTemplate(
            type = WidgetType.QUICK_NOTE,
            defaultTitle = "Scratchpad Vault",
            defaultDescription = "Encrypted quick notes and thoughts",
            defaultSize = WidgetSize.FULL,
            defaultAccent = WidgetAccent.VIOLET,
            category = "Security",
            iconName = "lock"
        ),
        WidgetTemplate(
            type = WidgetType.EDUCATION_PLANNER,
            defaultTitle = "Education Planner",
            defaultDescription = "Study time tracker, flashcards review & deadlines",
            defaultSize = WidgetSize.FULL,
            defaultAccent = WidgetAccent.OCEAN,
            category = "Growth",
            iconName = "school"
        )
    )

    fun createWidgetFromTemplate(template: WidgetTemplate): DashboardWidget {
        val uniqueId = "w_${template.type.name.lowercase()}_${System.currentTimeMillis() % 100000}"
        return DashboardWidget(
            id = uniqueId,
            type = template.type,
            title = template.defaultTitle,
            description = template.defaultDescription,
            size = template.defaultSize,
            accentColorHex = template.defaultAccent.hexColor
        )
    }
}

/**
 * User's customized dashboard configuration.
 */
data class DashboardConfiguration(
    val widgets: List<DashboardWidget> = defaultWidgets(),
    val lastUpdated: Long = System.currentTimeMillis()
) {
    companion object {
        fun defaultWidgets(): List<DashboardWidget> = listOf(
            DashboardWidget(
                id = "w_quick_glance",
                type = WidgetType.QUICK_GLANCE,
                title = "Daily Focus",
                description = "Daily greeting, focus agenda & key metrics",
                isVisible = true,
                orderIndex = 0,
                size = WidgetSize.FULL,
                accentColorHex = WidgetAccent.EMERALD.hexColor
            ),
            DashboardWidget(
                id = "w_screen_time",
                type = WidgetType.SCREEN_TIME_PREVIEW,
                title = "Digital Wellbeing",
                description = "Today's screen time progress & app limits",
                isVisible = true,
                orderIndex = 1,
                size = WidgetSize.HALF,
                accentColorHex = WidgetAccent.OCEAN.hexColor
            ),
            DashboardWidget(
                id = "w_health_summary",
                type = WidgetType.HEALTH_SUMMARY,
                title = "Health & Habits",
                description = "Water intake, steps, and habit streaks",
                isVisible = true,
                orderIndex = 2,
                size = WidgetSize.HALF,
                accentColorHex = WidgetAccent.ROSE.hexColor
            ),
            DashboardWidget(
                id = "w_expense_metrics",
                type = WidgetType.EXPENSE_METRICS,
                title = "Budget & Expenses",
                description = "Daily spending breakdown & remaining budget",
                isVisible = true,
                orderIndex = 3,
                size = WidgetSize.FULL,
                accentColorHex = WidgetAccent.AMBER.hexColor
            ),
            DashboardWidget(
                id = "w_quick_note",
                type = WidgetType.QUICK_NOTE,
                title = "Scratchpad Vault",
                description = "Encrypted quick notes and thoughts",
                isVisible = true,
                orderIndex = 4,
                size = WidgetSize.FULL,
                accentColorHex = WidgetAccent.VIOLET.hexColor
            )
        )
    }
}
