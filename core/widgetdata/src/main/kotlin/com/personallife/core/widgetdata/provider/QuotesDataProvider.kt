package com.personallife.core.widgetdata.provider

import javax.inject.Inject
import javax.inject.Singleton

data class QuoteItem(
    val id: String,
    val quote: String,
    val author: String,
    val category: String
)

@Singleton
class QuotesDataProvider @Inject constructor() {

    private val quotesList = listOf(
        QuoteItem(
            id = "q1",
            quote = "Simplicity is the ultimate sophistication.",
            author = "Leonardo da Vinci",
            category = "Design"
        ),
        QuoteItem(
            id = "q2",
            quote = "Small daily improvements over time lead to stunning results.",
            author = "Robin Sharma",
            category = "Motivation"
        ),
        QuoteItem(
            id = "q3",
            quote = "We suffer more often in imagination than in reality.",
            author = "Seneca",
            category = "Philosophy"
        ),
        QuoteItem(
            id = "q4",
            quote = "Focus is a muscle. The more you protect your attention, the stronger it becomes.",
            author = "Cal Newport",
            category = "Study"
        ),
        QuoteItem(
            id = "q5",
            quote = "Peace comes from within. Do not seek it without.",
            author = "Buddha",
            category = "Mindfulness"
        ),
        QuoteItem(
            id = "q6",
            quote = "Discipline equals freedom.",
            author = "Jocko Willink",
            category = "Discipline"
        ),
        QuoteItem(
            id = "q7",
            quote = "Act as if what you do makes a difference. It does.",
            author = "William James",
            category = "Motivation"
        ),
        QuoteItem(
            id = "q8",
            quote = "Live in each season as it passes; breathe the air, drink the drink.",
            author = "Henry David Thoreau",
            category = "Calm"
        )
    )

    fun getDailyQuote(): QuoteItem {
        val dayOfYear = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR)
        val index = (dayOfYear - 1) % quotesList.size
        return quotesList[index]
    }

    fun getRandomQuote(): QuoteItem = quotesList.random()

    fun getAllQuotes(): List<QuoteItem> = quotesList

    fun getQuotesByCategory(category: String): List<QuoteItem> =
        quotesList.filter { it.category.equals(category, ignoreCase = true) }
}
