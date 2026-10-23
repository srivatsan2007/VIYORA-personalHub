package com.personallife.core.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WidgetDao {

    @Query("SELECT * FROM dashboard_widgets ORDER BY orderIndex ASC")
    fun observeWidgets(): Flow<List<WidgetEntity>>

    @Query("SELECT * FROM dashboard_widgets ORDER BY orderIndex ASC")
    suspend fun getAllWidgets(): List<WidgetEntity>

    @Query("SELECT * FROM dashboard_widgets WHERE id = :id LIMIT 1")
    suspend fun getWidgetById(id: String): WidgetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWidgets(widgets: List<WidgetEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWidget(widget: WidgetEntity)

    @Update
    suspend fun updateWidget(widget: WidgetEntity)

    @Query("UPDATE dashboard_widgets SET isVisible = :isVisible, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateVisibility(id: String, isVisible: Boolean, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE dashboard_widgets SET size = :size, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateSize(id: String, size: String, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE dashboard_widgets SET accentColorHex = :accentColorHex, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateAccent(id: String, accentColorHex: String, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE dashboard_widgets SET orderIndex = :orderIndex, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateOrder(id: String, orderIndex: Int, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE dashboard_widgets SET waterIntakeMl = :water, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateWater(id: String, water: Int, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE dashboard_widgets SET quickNoteContent = :content, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateNote(id: String, content: String, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE dashboard_widgets SET completedTasksRaw = :completed, pendingTasksRaw = :pending, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateTasks(id: String, completed: String, pending: String, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE dashboard_widgets SET waterTargetMl = :waterTarget, dailyBudget = :budget, screenTimeLimitMinutes = :screenLimit, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateTargets(id: String, waterTarget: Int, budget: Double, screenLimit: Int, updatedAt: Long = System.currentTimeMillis())

    @Query("DELETE FROM dashboard_widgets WHERE id = :id")
    suspend fun deleteWidget(id: String)

    @Query("DELETE FROM dashboard_widgets")
    suspend fun clearWidgets()
}
