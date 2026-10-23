package com.personallife.feature.modulemanager.presentation

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personallife.core.designsystem.component.AppCard
import com.personallife.core.designsystem.component.LoadingStateView
import com.personallife.core.designsystem.component.ModuleStatusBadge
import com.personallife.core.model.AppModule
import com.personallife.core.model.ModuleId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModuleManagerScreen(
    viewModel: ModuleManagerViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isLoading) {
        LoadingStateView(message = "Loading modules...")
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Module Manager") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tailor Your Hub",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Enable or disable modules to personalize your dashboard spaces. Disabled modules will not appear in navigation or daily overviews.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            items(uiState.modules, key = { it.id.key }) { module ->
                ModuleToggleItem(
                    module = module,
                    onToggle = { isChecked ->
                        viewModel.toggleModule(module.id, isChecked)
                    },
                    onInfoClick = {
                        viewModel.selectModuleInfo(module)
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // Module Details Dialog
    uiState.selectedInfoModule?.let { module ->
        AlertDialog(
            onDismissRequest = viewModel::dismissModuleInfo,
            title = {
                Text(
                    text = module.title,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Category: ${module.category}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        ModuleStatusBadge(status = module.statusLabel)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = module.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (!module.isImplemented) {
                            "Status: Planned Feature. This module's data contracts and settings are ready in Phase 1. Implementation is scheduled for Phase 3/4."
                        } else {
                            "Status: Fully implemented and integrated into your Personal Hub."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = viewModel::dismissModuleInfo) {
                    Text("Close")
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
fun ModuleToggleItem(
    module: AppModule,
    onToggle: (Boolean) -> Unit,
    onInfoClick: () -> Unit
) {
    val icon = getModuleIcon(module.id)
    val isCoreDashboard = module.id == ModuleId.DASHBOARD

    AppCard(modifier = Modifier.clickable(onClick = onInfoClick)) {
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = module.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        ModuleStatusBadge(status = module.statusLabel)
                    }
                    Text(
                        text = module.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2
                    )
                }
            }

            if (!isCoreDashboard) {
                Switch(
                    checked = module.isEnabled,
                    onCheckedChange = onToggle
                )
            }
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
