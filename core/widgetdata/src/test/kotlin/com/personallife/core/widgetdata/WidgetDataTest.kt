package com.personallife.core.widgetdata

import com.personallife.core.widgetdata.provider.BatterySnapshot
import com.personallife.core.widgetdata.provider.QuotesDataProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class WidgetDataTest {

    @Test
    fun quotesDataProvider_returnsCuratedQuotes() {
        val provider = QuotesDataProvider()
        val allQuotes = provider.getAllQuotes()

        assertTrue("Quotes collection must contain quotes", allQuotes.isNotEmpty())
        val randomQuote = provider.getRandomQuote()
        assertNotNull(randomQuote)
        assertFalse(randomQuote.quote.isBlank())
        assertFalse(randomQuote.author.isBlank())
    }

    @Test
    fun quotesDataProvider_filterByCategory_returnsExpectedItems() {
        val provider = QuotesDataProvider()
        val philosophyQuotes = provider.getQuotesByCategory("Philosophy")

        assertTrue("Philosophy quotes must exist", philosophyQuotes.isNotEmpty())
        philosophyQuotes.forEach {
            assertEquals("Philosophy", it.category)
        }
    }

    @Test
    fun batterySnapshot_containsValidMetrics() {
        val snapshot = BatterySnapshot(
            percentage = 88,
            isCharging = true,
            isPlugged = true,
            temperatureCelsius = 28.5f,
            voltageMv = 4150,
            health = "Good"
        )

        assertEquals(88, snapshot.percentage)
        assertTrue(snapshot.isCharging)
        assertTrue(snapshot.isPlugged)
        assertEquals(28.5f, snapshot.temperatureCelsius, 0.01f)
        assertEquals("Good", snapshot.health)
    }
}
