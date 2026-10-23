package com.personallife.feature.widgetstudio.presentation.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GridView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.personallife.core.widgetengine.model.WidgetCategory
import com.personallife.core.widgetpreview.components.LiquidGlassChip

@Composable
fun CategoryChipsRow(
    selectedCategory: WidgetCategory?,
    showFavoritesOnly: Boolean,
    onCategorySelect: (WidgetCategory?) -> Unit,
    onToggleFavoritesOnly: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // "All" Chip
        LiquidGlassChip(
            text = "All Widgets",
            icon = Icons.Default.GridView,
            isSelected = selectedCategory == null && !showFavoritesOnly,
            accentColor = Color(0xFF38BDF8),
            onClick = {
                if (showFavoritesOnly) onToggleFavoritesOnly()
                onCategorySelect(null)
            }
        )

        // "Favorites" Chip
        LiquidGlassChip(
            text = "Favorites",
            icon = Icons.Default.Favorite,
            isSelected = showFavoritesOnly,
            accentColor = Color(0xFFFB7185),
            onClick = onToggleFavoritesOnly
        )

        // 16 Categories
        WidgetCategory.values().forEach { category ->
            LiquidGlassChip(
                text = category.title,
                isSelected = selectedCategory == category && !showFavoritesOnly,
                accentColor = when (category) {
                    WidgetCategory.CLOCK -> Color(0xFFA3E635)
                    WidgetCategory.MUSIC -> Color(0xFFC4B5FD)
                    WidgetCategory.BATTERY -> Color(0xFF34D399)
                    WidgetCategory.QUOTES -> Color(0xFFFDE047)
                    WidgetCategory.WEATHER -> Color(0xFF38BDF8)
                    WidgetCategory.GALLERY -> Color(0xFFF472B6)
                    WidgetCategory.SCREEN_TIME -> Color(0xFFFB923C)
                    WidgetCategory.TASKS -> Color(0xFF4ADE80)
                    WidgetCategory.EDUCATION -> Color(0xFF818CF8)
                    WidgetCategory.FINANCE -> Color(0xFF2DD4BF)
                    WidgetCategory.HEALTH -> Color(0xFFF43F5E)
                    WidgetCategory.NOTES -> Color(0xFFA78BFA)
                    WidgetCategory.SYSTEM_INFO -> Color(0xFF94A3B8)
                    WidgetCategory.UTILITIES -> Color(0xFFE2E8F0)
                    else -> Color(0xFF38BDF8)
                },
                onClick = {
                    if (showFavoritesOnly) onToggleFavoritesOnly()
                    onCategorySelect(category)
                }
            )
        }
    }
}
