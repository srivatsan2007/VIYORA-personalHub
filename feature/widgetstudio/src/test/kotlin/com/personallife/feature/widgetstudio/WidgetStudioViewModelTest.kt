package com.personallife.feature.widgetstudio

import com.personallife.core.widgetdata.repository.WidgetConfigurationRepository
import com.personallife.core.widgetengine.model.WidgetCategory
import com.personallife.core.widgetengine.model.WidgetConfiguration
import com.personallife.core.widgetengine.registry.WidgetRegistry
import com.personallife.feature.widgetstudio.presentation.WidgetStudioViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WidgetStudioViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val favoritesFlow = MutableStateFlow<Set<String>>(emptySet())
    private val recentFlow = MutableStateFlow<List<WidgetConfiguration>>(emptyList())

    private val fakeRepository = object : WidgetConfigurationRepository {
        override fun getConfiguration(instanceId: String): WidgetConfiguration {
            return WidgetRegistry.createDefaultConfiguration("clock_digital_cyber", instanceId)
        }

        override fun getConfigurationForAppWidget(appWidgetId: Int): WidgetConfiguration {
            return getConfiguration("appwidget_$appWidgetId")
        }

        override suspend fun saveConfiguration(config: WidgetConfiguration) {}

        override suspend fun deleteConfiguration(instanceId: String) {}

        override suspend fun deleteConfigurationForAppWidget(appWidgetId: Int) {}

        override val favoriteWidgetIds: Flow<Set<String>> = favoritesFlow

        override suspend fun toggleFavorite(widgetId: String) {
            val current = favoritesFlow.value.toMutableSet()
            if (current.contains(widgetId)) current.remove(widgetId) else current.add(widgetId)
            favoritesFlow.value = current
        }

        override val recentlyCustomized: Flow<List<WidgetConfiguration>> = recentFlow
    }

    private lateinit var viewModel: WidgetStudioViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = WidgetStudioViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_loadsAllWidgetsFromRegistry() = runTest(testDispatcher) {
        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertEquals("", state.searchQuery)
        assertEquals(null, state.selectedCategory)
        assertFalse(state.showFavoritesOnly)
        assertTrue(state.filteredWidgets.isNotEmpty())
        assertEquals(WidgetRegistry.definitions.size, state.filteredWidgets.size)
    }

    @Test
    fun searchFiltering_returnsMatchingWidgetsOnly() = runTest(testDispatcher) {
        advanceUntilIdle()

        viewModel.onSearchQueryChange("battery")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("battery", state.searchQuery)
        assertTrue(state.filteredWidgets.isNotEmpty())
        state.filteredWidgets.forEach { def ->
            val matches = def.name.contains("battery", ignoreCase = true) ||
                    def.description.contains("battery", ignoreCase = true) ||
                    def.category.title.contains("battery", ignoreCase = true)
            assertTrue(matches)
        }
    }

    @Test
    fun categoryFiltering_narrowsWidgetsToCategory() = runTest(testDispatcher) {
        advanceUntilIdle()

        viewModel.onCategorySelect(WidgetCategory.CLOCK)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(WidgetCategory.CLOCK, state.selectedCategory)
        assertTrue(state.filteredWidgets.isNotEmpty())
        state.filteredWidgets.forEach { def ->
            assertEquals(WidgetCategory.CLOCK, def.category)
        }
    }

    @Test
    fun toggleFavorite_updatesFavoritesFlow() = runTest(testDispatcher) {
        advanceUntilIdle()

        viewModel.onToggleFavorite("clock_digital_cyber")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.favoriteWidgetIds.contains("clock_digital_cyber"))

        viewModel.onToggleFavorite("clock_digital_cyber")
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.favoriteWidgetIds.contains("clock_digital_cyber"))
    }
}
