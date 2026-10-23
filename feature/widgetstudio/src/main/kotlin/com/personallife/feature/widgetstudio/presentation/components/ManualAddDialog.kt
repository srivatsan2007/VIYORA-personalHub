package com.personallife.feature.widgetstudio.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personallife.core.widgetpreview.components.LiquidGlassButton

@Composable
fun ManualAddDialog(
    widgetName: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            LiquidGlassButton(
                text = "Got It",
                icon = Icons.Default.Check,
                onClick = onDismiss,
                accentColor = Color(0xFFA3E635)
            )
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.TouchApp,
                    contentDescription = null,
                    tint = Color(0xFFA3E635)
                )
                Text(
                    text = "Add to Home Screen",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "If your Android launcher doesn't allow automatic widget pinning, you can place '$widgetName' manually:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFCBD5E1)
                )

                InstructionStep(
                    stepNumber = "1",
                    instruction = "Go to your Android Home Screen."
                )
                InstructionStep(
                    stepNumber = "2",
                    instruction = "Touch and hold any empty area of the screen."
                )
                InstructionStep(
                    stepNumber = "3",
                    instruction = "Tap 'Widgets' from the pop-up menu."
                )
                InstructionStep(
                    stepNumber = "4",
                    instruction = "Scroll to 'Personal Life Hub' and drag '$widgetName' to your desired space."
                )
            }
        },
        containerColor = Color(0xFF1E293B),
        shape = RoundedCornerShape(26.dp)
    )
}

@Composable
private fun InstructionStep(
    stepNumber: String,
    instruction: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(Color(0xFF38BDF8).copy(alpha = 0.2f)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stepNumber,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF38BDF8),
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = instruction,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFE2E8F0)
        )
    }
}
