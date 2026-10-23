package com.personallife.feature.widgetstudio.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.ViewQuilt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personallife.core.widgetengine.model.WidgetCategory
import com.personallife.core.widgetengine.model.WidgetSizePreset
import com.personallife.core.widgetengine.model.WidgetThemePreset
import com.personallife.core.widgetpreview.components.LiquidGlassButton
import com.personallife.core.widgetpreview.components.LiquidGlassCard
import com.personallife.core.widgetpreview.components.LiquidGlassChip
import com.personallife.core.widgetpreview.components.LiquidGlassSlider
import com.personallife.core.widgetpreview.components.parseColor
import com.personallife.core.widgetpreview.renderer.WidgetPreviewRenderer
import com.personallife.feature.widgetstudio.presentation.components.ManualAddDialog
import com.personallife.feature.widgetstudio.presentation.components.PermissionExplanationDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WidgetCustomizationStudioScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WidgetCustomizationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.statusMessage) {
        uiState.statusMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearStatusMessage()
        }
    }

    if (uiState.showManualAddDialog && uiState.definition != null) {
        ManualAddDialog(
            widgetName = uiState.definition!!.name,
            onDismiss = { viewModel.dismissManualAddDialog() }
        )
    }

    if (uiState.showPermissionDialog && uiState.definition != null) {
        PermissionExplanationDialog(
            requirement = uiState.definition!!.requiredPermission,
            onDismiss = { viewModel.dismissPermissionDialog() }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFF0F172A),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = uiState.definition?.name ?: "Widget Studio",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Customization Studio",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF94A3B8)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.resetToDefaults() }) {
                        Icon(
                            imageVector = Icons.Default.RestartAlt,
                            contentDescription = "Reset Defaults",
                            tint = Color(0xFF94A3B8)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0F172A).copy(alpha = 0.95f)
                )
            )
        },
        bottomBar = {
            // Persistent Bottom Action Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0F172A).copy(alpha = 0.95f))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LiquidGlassButton(
                        text = if (uiState.isSaved) "Saved" else "Save Settings",
                        icon = if (uiState.isSaved) Icons.Default.Check else Icons.Default.Save,
                        onClick = { viewModel.saveConfiguration() },
                        modifier = Modifier.weight(1f),
                        accentColor = Color(0xFF38BDF8)
                    )

                    LiquidGlassButton(
                        text = "Add to Home",
                        icon = Icons.Default.Add,
                        onClick = { viewModel.pinToHomeScreen(context) },
                        modifier = Modifier.weight(1f),
                        accentColor = Color(0xFFA3E635)
                    )
                }
            }
        }
    ) { innerPadding ->
        val definition = uiState.definition
        val config = uiState.configuration

        if (definition != null && config != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // ==========================================
                // 1. LIVE INTERACTIVE PREVIEW
                // ==========================================
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "LIVE PREVIEW",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA3E635),
                        letterSpacing = 1.2.sp
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(26.dp))
                            .background(Color(0xFF0B1120))
                            .border(1.dp, Color(0xFF334155), RoundedCornerShape(26.dp))
                            .padding(14.dp)
                    ) {
                        WidgetPreviewRenderer(
                            definition = definition,
                            configuration = config,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // ==========================================
                // 2. SIZE & LAYOUT PRESETS
                // ==========================================
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SectionHeader(
                        icon = Icons.Default.ViewQuilt,
                        title = "Size & Grid Layout",
                        accentColor = Color(0xFF38BDF8)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        definition.supportedSizes.forEach { sizePreset ->
                            LiquidGlassChip(
                                text = sizePreset.label,
                                isSelected = config.sizePreset == sizePreset,
                                accentColor = Color(0xFF38BDF8),
                                onClick = { viewModel.updateSizePreset(sizePreset) }
                            )
                        }
                    }
                }

                // ==========================================
                // 3. THEME PRESETS (Liquid Glass & Organic)
                // ==========================================
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SectionHeader(
                        icon = Icons.Default.Palette,
                        title = "Appearance Theme Presets",
                        accentColor = Color(0xFFC4B5FD)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        WidgetThemePreset.allPresets.forEach { preset ->
                            ThemePresetCard(
                                preset = preset,
                                isSelected = config.themePresetName == preset.name,
                                onClick = { viewModel.updateThemePreset(preset) }
                            )
                        }
                    }
                }

                // ==========================================
                // 4. LIQUID GLASS APPERANCE SLIDERS
                // ==========================================
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SectionHeader(
                        icon = Icons.Default.ColorLens,
                        title = "Glass Surface & Geometry",
                        accentColor = Color(0xFFA3E635)
                    )

                    LiquidGlassSlider(
                        value = config.glassOpacity,
                        onValueChange = { viewModel.updateGlassOpacity(it) },
                        valueRange = 0.2f..1.0f,
                        label = "Surface Opacity",
                        displayValue = "${(config.glassOpacity * 100).toInt()}%",
                        accentColor = Color(0xFFA3E635)
                    )

                    LiquidGlassSlider(
                        value = config.cornerRadiusDp.toFloat(),
                        onValueChange = { viewModel.updateCornerRadius(it.toInt()) },
                        valueRange = 8f..36f,
                        label = "Corner Radius",
                        displayValue = "${config.cornerRadiusDp} dp",
                        accentColor = Color(0xFF38BDF8)
                    )

                    LiquidGlassSlider(
                        value = config.borderWidthDp,
                        onValueChange = { viewModel.updateBorderWidth(it) },
                        valueRange = 0f..3f,
                        label = "Glass Border Width",
                        displayValue = String.format("%.1f dp", config.borderWidthDp),
                        accentColor = Color(0xFFC4B5FD)
                    )
                }

                // ==========================================
                // 5. ACCENT COLOR PALETTE SWATCHES
                // ==========================================
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Vibrant Accent Color",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )

                    val curatedColors = listOf(
                        "#A3E635" to "Lime",
                        "#34D399" to "Mint",
                        "#38BDF8" to "Sky",
                        "#C4B5FD" to "Lavender",
                        "#FB7185" to "Rose",
                        "#F59E0B" to "Amber",
                        "#00F5FF" to "Cyan",
                        "#F8FAFC" to "White"
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        curatedColors.forEach { (hex, _) ->
                            val color = parseColor(hex, Color.White)
                            val isSelected = config.accentColorHex.equals(hex, ignoreCase = true)

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) Color.White else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable { viewModel.updateAccentColorHex(hex) }
                            )
                        }
                    }
                }

                // ==========================================
                // 6. CONTENT & BEHAVIOR CONTROLS
                // ==========================================
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "CONTENT & BEHAVIOR",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8),
                        letterSpacing = 1.2.sp
                    )

                    when (definition.category) {
                        WidgetCategory.CLOCK -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "24-Hour Time Format",
                                    color = Color.White,
                                    fontSize = 15.sp
                                )
                                Switch(
                                    checked = config.is24Hour,
                                    onCheckedChange = { viewModel.updateTimeFormat(it, config.showSeconds) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color(0xFFA3E635),
                                        checkedTrackColor = Color(0xFF14532D)
                                    )
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Show Seconds Hand / Pulse",
                                    color = Color.White,
                                    fontSize = 15.sp
                                )
                                Switch(
                                    checked = config.showSeconds,
                                    onCheckedChange = { viewModel.updateTimeFormat(config.is24Hour, it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color(0xFF38BDF8),
                                        checkedTrackColor = Color(0xFF1E3A8A)
                                    )
                                )
                            }
                        }

                        WidgetCategory.QUOTES -> {
                            OutlinedTextField(
                                value = config.customQuoteText,
                                onValueChange = { viewModel.updateCustomQuote(it, config.customQuoteAuthor) },
                                label = { Text("Custom Quote / Affirmation", color = Color(0xFF94A3B8)) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF38BDF8),
                                    unfocusedBorderColor = Color(0xFF475569),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )

                            OutlinedTextField(
                                value = config.customQuoteAuthor,
                                onValueChange = { viewModel.updateCustomQuote(config.customQuoteText, it) },
                                label = { Text("Author Attribution", color = Color(0xFF94A3B8)) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF38BDF8),
                                    unfocusedBorderColor = Color(0xFF475569),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )
                        }

                        WidgetCategory.HEALTH -> {
                            OutlinedTextField(
                                value = config.waterTargetMl.toString(),
                                onValueChange = {
                                    val ml = it.toIntOrNull() ?: 2500
                                    viewModel.updateTargetValues(waterMl = ml)
                                },
                                label = { Text("Daily Water Goal (ml)", color = Color(0xFF94A3B8)) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF34D399),
                                    unfocusedBorderColor = Color(0xFF475569),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )
                        }

                        WidgetCategory.FINANCE -> {
                            OutlinedTextField(
                                value = config.dailyBudget.toString(),
                                onValueChange = {
                                    val budget = it.toDoubleOrNull() ?: 100.0
                                    viewModel.updateTargetValues(budget = budget)
                                },
                                label = { Text("Daily Spending Budget ($)", color = Color(0xFF94A3B8)) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF38BDF8),
                                    unfocusedBorderColor = Color(0xFF475569),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )
                        }

                        else -> {
                            Text(
                                text = "Standard layout automatically optimized for ${definition.category.title}.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
private fun SectionHeader(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    accentColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = accentColor,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

@Composable
private fun ThemePresetCard(
    preset: WidgetThemePreset,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bg = parseColor(preset.backgroundColorHex, Color.DarkGray)
    val accent = parseColor(preset.accentColorHex, Color.Cyan)

    Box(
        modifier = Modifier
            .width(130.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(bg.copy(alpha = 0.9f))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) accent else Color(0xFF334155),
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(accent)
                )
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(parseColor(preset.textColorHex, Color.White))
                )
            }
            Text(
                text = preset.displayName,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = Color.White,
                maxLines = 1
            )
        }
    }
}
