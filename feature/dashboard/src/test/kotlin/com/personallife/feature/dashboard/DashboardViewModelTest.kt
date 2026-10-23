package com.personallife.feature.dashboard

import com.personallife.core.model.ModuleId
import com.personallife.core.model.WidgetRegistry
import com.personallife.core.model.WidgetSize
import com.personallife.core.model.WidgetType
import com.personallife.core.testing.repository.FakeAuthRepository
import com.personallife.core.testing.repository.FakeDashboardRepository
import com.personallife.core.testing.repository.FakeModuleRepository
import com.personallife.core.testing.util.MainDispatcherRule
import com.personallife.feature.dashboard.presentation.DashboardViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeAuthRepository: FakeAuthRepository
    private lateinit var fakeModuleRepository: FakeModuleRepository
    private lateinit var fakeDashboardRepository: FakeDashboardRepository
    private lateinit var viewModel: DashboardViewModel

    @Before
    fun setUp() {
        fakeAuthRepository = FakeAuthRepository()
        fakeModuleRepository = FakeModuleRepository()
        fakeDashboardRepository = FakeDashboardRepository()
        viewModel = DashboardViewModel(
            authRepository = fakeAuthRepository,
            moduleRepository = fakeModuleRepository,
            dashboardRepository = fakeDashboardRepository
        )
    }

    @Test
    fun enabledModules_doesNotContainCrmByDefault() = runTest {
        advanceUntilIdle()
        val state = viewModel.uiState.value
        val containsCrm = state.enabledModules.any { it.id == ModuleId.CRM }
        assertFalse("Disabled CRM should not appear on dashboard", containsCrm)
    }

    @Test
    fun setWidgetVisibility_updatesWidgetVisibility() = runTest {
        advanceUntilIdle()
        viewModel.setWidgetVisibility("w_quick_glance", false)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        val widget = state.dashboardConfig.widgets.firstOrNull { it.id == "w_quick_glance" }
        assertFalse(widget?.isVisible ?: true)
    }

    @Test
    fun setWidgetSize_updatesWidgetSize() = runTest {
        advanceUntilIdle()
        viewModel.setWidgetSize("w_quick_glance", WidgetSize.HALF)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        val widget = state.dashboardConfig.widgets.firstOrNull { it.id == "w_quick_glance" }
        assertEquals(WidgetSize.HALF, widget?.size)
    }

    @Test
    fun addWidgetFromTemplate_addsNewWidget() = runTest {
        advanceUntilIdle()
        val initialCount = viewModel.uiState.value.dashboardConfig.widgets.size
        val educationTemplate = WidgetRegistry.templates.first { it.type == WidgetType.EDUCATION_PLANNER }

        viewModel.addWidgetFromTemplate(educationTemplate)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(initialCount + 1, state.dashboardConfig.widgets.size)
        assertTrue(state.dashboardConfig.widgets.any { it.type == WidgetType.EDUCATION_PLANNER })
    }

    @Test
    fun removeWidget_removesWidgetFromDashboard() = runTest {
        advanceUntilIdle()
        val targetId = "w_quick_note"
        viewModel.removeWidget(targetId)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.dashboardConfig.widgets.any { it.id == targetId })
    }

    @Test
    fun resetWidgetsToDefaults_resetsConfig() = runTest {
        advanceUntilIdle()
        viewModel.removeWidget("w_quick_glance")
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.dashboardConfig.widgets.any { it.id == "w_quick_glance" })

        viewModel.resetWidgetsToDefaults()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.dashboardConfig.widgets.any { it.id == "w_quick_glance" })
    }

    @Test
    fun logWater_increasesWaterIntake() = runTest {
        advanceUntilIdle()
        val initialWater = viewModel.uiState.value.dashboardConfig.widgets
            .first { it.type == WidgetType.HEALTH_SUMMARY }.waterIntakeMl

        viewModel.logWater(250)
        advanceUntilIdle()

        val updatedWater = viewModel.uiState.value.dashboardConfig.widgets
            .first { it.type == WidgetType.HEALTH_SUMMARY }.waterIntakeMl
        assertEquals(initialWater + 250, updatedWater)
    }

    @Test
    fun toggleTask_movesPendingTaskToCompleted() = runTest {
        advanceUntilIdle()
        val taskToToggle = "Read 20 pages"

        viewModel.toggleTask(taskToToggle)
        advanceUntilIdle()

        val glanceWidget = viewModel.uiState.value.dashboardConfig.widgets
            .first { it.type == WidgetType.QUICK_GLANCE }
        assertTrue(glanceWidget.completedTasks.contains(taskToToggle))
        assertFalse(glanceWidget.pendingTasks.contains(taskToToggle))
    }

    @Test
    fun saveQuickNote_updatesNoteContent() = runTest {
        advanceUntilIdle()
        val newNote = "Private biometric encrypted note"

        viewModel.saveQuickNote(newNote)
        advanceUntilIdle()

        val noteWidget = viewModel.uiState.value.dashboardConfig.widgets
            .first { it.type == WidgetType.QUICK_NOTE }
        assertEquals(newNote, noteWidget.quickNoteContent)
    }

    @Test
    fun customizeWidgetAccent_updatesAccentHex() = runTest {
        advanceUntilIdle()
        val newAccent = "#8B5CF6"

        viewModel.customizeWidgetAccent("w_quick_glance", newAccent)
        advanceUntilIdle()

        val widget = viewModel.uiState.value.dashboardConfig.widgets
            .first { it.id == "w_quick_glance" }
        assertEquals(newAccent, widget.accentColorHex)
    }

    @Test
    fun saveWidgetCustomization_updatesAccentAndSize() = runTest {
        advanceUntilIdle()
        val widget = viewModel.uiState.value.dashboardConfig.widgets.first { it.id == "w_quick_glance" }

        viewModel.openWidgetCustomizer(widget)
        viewModel.onCustomizingAccentChanged("#EC4899")
        viewModel.onCustomizingSizeChanged(WidgetSize.HALF)
        viewModel.saveWidgetCustomization()
        advanceUntilIdle()

        val updatedWidget = viewModel.uiState.value.dashboardConfig.widgets.first { it.id == "w_quick_glance" }
        assertEquals("#EC4899", updatedWidget.accentColorHex)
        assertEquals(WidgetSize.HALF, updatedWidget.size)
    }
}
