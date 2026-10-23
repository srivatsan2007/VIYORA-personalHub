package com.personallife.feature.widgetstudio.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personallife.core.widgetengine.model.WidgetConfiguration
import com.personallife.core.widgetengine.model.WidgetDefinition
import com.personallife.core.widgetengine.model.WidgetStatus
import com.personallife.core.widgetengine.registry.WidgetRegistry
import com.personallife.core.widgetpreview.components.LiquidGlassButton
import com.personallife.core.widgetpreview.components.LiquidGlassCard
import com.personallife.core.widgetpreview.components.LiquidGlassChip
import com.personallife.core.widgetpreview.renderer.WidgetPreviewRenderer

@Composable
fun WidgetCard(
    definition: WidgetDefinition,
    isFavorite: Boolean,
    onCustomizeClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onPinClick: () -> Unit,
    modifier: Modifier = Modifier,
    savedConfiguration: WidgetConfiguration? = null
) {
    val activeConfig = savedConfiguration ?: WidgetRegistry.createDefaultConfiguration(
        widgetId = definition.id,
        instanceId = definition.id
    )

    LiquidGlassCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = Color(0xFF1E293B).copy(alpha = 0.75f),
        accentColor = Color(0xFF38BDF8),
        glassOpacity = 0.85f,
        cornerRadius = 24.dp,
        borderWidth = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header: Title, Category & Favorite Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = definition.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = definition.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(onClick = onFavoriteToggle) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color(0xFFFB7185) else Color(0xFF94A3B8)
                    )
                }
            }

            // Status & Size Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(status = definition.status)

                LiquidGlassChip(
                    text = definition.defaultSize.label,
                    accentColor = Color(0xFFA3E635),
                    isSelected = false
                )

                LiquidGlassChip(
                    text = definition.category.title,
                    accentColor = Color(0xFFC4B5FD),
                    isSelected = false
                )
            }

            // Live High-Fidelity Preview Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF0F172A).copy(alpha = 0.6f))
                    .padding(8.dp)
            ) {
                WidgetPreviewRenderer(
                    definition = definition,
                    configuration = activeConfig,
                    modifier = Modifier.fillMaxWidth(),
                    onInteractiveClick = onCustomizeClick
                )
            }

            // Actions: Customize & Pin To Home Screen
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LiquidGlassButton(
                    text = "Customize",
                    icon = Icons.Default.Tune,
                    onClick = onCustomizeClick,
                    modifier = Modifier.weight(1f),
                    accentColor = Color(0xFF38BDF8)
                )

                if (definition.status.isInstallable) {
                    LiquidGlassButton(
                        text = "Add to Home",
                        icon = Icons.Default.Add,
                        onClick = onPinClick,
                        modifier = Modifier.weight(1f),
                        accentColor = Color(0xFFA3E635)
                    )
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: WidgetStatus) {
    val (bgColor, textColor) = when (status) {
        WidgetStatus.VERIFIED -> Color(0xFF14532D) to Color(0xFF86EFAC)
        WidgetStatus.IMPLEMENTED -> Color(0xFF1E3A8A) to Color(0xFF93C5FD)
        WidgetStatus.PREVIEW_ONLY -> Color(0xFF3F3F46) to Color(0xFFD4D4D8)
        WidgetStatus.REQUIRES_PERMISSION -> Color(0xFF713F12) to Color(0xFFFDE047)
        WidgetStatus.REQUIRES_SETUP -> Color(0xFF581C87) to Color(0xFFD8B4FE)
        WidgetStatus.PLANNED -> Color(0xFF27272A) to Color(0xFFA1A1AA)
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor.copy(alpha = 0.7f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = status.label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = textColor,
            fontSize = 11.sp
        )
    }
}
