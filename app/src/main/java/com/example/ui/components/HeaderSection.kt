package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ActivationState
import com.example.ui.theme.*

@Composable
fun HeaderSection(
    activationState: ActivationState,
    onOpenActivation: () -> Unit,
    onOpenVip: () -> Unit,
    onOpenTerminal: () -> Unit
) {
    val isActivated = activationState.isActivated

    val headerGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF2C0A4A),
            Color(0xFF130424),
            FoxBlack
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(headerGradient)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Top App Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Fox Logo + App Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onOpenActivation() }
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(NeonPurple, DeepPurple, FoxBlack)
                            )
                        )
                        .border(1.5.dp, if (isActivated) EmeraldActive else NeonPurple, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🦊",
                        fontSize = 24.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Shadow Mouse Pro",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = NeonPurple.copy(alpha = 0.25f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NeonPurple)
                        ) {
                            Text(
                                text = "9.5",
                                color = PurpleAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(if (isActivated) EmeraldActive else AlertRed)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = if (isActivated) "Engine Ativado (0ms)" else "Aguardando Ativação",
                            color = if (isActivated) EmeraldGlow else Color(0xFFC084FC),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Quick Actions
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Terminal Shortcut
                IconButton(
                    onClick = onOpenTerminal,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(FoxSurfaceVariant)
                ) {
                    Icon(
                        imageVector = Icons.Default.Terminal,
                        contentDescription = "Terminal",
                        tint = CyanGlow,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // VIP Badge Button
                IconButton(
                    onClick = onOpenVip,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(GoldVIP.copy(alpha = 0.15f))
                        .border(1.dp, GoldVIP.copy(alpha = 0.4f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = "VIP Macro",
                        tint = GoldVIP,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
