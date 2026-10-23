package com.personallife.feature.dashboard.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.ViewColumn
import androidx.compose.material.icons.filled.ViewStream
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.personallife.core.model.DashboardWidget
import com.personallife.core.model.WidgetAccent
import com.personallife.core.model.WidgetRegistry
import com.personallife.core.model.WidgetSize
import com.personallife.core.model.WidgetTemplate

@Composable
fun WidgetManagerDialog(
    widgets: List<DashboardWidget>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onToggleVisibility: (String, Boolean) -> Unit,
    onToggleSize: (String, WidgetSize) -> Unit,
    onReorder: (String, Boolean) -> Unit,
    onCustomizeWidget: (DashboardWidget) -> Unit,
    onAddWidget: (WidgetTemplate) -> Unit,
    onRemoveWidget: (String) -> Unit,
    onResetToDefaults: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Dashboard Widget Manager",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { onTabSelected(0) },
                        text = { Text("Active (${widgets.size})") }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { onTabSelected(1) },
                        text = { Text("Add New") }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (selectedTab == 0) {
                    // TAB 1: Active Widgets Management
                    LazyColumn(
                        modifier = Modifier.height(360.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        itemsIndexed(widgets, key = { _, it -> it.id }) { index, widget ->
                            val accentColor = parseHexColor(widget.accentColorHex, MaterialTheme.colorScheme.primary)

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Reorder Arrows
                                Column(verticalArrangement = Arrangement.Center) {
                                    IconButton(
                                        onClick = { onReorder(widget.id, true) },
                                        enabled = index > 0,
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowUpward,
                                            contentDescription = "Move Up",
                                            tint = if (index > 0) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outlineVariant,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    IconButton(
                                        onClick = { onReorder(widget.id, false) },
                                        enabled = index < widgets.size - 1,
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowDownward,
                                            contentDescription = "Move Down",
                                            tint = if (index < widgets.size - 1) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outlineVariant,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(6.dp))

                                // Widget Title & Size Badge
                                Column(modifier = Modifier.weight(1f).padding(end = 4.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .clip(CircleShape)
                                                .background(accentColor)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = widget.title,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(2.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = accentColor.copy(alpha = 0.15f),
                                            modifier = Modifier.clickable {
                                                val nextSize = if (widget.size == WidgetSize.FULL) WidgetSize.HALF else WidgetSize.FULL
                                                onToggleSize(widget.id, nextSize)
                                            }
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = if (widget.size == WidgetSize.FULL) Icons.Default.ViewStream else Icons.Default.ViewColumn,
                                                    contentDescription = "Toggle Size",
                                                    tint = accentColor,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                                Spacer(modifier = Modifier.width(3.dp))
                                                Text(
                                                    text = if (widget.size == WidgetSize.FULL) "Full" else "Half",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = accentColor
                                                )
                                            }
                                        }
                                    }
                                }

                                // Customize Accent & Targets Button
                                IconButton(
                                    onClick = { onCustomizeWidget(widget) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Tune,
                                        contentDescription = "Customize",
                                        tint = accentColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                // Delete Widget Button
                                IconButton(
                                    onClick = { onRemoveWidget(widget.id) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Remove Widget",
                                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                // Visibility Switch
                                Switch(
                                    checked = widget.isVisible,
                                    onCheckedChange = { isChecked ->
                                        onToggleVisibility(widget.id, isChecked)
                                    },
                                    modifier = Modifier.padding(start = 2.dp)
                                )
                            }
                        }
                    }
                } else {
                    // TAB 2: Template Catalog / Add New Widget
                    LazyColumn(
                        modifier = Modifier.height(360.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(WidgetRegistry.templates) { template ->
                            val accentColor = Color(android.graphics.Color.parseColor(template.defaultAccent.hexColor))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = accentColor.copy(alpha = 0.15f)
                                        ) {
                                            Text(
                                                text = template.category,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = accentColor,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = template.defaultTitle,
                                            style = MaterialTheme.typography.titleSmall,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = template.defaultDescription,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                FilledTonalButton(
                                    onClick = { onAddWidget(template) },
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = accentColor.copy(alpha = 0.2f),
                                        contentColor = accentColor
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add",
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Add", style = MaterialTheme.typography.labelMedium)
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done")
            }
        },
        dismissButton = {
            TextButton(onClick = onResetToDefaults) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Reset Defaults")
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
fun WidgetCustomizerModal(
    widget: DashboardWidget,
    accentHex: String,
    size: WidgetSize,
    waterTarget: String,
    dailyBudget: String,
    screenLimit: String,
    onAccentChange: (String) -> Unit,
    onSizeChange: (WidgetSize) -> Unit,
    onWaterTargetChange: (String) -> Unit,
    onDailyBudgetChange: (String) -> Unit,
    onScreenLimitChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
    val activeAccentColor = parseHexColor(accentHex, MaterialTheme.colorScheme.primary)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Customize ${widget.title}",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Color Accent Palette
                Text(
                    text = "Accent Color",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    WidgetAccent.entries.forEach { item ->
                        val color = Color(android.graphics.Color.parseColor(item.hexColor))
                        val isSelected = accentHex.equals(item.hexColor, ignoreCase = true)

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { onAccentChange(item.hexColor) },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surface)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Layout Size Selector
                Text(
                    text = "Display Layout",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val fullSelected = size == WidgetSize.FULL
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (fullSelected) activeAccentColor.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (fullSelected) activeAccentColor else Color.Transparent
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onSizeChange(WidgetSize.FULL) }
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.ViewStream,
                                contentDescription = null,
                                tint = if (fullSelected) activeAccentColor else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Full Width",
                                style = MaterialTheme.typography.labelMedium,
                                color = if (fullSelected) activeAccentColor else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    val halfSelected = size == WidgetSize.HALF
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (halfSelected) activeAccentColor.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (halfSelected) activeAccentColor else Color.Transparent
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onSizeChange(WidgetSize.HALF) }
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.ViewColumn,
                                contentDescription = null,
                                tint = if (halfSelected) activeAccentColor else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Compact (Half)",
                                style = MaterialTheme.typography.labelMedium,
                                color = if (halfSelected) activeAccentColor else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // Metric Target inputs depending on widget type
                when (widget.type) {
                    com.personallife.core.model.WidgetType.HEALTH_SUMMARY -> {
                        Spacer(modifier = Modifier.height(14.dp))
                        OutlinedTextField(
                            value = waterTarget,
                            onValueChange = onWaterTargetChange,
                            label = { Text("Daily Water Goal (ml)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                    com.personallife.core.model.WidgetType.EXPENSE_METRICS -> {
                        Spacer(modifier = Modifier.height(14.dp))
                        OutlinedTextField(
                            value = dailyBudget,
                            onValueChange = onDailyBudgetChange,
                            label = { Text("Daily Budget Goal ($)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                    com.personallife.core.model.WidgetType.SCREEN_TIME_PREVIEW -> {
                        Spacer(modifier = Modifier.height(14.dp))
                        OutlinedTextField(
                            value = screenLimit,
                            onValueChange = onScreenLimitChange,
                            label = { Text("Daily Screen Limit (mins)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                    else -> Unit
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onSave) {
                Text("Save Changes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
fun QuickNoteEditorDialog(
    initialText: String,
    onSaveNote: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var textState by remember { mutableStateOf(initialText) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Hardware Encrypted Scratchpad",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column {
                Text(
                    text = "Notes saved here are protected with AES-256 in Android Keystore memory.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = textState,
                    onValueChange = { textState = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    placeholder = { Text("Write your private note...") },
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSaveNote(textState) }) {
                Text("Save Encrypted")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}
