package com.personallife.core.widgetpreview.renderer

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personallife.core.widgetengine.model.WidgetCategory
import com.personallife.core.widgetengine.model.WidgetConfiguration
import com.personallife.core.widgetengine.model.WidgetDefinition
import com.personallife.core.widgetpreview.components.LiquidGlassCard
import com.personallife.core.widgetpreview.components.LiquidGlassChip
import com.personallife.core.widgetpreview.components.parseColor
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Universal In-App Widget Preview Renderer.
 * Renders any widget definition with a specified WidgetConfiguration in high-fidelity Liquid Glass UI.
 */
@Composable
fun WidgetPreviewRenderer(
    definition: WidgetDefinition,
    configuration: WidgetConfiguration,
    modifier: Modifier = Modifier,
    onInteractiveClick: (() -> Unit)? = null
) {
    val bgColor = parseColor(configuration.backgroundColorHex, Color(0xFF1E293B))
    val accentColor = parseColor(configuration.accentColorHex, Color(0xFF38BDF8))
    val textColor = parseColor(configuration.textColorHex, Color(0xFFF8FAFC))
    val subtextColor = parseColor(configuration.secondaryTextColorHex, Color(0xFF94A3B8))

    LiquidGlassCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = bgColor,
        accentColor = accentColor,
        glassOpacity = configuration.glassOpacity,
        cornerRadius = configuration.cornerRadiusDp.dp,
        borderWidth = configuration.borderWidthDp.dp,
        onClick = onInteractiveClick
    ) {
        when (definition.category) {
            WidgetCategory.CLOCK -> ClockPreviewContent(definition, configuration, accentColor, textColor, subtextColor)
            WidgetCategory.MUSIC -> MusicPreviewContent(definition, configuration, accentColor, textColor, subtextColor)
            WidgetCategory.BATTERY -> BatteryPreviewContent(definition, configuration, accentColor, textColor, subtextColor)
            WidgetCategory.QUOTES -> QuotePreviewContent(definition, configuration, accentColor, textColor, subtextColor)
            WidgetCategory.WEATHER -> WeatherPreviewContent(definition, configuration, accentColor, textColor, subtextColor)
            WidgetCategory.GALLERY -> GalleryPreviewContent(definition, configuration, accentColor, textColor, subtextColor)
            WidgetCategory.SCREEN_TIME -> ScreenTimePreviewContent(definition, configuration, accentColor, textColor, subtextColor)
            WidgetCategory.TASKS -> TasksPreviewContent(definition, configuration, accentColor, textColor, subtextColor)
            WidgetCategory.EDUCATION -> EducationPreviewContent(definition, configuration, accentColor, textColor, subtextColor)
            WidgetCategory.FINANCE -> FinancePreviewContent(definition, configuration, accentColor, textColor, subtextColor)
            WidgetCategory.HEALTH -> HealthPreviewContent(definition, configuration, accentColor, textColor, subtextColor)
            WidgetCategory.NOTES -> NotesPreviewContent(definition, configuration, accentColor, textColor, subtextColor)
            WidgetCategory.CALENDAR -> CalendarPreviewContent(definition, configuration, accentColor, textColor, subtextColor)
            WidgetCategory.SYSTEM_INFO -> SystemInfoPreviewContent(definition, configuration, accentColor, textColor, subtextColor)
            WidgetCategory.ECOSYSTEM -> EcosystemPreviewContent(definition, configuration, accentColor, textColor, subtextColor)
            else -> GenericPreviewContent(definition, configuration, accentColor, textColor, subtextColor)
        }
    }
}

// -------------------------------------------------------------
// CATEGORY A: CLOCK PREVIEW CONTENT
// -------------------------------------------------------------
@Composable
private fun ClockPreviewContent(
    definition: WidgetDefinition,
    config: WidgetConfiguration,
    accentColor: Color,
    textColor: Color,
    subtextColor: Color
) {
    val currentTime = if (config.is24Hour) {
        if (config.showSeconds) "14:35:28" else "14:35"
    } else {
        if (config.showSeconds) "2:35:28 PM" else "2:35 PM"
    }
    val currentDate = SimpleDateFormat(config.dateFormat.ifBlank { "EEEE, MMMM d" }, Locale.getDefault()).format(Date())

    when (definition.id) {
        "sheer_sleeping_clock" -> {
            Row(
                modifier = Modifier.fillMaxWidth().padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = currentTime, color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.Black)
                    Text(text = "Monday", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(text = "27 Sept", color = Color(0xFF94A3B8), fontSize = 12.sp)
                }
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(RoundedCornerShape(topStart = 68.dp, bottomEnd = 16.dp))
                        .background(Color(0xFF5C7CFA)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "˘ ᵕ ˘", color = Color(0xFF0B1220), fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        "sheer_capsule_clock" -> {
            Column(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "22", color = Color.White, fontSize = 26.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                Box(modifier = Modifier.size(width = 16.dp, height = 2.dp).background(Color(0xFF5C7CFA).copy(alpha = 0.4f)))
                Text(text = "10", color = Color(0xFF5C7CFA), fontSize = 26.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
            }
        }
        "sheer_outline_tall_clock" -> {
            Box(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                Column(modifier = Modifier.align(Alignment.CenterStart)) {
                    Text(
                        text = "12\n06",
                        color = Color.White,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.ExtraLight,
                        lineHeight = 38.sp,
                        letterSpacing = 2.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF5C7CFA))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "06", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Black)
                        Text(text = "FRI", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        "clock_analog_classic", "clock_roman_analog" -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AnalogClockDialCanvas(accentColor = accentColor, modifier = Modifier.size(88.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    LiquidGlassChip(text = "PERSONAL HUB", accentColor = accentColor)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = currentTime, color = textColor, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text(text = currentDate, color = subtextColor, fontSize = 12.sp)
                }
            }
        }
        "clock_hybrid_watch" -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnalogClockDialCanvas(accentColor = accentColor, modifier = Modifier.size(76.dp))
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    LiquidGlassChip(text = "HYBRID WATCH", accentColor = accentColor)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = currentTime, color = textColor, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text(text = currentDate, color = subtextColor, fontSize = 12.sp)
                }
            }
        }
        "clock_flip_vintage" -> {
            Column {
                LiquidGlassChip(text = "RETRO FLIP", accentColor = accentColor)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("1", "4", ":", "3", "5").forEach { char ->
                        if (char == ":") {
                            Text(text = ":", color = accentColor, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                        } else {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color.Black.copy(alpha = 0.6f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.3f))
                            ) {
                                Text(
                                    text = char,
                                    color = textColor,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = currentDate, color = subtextColor, fontSize = 12.sp)
            }
        }
        else -> {
            // Cyber Digital / Minimal
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LiquidGlassChip(text = "PERSONAL HUB", accentColor = accentColor)
                    Text(text = "LIVE", color = accentColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = currentTime,
                    color = textColor,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = (-0.5).sp
                )
                Text(text = currentDate, color = subtextColor, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun AnalogClockDialCanvas(accentColor: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val radius = size.minDimension / 2
        val center = Offset(size.width / 2, size.height / 2)

        // Outer dial ring
        drawCircle(
            color = accentColor.copy(alpha = 0.25f),
            radius = radius - 4f,
            style = Stroke(width = 3.dp.toPx())
        )

        // Hour and minute ticks
        for (i in 0 until 12) {
            val angle = Math.toRadians((i * 30).toDouble())
            val startDist = radius - 16f
            val endDist = radius - 6f
            val start = Offset(
                center.x + (startDist * Math.sin(angle)).toFloat(),
                center.y - (startDist * Math.cos(angle)).toFloat()
            )
            val end = Offset(
                center.x + (endDist * Math.sin(angle)).toFloat(),
                center.y - (endDist * Math.cos(angle)).toFloat()
            )
            drawLine(
                color = if (i % 3 == 0) accentColor else accentColor.copy(alpha = 0.4f),
                start = start,
                end = end,
                strokeWidth = if (i % 3 == 0) 3.dp.toPx() else 1.5.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        // Hour hand (pointing to ~2:35)
        val hourAngle = Math.toRadians(75.0)
        val hourEnd = Offset(
            center.x + ((radius * 0.5f) * Math.sin(hourAngle)).toFloat(),
            center.y - ((radius * 0.5f) * Math.cos(hourAngle)).toFloat()
        )
        drawLine(
            color = Color.White,
            start = center,
            end = hourEnd,
            strokeWidth = 3.5.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Minute hand (pointing to ~35 min)
        val minAngle = Math.toRadians(210.0)
        val minEnd = Offset(
            center.x + ((radius * 0.75f) * Math.sin(minAngle)).toFloat(),
            center.y - ((radius * 0.75f) * Math.cos(minAngle)).toFloat()
        )
        drawLine(
            color = accentColor,
            start = center,
            end = minEnd,
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Center hub
        drawCircle(color = accentColor, radius = 5.dp.toPx())
    }
}

// -------------------------------------------------------------
// CATEGORY B: MUSIC PREVIEW CONTENT
// -------------------------------------------------------------
@Composable
private fun MusicPreviewContent(
    definition: WidgetDefinition,
    config: WidgetConfiguration,
    accentColor: Color,
    textColor: Color,
    subtextColor: Color
) {
    if (definition.id == "sheer_duotone_music") {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 68.dp, height = 76.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF5C7CFA)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🎧", fontSize = 28.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Shape of You", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = "Ed Sheeran", color = Color(0xFF94A3B8), fontSize = 12.sp)
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { 0.35f },
                    modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                    color = Color(0xFF5C7CFA),
                    trackColor = Color(0xFF1E293B)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "1:24", color = Color(0xFF64748B), fontSize = 10.sp)
                    Row {
                        Text(text = "⏮ ", color = Color.White, fontSize = 12.sp)
                        Text(text = " ▶ ", color = Color(0xFF5C7CFA), fontSize = 12.sp)
                        Text(text = " ⏭", color = Color.White, fontSize = 12.sp)
                    }
                    Text(text = "3:54", color = Color(0xFF64748B), fontSize = 10.sp)
                }
            }
        }
        return
    }

    if (definition.id == "sheer_orange_music") {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1.2f)) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF181E2C)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "♫", color = Color.White, fontSize = 16.sp)
                    }
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFFFD600)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "▶", color = Color.Black, fontSize = 14.sp)
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF181E2C)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "⏮", color = Color(0xFFCBD5E1), fontSize = 14.sp)
                    }
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF181E2C)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "⏭", color = Color(0xFFCBD5E1), fontSize = 14.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .size(width = 80.dp, height = 78.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFFF6B00)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🎧", fontSize = 32.sp)
            }
        }
        return
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LiquidGlassChip(text = "NOW PLAYING", accentColor = accentColor)
            Text(text = "Spotify", color = subtextColor, fontSize = 11.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentColor.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "♪", color = accentColor, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Midnight Sun", color = textColor, fontSize = 15.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(text = "Ludovico Einaudi", color = subtextColor, fontSize = 12.sp, maxLines = 1)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
                    Icon(imageVector = Icons.Default.SkipPrevious, contentDescription = null, tint = subtextColor)
                }
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(accentColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black, modifier = Modifier.size(20.dp))
                }
                IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
                    Icon(imageVector = Icons.Default.SkipNext, contentDescription = null, tint = subtextColor)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// CATEGORY C: BATTERY PREVIEW CONTENT
// -------------------------------------------------------------
@Composable
private fun BatteryPreviewContent(
    definition: WidgetDefinition,
    config: WidgetConfiguration,
    accentColor: Color,
    textColor: Color,
    subtextColor: Color
) {
    if (definition.id == "sheer_dotted_battery") {
        Box(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(36.dp)
                    .clip(RoundedCornerShape(bottomStart = 20.dp))
                    .background(Color(0xFF5C7CFA))
            )
            Column(modifier = Modifier.padding(top = 12.dp)) {
                Text(text = "discharging", color = Color(0xFF94A3B8), fontSize = 13.sp, fontFamily = FontFamily.Monospace)
                Text(text = "52%", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
            }
        }
        return
    }

    if (definition.id == "sheer_dotted_matrix_95") {
        Column(
            modifier = Modifier.fillMaxWidth().padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "ENERGY", color = Color(0xFF5C7CFA), fontSize = 11.sp, fontFamily = FontFamily.Monospace, letterSpacing = 2.sp)
            Text(
                text = "95",
                color = Color.White,
                fontSize = 52.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.sp
            )
            Text(text = "● ACTIVE", color = Color(0xFF94A3B8), fontSize = 10.sp, fontFamily = FontFamily.Monospace)
        }
        return
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            LiquidGlassChip(text = "BATTERY STATUS", accentColor = accentColor)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "85%", color = textColor, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Text(text = "Fast Charging • 38m to full", color = subtextColor, fontSize = 11.sp)
        }

        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(accentColor.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.BatteryChargingFull,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

// -------------------------------------------------------------
// CATEGORY D: QUOTE PREVIEW CONTENT
// -------------------------------------------------------------
@Composable
private fun QuotePreviewContent(
    definition: WidgetDefinition,
    config: WidgetConfiguration,
    accentColor: Color,
    textColor: Color,
    subtextColor: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LiquidGlassChip(text = "DAILY INSPIRATION", accentColor = accentColor)
            Icon(imageVector = Icons.Default.FormatQuote, contentDescription = null, tint = accentColor.copy(alpha = 0.7f), modifier = Modifier.size(20.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "\"${config.customQuoteText.ifBlank { "Small daily improvements over time lead to stunning results." }}\"",
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "— ${config.customQuoteAuthor.ifBlank { "Personal Hub" }}",
            color = accentColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// -------------------------------------------------------------
// CATEGORY E: WEATHER PREVIEW CONTENT
// -------------------------------------------------------------
@Composable
private fun WeatherPreviewContent(
    definition: WidgetDefinition,
    config: WidgetConfiguration,
    accentColor: Color,
    textColor: Color,
    subtextColor: Color
) {
    if (definition.id == "sheer_celestial_dial") {
        Column(
            modifier = Modifier.fillMaxWidth().padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "☀️", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "06:14 AM", color = Color(0xFF38BDF8), fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFF334155)))
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "07:11 PM", color = Color(0xFF94A3B8), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "🌙", fontSize = 16.sp)
            }
        }
        return
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            LiquidGlassChip(text = "SAN FRANCISCO", accentColor = accentColor)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "72°F", color = textColor, fontSize = 34.sp, fontWeight = FontWeight.Bold)
            Text(text = "Mostly Sunny • H:76° L:58°", color = subtextColor, fontSize = 11.sp)
        }

        Icon(
            imageVector = Icons.Default.WbSunny,
            contentDescription = null,
            tint = accentColor,
            modifier = Modifier.size(46.dp)
        )
    }
}

// -------------------------------------------------------------
// CATEGORY F: GALLERY PREVIEW CONTENT
// -------------------------------------------------------------
@Composable
private fun GalleryPreviewContent(
    definition: WidgetDefinition,
    config: WidgetConfiguration,
    accentColor: Color,
    textColor: Color,
    subtextColor: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LiquidGlassChip(text = "MEMORIES", accentColor = accentColor)
            Text(text = "Today", color = subtextColor, fontSize = 11.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(accentColor.copy(alpha = 0.25f)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "📷", fontSize = 28.sp)
                Text(text = "Tap to choose photo", color = textColor, fontSize = 12.sp)
            }
        }
    }
}

// -------------------------------------------------------------
// CATEGORY H: SCREEN TIME PREVIEW CONTENT
// -------------------------------------------------------------
@Composable
private fun ScreenTimePreviewContent(
    definition: WidgetDefinition,
    config: WidgetConfiguration,
    accentColor: Color,
    textColor: Color,
    subtextColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            LiquidGlassChip(text = "DIGITAL WELLBEING", accentColor = accentColor)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "2h 15m", color = textColor, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text(text = "Daily limit: 4h 00m (56%)", color = subtextColor, fontSize = 11.sp)
        }

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(accentColor.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Default.HourglassTop, contentDescription = null, tint = accentColor, modifier = Modifier.size(24.dp))
        }
    }
}

// -------------------------------------------------------------
// CATEGORY J: TASKS PREVIEW CONTENT
// -------------------------------------------------------------
@Composable
private fun TasksPreviewContent(
    definition: WidgetDefinition,
    config: WidgetConfiguration,
    accentColor: Color,
    textColor: Color,
    subtextColor: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LiquidGlassChip(text = "TODAY'S FOCUS", accentColor = accentColor)
            Text(text = "2 of 4 done", color = accentColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = accentColor, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Review project milestone", color = subtextColor, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.RadioButtonUnchecked, contentDescription = null, tint = subtextColor, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Workout session (45m)", color = textColor, fontSize = 13.sp)
        }
    }
}

// -------------------------------------------------------------
// CATEGORY K: EDUCATION PREVIEW CONTENT
// -------------------------------------------------------------
@Composable
private fun EducationPreviewContent(
    definition: WidgetDefinition,
    config: WidgetConfiguration,
    accentColor: Color,
    textColor: Color,
    subtextColor: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LiquidGlassChip(text = "STUDY PLANNER", accentColor = accentColor)
            Icon(imageVector = Icons.Default.School, contentDescription = null, tint = accentColor, modifier = Modifier.size(18.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Focus: 45m / 120m goal", color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { 0.38f },
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
            color = accentColor,
            trackColor = subtextColor.copy(alpha = 0.2f)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = "18 flashcards due • Mobile Arch in 6d", color = subtextColor, fontSize = 11.sp)
    }
}

// -------------------------------------------------------------
// CATEGORY L: FINANCE PREVIEW CONTENT
// -------------------------------------------------------------
@Composable
private fun FinancePreviewContent(
    definition: WidgetDefinition,
    config: WidgetConfiguration,
    accentColor: Color,
    textColor: Color,
    subtextColor: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LiquidGlassChip(text = "DAILY BUDGET", accentColor = accentColor)
            Text(text = "$57.50 left", color = accentColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(text = "Spent $42.50 of $100.00", color = textColor, fontSize = 15.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = { 0.425f },
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
            color = accentColor,
            trackColor = subtextColor.copy(alpha = 0.2f)
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Mini 7-day breakdown bars
        Row(
            modifier = Modifier.fillMaxWidth().height(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            listOf(0.4f, 0.6f, 0.3f, 0.8f, 0.5f, 0.425f, 0.2f).forEachIndexed { idx, ratio ->
                val isToday = idx == 5
                Box(
                    modifier = Modifier
                        .width(12.dp)
                        .height((ratio * 22).coerceAtLeast(3f).dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(if (isToday) accentColor else subtextColor.copy(alpha = 0.4f))
                )
            }
        }
    }
}

// -------------------------------------------------------------
// CATEGORY M: HEALTH PREVIEW CONTENT
// -------------------------------------------------------------
@Composable
private fun HealthPreviewContent(
    definition: WidgetDefinition,
    config: WidgetConfiguration,
    accentColor: Color,
    textColor: Color,
    subtextColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            LiquidGlassChip(text = "HYDRATION & STEPS", accentColor = accentColor)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "1.5L / 2.5L", color = textColor, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text = "5,420 / 10,000 steps today", color = subtextColor, fontSize = 11.sp)
        }

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = accentColor.copy(alpha = 0.2f),
            border = androidx.compose.foundation.BorderStroke(1.dp, accentColor)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = accentColor, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "+250ml", color = accentColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// -------------------------------------------------------------
// CATEGORY N: NOTES & VAULT PREVIEW CONTENT
// -------------------------------------------------------------
@Composable
private fun NotesPreviewContent(
    definition: WidgetDefinition,
    config: WidgetConfiguration,
    accentColor: Color,
    textColor: Color,
    subtextColor: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LiquidGlassChip(text = "ENCRYPTED VAULT", accentColor = accentColor)
            Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = accentColor, modifier = Modifier.size(18.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Review project milestone at 5 PM with client.",
            color = textColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = "AES-256 Keystore Protected", color = subtextColor, fontSize = 10.sp)
    }
}

// -------------------------------------------------------------
// CATEGORY: CALENDAR PREVIEW CONTENT
// -------------------------------------------------------------
@Composable
private fun CalendarPreviewContent(
    definition: WidgetDefinition,
    config: WidgetConfiguration,
    accentColor: Color,
    textColor: Color,
    subtextColor: Color
) {
    if (definition.id == "sheer_date_capsule") {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "9", color = Color(0xFF0B1220), fontSize = 18.sp, fontWeight = FontWeight.Black)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = "SEPT", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                Text(text = "Thursday", color = Color(0xFF8E9CAE), fontSize = 11.sp)
            }
        }
        return
    }

    if (definition.id == "sheer_day_selector") {
        Column(
            modifier = Modifier.fillMaxWidth().padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "14", color = Color(0xFF2A3C5E), fontSize = 15.sp, fontFamily = FontFamily.Monospace)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "> 15 WED <", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "16", color = Color(0xFF2A3C5E), fontSize = 15.sp, fontFamily = FontFamily.Monospace)
        }
        return
    }

    if (definition.id == "sheer_full_calendar") {
        Column(
            modifier = Modifier.fillMaxWidth().padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "OCTOBER 2026", color = Color(0xFF0B1220), fontSize = 13.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "S  M  T  W  T  F  S", color = Color(0xFF64748B), fontSize = 10.sp, fontFamily = FontFamily.Monospace)
            Text(
                text = "       1  2  3\n4  5  6  7  8  9 10\n11 12 13 14 15 16 17\n18 19 20 21 22 [23] 24\n25 26 27 28 29 30 31",
                color = Color(0xFF1E293B),
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                lineHeight = 14.sp
            )
        }
        return
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "OCTOBER 2026", color = Color(0xFF5C7CFA), fontSize = 13.sp, fontWeight = FontWeight.Black)
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF5C7CFA).copy(alpha = 0.2f))
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Text(text = "TODAY • 23 FRI", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "No events scheduled", color = Color(0xFF94A3B8), fontSize = 11.sp)
    }
}

// -------------------------------------------------------------
// CATEGORY O: SYSTEM INFO PREVIEW CONTENT
// -------------------------------------------------------------
@Composable
private fun SystemInfoPreviewContent(
    definition: WidgetDefinition,
    config: WidgetConfiguration,
    accentColor: Color,
    textColor: Color,
    subtextColor: Color
) {
    if (definition.id == "sheer_storage_capsule") {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "💾", fontSize = 14.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "128.4 / 256 GB", color = Color.White, fontSize = 14.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        }
        return
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LiquidGlassChip(text = "SYSTEM STATUS", accentColor = accentColor)
            Icon(imageVector = Icons.Default.Storage, contentDescription = null, tint = accentColor, modifier = Modifier.size(18.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Storage: 142 GB / 256 GB (55%)", color = textColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { 0.55f },
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
            color = accentColor,
            trackColor = subtextColor.copy(alpha = 0.2f)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = "Wi-Fi Connected • 5.2 GHz • Battery 85%", color = subtextColor, fontSize = 11.sp)
    }
}

// -------------------------------------------------------------
// OS ECOSYSTEMS: GOOGLE PIXEL, NOTHING OS & SAMSUNG ONE UI
// -------------------------------------------------------------
@Composable
private fun EcosystemPreviewContent(
    definition: WidgetDefinition,
    config: WidgetConfiguration,
    accentColor: Color,
    textColor: Color,
    subtextColor: Color
) {
    val currentTime = if (config.is24Hour) "14:35" else "2:35 PM"
    val currentDate = SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).format(Date())

    when (definition.id) {
        "pixel_at_a_glance" -> {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = currentDate, color = textColor, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(text = "At a Glance • Viyora", color = accentColor, fontSize = 12.sp)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(accentColor.copy(alpha = 0.2f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(text = "⚡ 64%", color = accentColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        "pixel_scalloped_clock" -> {
            Column(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = currentTime, color = textColor, fontSize = 34.sp, fontWeight = FontWeight.Black)
                Text(text = currentDate.uppercase(), color = accentColor, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "● 64% Battery • Charging", color = subtextColor, fontSize = 11.sp)
            }
        }
        "nothing_dot_clock" -> {
            Column(
                modifier = Modifier.fillMaxWidth().padding(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).background(Color(0xFFE11D48)))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "NOTHING (R)", color = Color(0xFFA1A1AA), fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                }
                Text(text = currentTime, color = Color.White, fontSize = 36.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                Text(text = "23 OCT // 64% PWR", color = Color(0xFFE11D48), fontSize = 11.sp, fontFamily = FontFamily.Monospace)
            }
        }
        "nothing_red_battery" -> {
            Column(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFE11D48)))
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "64%", color = Color.White, fontSize = 32.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                Text(text = "BATTERY", color = Color(0xFFA1A1AA), fontSize = 10.sp, fontFamily = FontFamily.Monospace)
            }
        }
        "oneui_squircle_clock" -> {
            Row(
                modifier = Modifier.fillMaxWidth().padding(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = currentTime, color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Light)
                    Text(text = currentDate, color = Color(0xFF94A3B8), fontSize = 12.sp)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF38BDF8).copy(alpha = 0.2f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "64%", color = Color(0xFF38BDF8), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text(text = "ONE UI", color = Color(0xFF64748B), fontSize = 9.sp)
                    }
                }
            }
        }
        "oneui_device_battery" -> {
            Column(modifier = Modifier.fillMaxWidth().padding(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Galaxy Phone", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Text(text = "64%", color = Color(0xFF38BDF8), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { 0.64f },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                    color = Color(0xFF38BDF8),
                    trackColor = Color(0xFF334155)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Battery • Normal", color = Color(0xFF94A3B8), fontSize = 11.sp)
            }
        }
        else -> GenericPreviewContent(definition, config, accentColor, textColor, subtextColor)
    }
}

// -------------------------------------------------------------
// GENERIC / FALLBACK PREVIEW
// -------------------------------------------------------------
@Composable
private fun GenericPreviewContent(
    definition: WidgetDefinition,
    config: WidgetConfiguration,
    accentColor: Color,
    textColor: Color,
    subtextColor: Color
) {
    when (definition.id) {
        "sheer_contact_pill" -> {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "💬", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "SAMIRA", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, letterSpacing = 2.sp)
            }
            return
        }
        "sheer_dark_toggle" -> {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "◐", color = Color.White, fontSize = 16.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Dark mode", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
            return
        }
        "sheer_google_search" -> {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "G", color = Color(0xFF4285F4), fontSize = 18.sp, fontWeight = FontWeight.Black)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Search", color = Color(0xFF5F6368), fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
            return
        }
        "sheer_bluetooth_toggle" -> {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "ᛒ", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Bluetooth", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
            return
        }
        "sheer_sound_toggle" -> {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "🔊", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Sound", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
            return
        }
        "sheer_steps_activity" -> {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(20.dp).clip(CircleShape).background(Color(0xFF00B894)), contentAlignment = Alignment.Center) {
                    Text(text = "↑", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "143", color = Color(0xFF0B1220), fontSize = 16.sp, fontWeight = FontWeight.Black)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "steps", color = Color(0xFF64748B), fontSize = 11.sp)
            }
            return
        }
        "sheer_quote_card" -> {
            Column(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                Text(
                    text = "“Success is not the key to happiness. Happiness is the key to success. If you love what you are doing you will be successful.”",
                    color = Color.White,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "— ALBERT SCHWEITZER", color = Color(0xFF5C7CFA), fontSize = 10.sp, fontFamily = FontFamily.Monospace)
            }
            return
        }
        "sheer_photo_memory" -> {
            Column(
                modifier = Modifier.fillMaxWidth().padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "🖼️", fontSize = 36.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "MEMORIES", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            }
            return
        }
        else -> {
            Column {
                LiquidGlassChip(text = definition.category.title.uppercase(), accentColor = accentColor)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = definition.name, color = textColor, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = definition.description, color = subtextColor, fontSize = 12.sp)
            }
        }
    }
}
