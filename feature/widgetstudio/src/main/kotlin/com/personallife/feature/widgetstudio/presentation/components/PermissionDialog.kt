package com.personallife.feature.widgetstudio.presentation.components

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.personallife.core.widgetengine.model.WidgetPermissionRequirement
import com.personallife.core.widgetpreview.components.LiquidGlassButton

@Composable
fun PermissionExplanationDialog(
    requirement: WidgetPermissionRequirement,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            LiquidGlassButton(
                text = "Open Settings",
                icon = Icons.Default.OpenInNew,
                onClick = {
                    openPermissionSettings(context, requirement)
                    onDismiss()
                },
                accentColor = Color(0xFFF59E0B)
            )
        },
        dismissButton = {
            LiquidGlassButton(
                text = "Cancel",
                onClick = onDismiss,
                accentColor = Color(0xFF94A3B8)
            )
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = Color(0xFFF59E0B)
                )
                Text(
                    text = "Permission Required",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${requirement.label} is required for this widget to query system metrics.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFCBD5E1)
                )
                Text(
                    text = "Personal Hub operates entirely on-device with zero external data collection. Please grant access in your device settings to activate live stats.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF94A3B8)
                )
            }
        },
        containerColor = Color(0xFF1E293B),
        shape = RoundedCornerShape(26.dp)
    )
}

private fun openPermissionSettings(context: Context, requirement: WidgetPermissionRequirement) {
    try {
        val intent = when (requirement) {
            WidgetPermissionRequirement.USAGE_STATS -> Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            WidgetPermissionRequirement.MEDIA_SESSION -> Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
            WidgetPermissionRequirement.LOCATION -> Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            else -> Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = android.net.Uri.fromParts("package", context.packageName, null)
            }
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (_: Exception) {
        // Fallback to application settings
        val fallback = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = android.net.Uri.fromParts("package", context.packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(fallback)
    }
}
