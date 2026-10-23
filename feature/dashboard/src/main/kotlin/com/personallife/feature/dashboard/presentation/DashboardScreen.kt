package com.personallife.feature.dashboard.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personallife.core.designsystem.component.AppCard
import com.personallife.core.designsystem.component.LoadingStateView
import com.personallife.core.designsystem.component.ModuleStatusBadge
import com.personallife.core.designsystem.component.SectionHeader
import com.personallife.core.model.AppModule
import com.personallife.core.model.ModuleId
import com.personallife.core.model.WidgetSize
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToWidgetStudio: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isLoading) {
        LoadingStateView(message = "Loading Viyora...")
        return
    }

    val greeting = getGreeting(uiState.userProfile?.displayName)
    val currentDateFormatted = getFormattedDate()

    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Bar: Viyora Logo + Greeting + Profile + Settings Shortcuts
            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = com.personallife.core.designsystem.R.drawable.ic_viyora_logo),
                            contentDescription = "Viyora",
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "VIYORA • ${currentDateFormatted.uppercase(Locale.getDefault())}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = greeting,
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Widget Studio Shortcut
                        IconButton(onClick = onNavigateToWidgetStudio) {
                            Icon(
                                imageVector = Icons.Default.Widgets,
                                contentDescription = "Widget Studio",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        // Profile Avatar Shortcut
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .clickable(onClick = onNavigateToProfile),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = uiState.userProfile?.initials ?: "U",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // Settings Shortcut
                        IconButton(onClick = onNavigateToSettings) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Search Bar
            item(span = { GridItemSpan(2) }) {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = viewModel::onSearchQueryChanged,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search your hub, notes & records...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    ),
                    singleLine = true
                )
            }

            // Quick Actions Horizontal Row
            item(span = { GridItemSpan(2) }) {
                QuickActionsRow(
                    onActionClick = { actionTitle ->
                        val dummyModule = AppModule(
                            id = ModuleId.DASHBOARD,
                            title = actionTitle,
                            description = "Quick action entry point for $actionTitle",
                            isEnabled = true,
                            isImplemented = false,
                            iconName = "",
                            category = "Action"
                        )
                        viewModel.selectPlaceholderModule(dummyModule)
                    }
                )
            }

            // In-App Widgets Header with Add & Customize Actions
            item(span = { GridItemSpan(2) }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Dashboard Widgets",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TextButton(onClick = { viewModel.openWidgetManager(1) }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Widget",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add", style = MaterialTheme.typography.labelMedium)
                        }

                        TextButton(onClick = { viewModel.openWidgetManager(0) }) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "Manage Widgets",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Manage", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }

            // In-App Widgets Grid Items (Full vs Half Size adaptive layout)
            val visibleWidgets = uiState.dashboardConfig.widgets.filter { it.isVisible }
            if (visibleWidgets.isEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    AppCard {
                        Text(
                            text = "No widgets currently visible. Tap 'Add' or 'Manage' to configure your widgets.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(
                    items = visibleWidgets,
                    key = { it.id },
                    span = { widget ->
                        GridItemSpan(if (widget.size == WidgetSize.HALF) 1 else 2)
                    }
                ) { widget ->
                    DashboardWidgetRenderer(
                        widget = widget,
                        onLogWater = viewModel::logWater,
                        onToggleTask = viewModel::toggleTask,
                        onOpenNoteEditor = viewModel::openQuickNoteEditor,
                        onCustomizeWidget = viewModel::openWidgetCustomizer
                    )
                }
            }

            // Active Enabled Modules Section
            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(6.dp))
                SectionHeader(title = "Life Spaces & Modules")
            }

            items(
                items = uiState.enabledModules,
                key = { it.id.key },
                span = { GridItemSpan(2) }
            ) { module ->
                ModuleCardItem(
                    module = module,
                    onClick = { viewModel.selectPlaceholderModule(module) }
                )
            }

            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // Phase 2: Enhanced Tabbed Widget Manager Dialog (Active & Catalog)
    if (uiState.isWidgetManagerOpen) {
        WidgetManagerDialog(
            widgets = uiState.dashboardConfig.widgets,
            selectedTab = uiState.widgetManagerTab,
            onTabSelected = viewModel::setWidgetManagerTab,
            onToggleVisibility = viewModel::setWidgetVisibility,
            onToggleSize = viewModel::setWidgetSize,
            onReorder = viewModel::reorderWidget,
            onCustomizeWidget = viewModel::openWidgetCustomizer,
            onAddWidget = viewModel::addWidgetFromTemplate,
            onRemoveWidget = viewModel::removeWidget,
            onResetToDefaults = viewModel::resetWidgetsToDefaults,
            onDismiss = viewModel::closeWidgetManager
        )
    }

    // Phase 2: Widget Theme Accent & Custom Targets Modal
    uiState.selectedWidgetForCustomization?.let { widget ->
        WidgetCustomizerModal(
            widget = widget,
            accentHex = uiState.customizingAccentHex,
            size = uiState.customizingSize,
            waterTarget = uiState.customizingWaterTarget,
            dailyBudget = uiState.customizingDailyBudget,
            screenLimit = uiState.customizingScreenLimit,
            onAccentChange = viewModel::onCustomizingAccentChanged,
            onSizeChange = viewModel::onCustomizingSizeChanged,
            onWaterTargetChange = viewModel::onCustomizingWaterTargetChanged,
            onDailyBudgetChange = viewModel::onCustomizingBudgetChanged,
            onScreenLimitChange = viewModel::onCustomizingScreenLimitChanged,
            onSave = viewModel::saveWidgetCustomization,
            onDismiss = viewModel::closeWidgetCustomizer
        )
    }

    // Hardware Encrypted Quick Note Editor Dialog
    if (uiState.isQuickNoteEditorOpen) {
        QuickNoteEditorDialog(
            initialText = uiState.editingQuickNoteText,
            onSaveNote = viewModel::saveQuickNote,
            onDismiss = viewModel::closeQuickNoteEditor
        )
    }

    // Planned Module Information Dialog
    uiState.selectedPlaceholderModule?.let { module ->
        AlertDialog(
            onDismissRequest = viewModel::dismissPlaceholderDialog,
            title = {
                Text(
                    text = module.title,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Column {
                    ModuleStatusBadge(status = module.statusLabel)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = module.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (!module.isImplemented) {
                            "This module is planned for Phase 3/4 of the Personal Hub roadmap. In Phase 1 & 2, its foundation, encrypted schema, and module visibility preferences are active and persisted. Tap 'Settings -> Module Manager' to configure its visibility."
                        } else {
                            "This module is currently active in your Personal Hub."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = viewModel::dismissPlaceholderDialog) {
                    Text("Understood")
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
fun QuickActionsRow(onActionClick: (String) -> Unit) {
    val actions = listOf(
        "Tasks" to Icons.Default.Dashboard,
        "Documents" to Icons.Default.Lock,
        "Health" to Icons.Default.Favorite,
        "Expenses" to Icons.Default.AccountBalanceWallet,
        "Education" to Icons.Default.School
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(actions) { (title, icon) ->
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.clickable { onActionClick(title) }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun ModuleCardItem(
    module: AppModule,
    onClick: () -> Unit
) {
    val icon = getModuleIcon(module.id)

    AppCard(
        modifier = Modifier.clickable(onClick = onClick),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f).padding(end = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = module.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = module.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }

            ModuleStatusBadge(status = module.statusLabel)
        }
    }
}

fun getModuleIcon(id: ModuleId): ImageVector = when (id) {
    ModuleId.DASHBOARD -> Icons.Default.Dashboard
    ModuleId.PERSONAL_VAULT -> Icons.Default.Lock
    ModuleId.HEALTH_TRACKER -> Icons.Default.Favorite
    ModuleId.EDUCATION_HUB -> Icons.Default.School
    ModuleId.EXPENSE_TRACKER -> Icons.Default.AccountBalanceWallet
    ModuleId.SCREEN_TIME -> Icons.Default.Timer
    ModuleId.AI_ASSISTANT -> Icons.Default.Psychology
    ModuleId.CRM -> Icons.Default.Contacts
}

fun getGreeting(displayName: String?): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val timeGreeting = when (hour) {
        in 4..11 -> "Good Morning"
        in 12..16 -> "Good Afternoon"
        else -> "Good Evening"
    }
    val name = if (!displayName.isNullOrBlank()) ", ${displayName.split(" ").first()}" else ""
    return "$timeGreeting$name"
}

fun getFormattedDate(): String {
    val sdf = SimpleDateFormat("EEEE, MMM d", Locale.getDefault())
    return sdf.format(Date())
}
