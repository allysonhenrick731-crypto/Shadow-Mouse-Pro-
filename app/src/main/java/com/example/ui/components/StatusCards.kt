package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ActivationState
import com.example.model.PeripheralDevice
import com.example.ui.theme.*

@Composable
fun StatusCardsSection(
    activationState: ActivationState,
    devices: List<PeripheralDevice>,
    onOpenActivation: () -> Unit,
    onOpenDevices: () -> Unit
) {
    val isActivated = activationState.isActivated
    val connectedDevices = devices.filter { it.isConnected }
    val hasDevices = connectedDevices.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        // Combined Status Container with sleek border
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = FoxSurface,
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                Brush.linearGradient(
                    listOf(
                        if (isActivated && hasDevices) EmeraldActive.copy(alpha = 0.5f) else FoxBorder,
                        NeonPurple.copy(alpha = 0.3f)
                    )
                )
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                // 1. Device Connection Status Item
                StatusItemRow(
                    icon = if (hasDevices) Icons.Default.SportsEsports else Icons.Default.Warning,
                    iconTint = if (hasDevices) ElectricViolet else AlertRed,
                    iconBg = if (hasDevices) ElectricViolet.copy(alpha = 0.15f) else AlertRed.copy(alpha = 0.15f),
                    title = if (hasDevices) {
                        "${connectedDevices.size} Dispositivo(s) Conectado(s)"
                    } else {
                        "Nenhum dispositivo conectado"
                    },
                    subtitle = if (hasDevices) {
                        connectedDevices.joinToString(" • ") { it.name.substringBefore("(") }
                    } else {
                        "Toque para conectar via USB OTG ou Bluetooth"
                    },
                    isSuccess = hasDevices,
                    onClick = onOpenDevices
                )

                HorizontalDivider(
                    color = FoxBorder.copy(alpha = 0.6f),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                // 2. Activation Status Item
                StatusItemRow(
                    icon = if (isActivated) Icons.Default.Bolt else Icons.Default.Warning,
                    iconTint = if (isActivated) EmeraldActive else AlertRed,
                    iconBg = if (isActivated) EmeraldActive.copy(alpha = 0.15f) else AlertRed.copy(alpha = 0.15f),
                    title = if (isActivated) {
                        activationState.statusMessage
                    } else {
                        "Precisa de ativação"
                    },
                    subtitle = if (isActivated) {
                        "Mapeador pronto para jogos (0ms input lag)"
                    } else {
                        "Ativar via Sem Fio (Android 11+), Shizuku ou PC"
                    },
                    isSuccess = isActivated,
                    onClick = onOpenActivation
                )
            }
        }
    }
}

@Composable
private fun StatusItemRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    iconBg: Color,
    title: String,
    subtitle: String,
    isSuccess: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Status Icon Circle
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconBg)
                .border(1.dp, iconTint.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Title and Subtitle
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isSuccess) Color.White else Color(0xFFFCA5A5),
                    fontSize = 15.sp
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = if (isSuccess) EmeraldGlow.copy(alpha = 0.8f) else Color(0xFF9CA3AF),
                    fontSize = 12.sp
                ),
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Right Arrow
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Abrir",
            tint = PurpleAccent.copy(alpha = 0.7f),
            modifier = Modifier.size(22.dp)
        )
    }
}
