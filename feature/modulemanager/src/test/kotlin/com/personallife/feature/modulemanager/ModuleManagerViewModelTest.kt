package com.personallife.feature.modulemanager

import com.personallife.core.model.ModuleId
import com.personallife.core.testing.repository.FakeModuleRepository
import com.personallife.core.testing.util.MainDispatcherRule
import com.personallife.feature.modulemanager.presentation.ModuleManagerViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ModuleManagerViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeModuleRepository: FakeModuleRepository
    private lateinit var viewModel: ModuleManagerViewModel

    @Before
    fun setUp() {
        fakeModuleRepository = FakeModuleRepository()
        viewModel = ModuleManagerViewModel(fakeModuleRepository)
    }

    @Test
    fun crmModule_is_disabledByDefault() = runTest {
        advanceUntilIdle()
        val state = viewModel.uiState.value
        val crm = state.modules.firstOrNull { it.id == ModuleId.CRM }
        assertFalse("CRM should be disabled by default", crm?.isEnabled ?: true)
    }

    @Test
    fun toggleModule_enablesCrm_whenToggledOn() = runTest {
        advanceUntilIdle()
        viewModel.toggleModule(ModuleId.CRM, true)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        val crm = state.modules.firstOrNull { it.id == ModuleId.CRM }
        assertTrue("CRM should be enabled after toggling on", crm?.isEnabled ?: false)
    }
}
