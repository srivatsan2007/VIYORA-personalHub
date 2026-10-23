package com.personallife.core.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.personallife.core.model.DashboardWidget
import com.personallife.core.model.WidgetSize
import com.personallife.core.model.WidgetType

/**
 * Room entity representing an encrypted dashboard widget record.
 */
@Entity(tableName = "dashboard_widgets")
data class WidgetEntity(
    @PrimaryKey
    val id: String,
    val type: String,
    val title: String,
    val description: String,
    val isVisible: Boolean,
    val orderIndex: Int,
    val size: String,
    val accentColorHex: String,
    val waterIntakeMl: Int,
    val waterTargetMl: Int,
    val stepCount: Int,
    val stepTarget: Int,
    val spentToday: Double,
    val dailyBudget: Double,
    val recentCategory: String,
    val screenTimeMinutes: Int,
    val screenTimeLimitMinutes: Int,
    val quickNoteContent: String,
    val isNoteEncrypted: Boolean,
    val completedTasksRaw: String, // Comma-delimited list
    val pendingTasksRaw: String,   // Comma-delimited list
    val dailyQuote: String,
    val studyMinutesDone: Int = 45,
    val studyMinutesGoal: Int = 120,
    val flashcardsDue: Int = 18,
    val nextExamName: String = "Mobile Architecture",
    val nextExamDaysLeft: Int = 6,
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): DashboardWidget {
        val widgetType = try {
            WidgetType.valueOf(type)
        } catch (_: Exception) {
            WidgetType.QUICK_GLANCE
        }

        val widgetSize = try {
            WidgetSize.valueOf(size)
        } catch (_: Exception) {
            WidgetSize.FULL
        }

        val completed = if (completedTasksRaw.isBlank()) emptyList() else completedTasksRaw.split("|||")
        val pending = if (pendingTasksRaw.isBlank()) emptyList() else pendingTasksRaw.split("|||")

        return DashboardWidget(
            id = id,
            type = widgetType,
            title = title,
            description = description,
            isVisible = isVisible,
            orderIndex = orderIndex,
            size = widgetSize,
            accentColorHex = accentColorHex,
            waterIntakeMl = waterIntakeMl,
            waterTargetMl = waterTargetMl,
            stepCount = stepCount,
            stepTarget = stepTarget,
            spentToday = spentToday,
            dailyBudget = dailyBudget,
            recentCategory = recentCategory,
            screenTimeMinutes = screenTimeMinutes,
            screenTimeLimitMinutes = screenTimeLimitMinutes,
            quickNoteContent = quickNoteContent,
            isNoteEncrypted = isNoteEncrypted,
            completedTasks = completed,
            pendingTasks = pending,
            dailyQuote = dailyQuote,
            studyMinutesDone = studyMinutesDone,
            studyMinutesGoal = studyMinutesGoal,
            flashcardsDue = flashcardsDue,
            nextExamName = nextExamName,
            nextExamDaysLeft = nextExamDaysLeft
        )
    }

    companion object {
        fun fromDomain(widget: DashboardWidget): WidgetEntity {
            return WidgetEntity(
                id = widget.id,
                type = widget.type.name,
                title = widget.title,
                description = widget.description,
                isVisible = widget.isVisible,
                orderIndex = widget.orderIndex,
                size = widget.size.name,
                accentColorHex = widget.accentColorHex,
                waterIntakeMl = widget.waterIntakeMl,
                waterTargetMl = widget.waterTargetMl,
                stepCount = widget.stepCount,
                stepTarget = widget.stepTarget,
                spentToday = widget.spentToday,
                dailyBudget = widget.dailyBudget,
                recentCategory = widget.recentCategory,
                screenTimeMinutes = widget.screenTimeMinutes,
                screenTimeLimitMinutes = widget.screenTimeLimitMinutes,
                quickNoteContent = widget.quickNoteContent,
                isNoteEncrypted = widget.isNoteEncrypted,
                completedTasksRaw = widget.completedTasks.joinToString("|||"),
                pendingTasksRaw = widget.pendingTasks.joinToString("|||"),
                dailyQuote = widget.dailyQuote,
                studyMinutesDone = widget.studyMinutesDone,
                studyMinutesGoal = widget.studyMinutesGoal,
                flashcardsDue = widget.flashcardsDue,
                nextExamName = widget.nextExamName,
                nextExamDaysLeft = widget.nextExamDaysLeft,
                updatedAt = System.currentTimeMillis()
            )
        }
    }
}
