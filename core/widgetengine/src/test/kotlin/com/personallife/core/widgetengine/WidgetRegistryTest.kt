package com.personallife.core.widgetengine

import com.personallife.core.widgetengine.model.WidgetCategory
import com.personallife.core.widgetengine.model.WidgetSizePreset
import com.personallife.core.widgetengine.model.WidgetStatus
import com.personallife.core.widgetengine.model.WidgetThemePreset
import com.personallife.core.widgetengine.registry.WidgetRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class WidgetRegistryTest {

    @Test
    fun widgetRegistry_hasDefinitionsAcrossMultipleCategories() {
        val definitions = WidgetRegistry.definitions
        assertTrue("Widget registry must contain definitions", definitions.isNotEmpty())

        val categoriesPresent = definitions.map { it.category }.distinct()
        assertTrue("Registry must span at least 10 categories", categoriesPresent.size >= 10)
        assertTrue(categoriesPresent.contains(WidgetCategory.CLOCK))
        assertTrue(categoriesPresent.contains(WidgetCategory.BATTERY))
        assertTrue(categoriesPresent.contains(WidgetCategory.QUOTES))
        assertTrue(categoriesPresent.contains(WidgetCategory.MUSIC))
        assertTrue(categoriesPresent.contains(WidgetCategory.SCREEN_TIME))
    }

    @Test
    fun widgetRegistry_findsWidgetByIdSuccessfully() {
        val clock = WidgetRegistry.findById("clock_digital_cyber")
        assertNotNull("Cyber Digital Clock must exist in registry", clock)
        assertEquals("Cyber Digital Clock", clock?.name)
        assertEquals(WidgetCategory.CLOCK, clock?.category)
        assertEquals(WidgetStatus.VERIFIED, clock?.status)
    }

    @Test
    fun widgetRegistry_createsValidDefaultConfiguration() {
        val config = WidgetRegistry.createDefaultConfiguration(
            widgetId = "clock_digital_cyber",
            instanceId = "test_instance_101"
        )
        assertEquals("test_instance_101", config.instanceId)
        assertEquals("clock_digital_cyber", config.widgetId)
        assertFalse(config.backgroundColorHex.isBlank())
        assertFalse(config.accentColorHex.isBlank())
        assertTrue(config.glassOpacity in 0.0f..1.0f)
        assertTrue(config.cornerRadiusDp > 0)
    }

    @Test
    fun widgetThemePreset_findByName_returnsExpectedPresetOrFallback() {
        val olive = WidgetThemePreset.findByName("OLIVE_CREAM")
        assertEquals(WidgetThemePreset.OLIVE_AND_CREAM.displayName, olive.displayName)

        val mint = WidgetThemePreset.findByName("MINT_LAVENDER")
        assertEquals(WidgetThemePreset.MINT_AND_LAVENDER.displayName, mint.displayName)

        val fallback = WidgetThemePreset.findByName("NON_EXISTENT_PRESET")
        assertEquals(WidgetThemePreset.OLIVE_AND_CREAM.displayName, fallback.displayName)
    }

    @Test
    fun widgetSizePreset_dimensions_arePositive() {
        WidgetSizePreset.values().forEach { size ->
            assertTrue(size.cellWidth > 0)
            assertTrue(size.cellHeight > 0)
            assertTrue(size.minWidthDp > 0)
            assertTrue(size.minHeightDp > 0)
        }
    }
}
