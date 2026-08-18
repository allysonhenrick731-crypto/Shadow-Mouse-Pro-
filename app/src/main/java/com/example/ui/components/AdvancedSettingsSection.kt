package com.example.ui.components

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppThemeSetting
import com.example.ui.theme.*

@Composable
fun AdvancedSettingsSection(
    themeSetting: AppThemeSetting,
    onThemeChange: (AppThemeSetting) -> Unit,
    isOneHandMode: Boolean,
    onOneHandModeToggle: (Boolean) -> Unit,
    onOpenKeymaps: () -> Unit,
    onOpenCursorPicker: () -> Unit,
    onOpenTerminal: () -> Unit,
    onOpenAppManager: () -> Unit,
    onOpenVipMacro: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Configurações avançadas",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 17.sp
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 1. Theme Selector Section
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = FoxSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, FoxBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Tema escuro",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 15.sp
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Escolha seguir as configurações do sistema ou definir um tema.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF9CA3AF),
                        fontSize = 12.sp
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ThemeRadioOption(
                        title = "Seguir o sistema",
                        selected = themeSetting == AppThemeSetting.SYSTEM,
                        onClick = { onThemeChange(AppThemeSetting.SYSTEM) }
                    )
                    ThemeRadioOption(
                        title = "Modo claro",
                        selected = themeSetting == AppThemeSetting.LIGHT,
                        onClick = { onThemeChange(AppThemeSetting.LIGHT) }
                    )
                    ThemeRadioOption(
                        title = "Modo escuro",
                        selected = themeSetting == AppThemeSetting.DARK,
                        onClick = { onThemeChange(AppThemeSetting.DARK) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 2. Settings Items List
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = FoxSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, FoxBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                // One Hand Peripheral Mode
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Modo periférico com uma mão",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 15.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Jogar com tela sensível ao toque e periféricos.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF9CA3AF),
                                fontSize = 12.sp
                            )
                        )
                    }

                    Switch(
                        checked = isOneHandMode,
                        onCheckedChange = onOneHandModeToggle,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = NeonPurple,
                            uncheckedThumbColor = Color(0xFF9CA3AF),
                            uncheckedTrackColor = FoxSurfaceVariant
                        )
                    )
                }

                HorizontalDivider(color = FoxBorder.copy(alpha = 0.6f))

                // Manage Keymaps
                SettingsNavRow(
                    title = "Gerenciar mapas de chaves",
                    subtitle = "Exportar e importar mapas de chaves para backup ou compartilhamento.",
                    onClick = onOpenKeymaps
                )

                HorizontalDivider(color = FoxBorder.copy(alpha = 0.6f))

                // Custom Cursor
                SettingsNavRow(
                    title = "Cursor personalizado",
                    subtitle = "Escolha ou personalize o cursor do seu jogo favorito.",
                    onClick = onOpenCursorPicker
                )

                HorizontalDivider(color = FoxBorder.copy(alpha = 0.6f))

                // Command Terminal
                SettingsNavRow(
                    title = "Terminal de Comando",
                    subtitle = "Terminal conveniente para desenvolvedores e usuários avançados.",
                    onClick = onOpenTerminal
                )

                HorizontalDivider(color = FoxBorder.copy(alpha = 0.6f))

                // App Manager
                SettingsNavRow(
                    title = "Gerenciar aplicativos",
                    subtitle = "Gerenciar ou encerrar todos os aplicativos que não estão em execução",
                    onClick = onOpenAppManager
                )

                HorizontalDivider(color = FoxBorder.copy(alpha = 0.6f))

                // Macro Unlock VIP
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onOpenVipMacro)
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Desbloqueio de recurso macro",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 15.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.WorkspacePremium,
                                contentDescription = "VIP",
                                tint = GoldVIP,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Compre uma vez, desbloqueie o recurso macro para sempre",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = GoldVIP.copy(alpha = 0.9f),
                                fontSize = 12.sp
                            )
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Abrir",
                        tint = PurpleAccent.copy(alpha = 0.7f),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Community and Support Links (Just like Panda Mouse Pro)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FooterLinkItem(
                icon = Icons.Default.Language,
                label = "Site Oficial",
                onClick = {
                    Toast.makeText(context, "Abrindo Site Oficial do Shadow Mouse Pro", Toast.LENGTH_SHORT).show()
                }
            )

            FooterLinkItem(
                icon = Icons.Default.SmartDisplay,
                label = "Tutoriais em vídeo",
                onClick = {
                    Toast.makeText(context, "Abrindo Guia e Tutoriais no YouTube", Toast.LENGTH_SHORT).show()
                }
            )

            FooterLinkItem(
                icon = Icons.AutoMirrored.Filled.Send,
                label = "Juntar Telegram",
                onClick = {
                    Toast.makeText(context, "Acessando Comunidade Shadow no Telegram", Toast.LENGTH_SHORT).show()
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ThemeRadioOption(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = NeonPurple,
                unselectedColor = Color(0xFF9CA3AF)
            )
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall.copy(
                color = if (selected) Color.White else Color(0xFF9CA3AF),
                fontSize = 12.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
        )
    }
}

@Composable
private fun SettingsNavRow(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 15.sp
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF9CA3AF),
                    fontSize = 12.sp
                )
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Abrir",
            tint = PurpleAccent.copy(alpha = 0.7f),
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
private fun FooterLinkItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PurpleAccent,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}
