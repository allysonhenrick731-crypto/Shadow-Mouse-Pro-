package com.example.ui.dialogs

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.ConnectionType
import com.example.model.DeviceType
import com.example.model.PeripheralDevice
import com.example.ui.theme.*

@Composable
fun DeviceManagerDialog(
    devices: List<PeripheralDevice>,
    onToggleDevice: (String) -> Unit,
    onConnectAll: () -> Unit,
    onDisconnectAll: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) } // 0: USB OTG, 1: Bluetooth, 2: Calibração

    var mouseDpi by remember { mutableIntStateOf(2400) }
    var pollingRate by remember { mutableIntStateOf(1000) }
    var sensX by remember { mutableFloatStateOf(85f) }
    var sensY by remember { mutableFloatStateOf(80f) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.9f)
                .clip(RoundedCornerShape(24.dp)),
            color = FoxDarkBackground,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, ElectricViolet.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(ElectricViolet.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SportsEsports,
                                contentDescription = null,
                                tint = ElectricViolet,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Gerenciador de Periféricos",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Tabs
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = FoxSurface,
                    contentColor = NeonPurple,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = NeonPurple
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Text(
                                "USB OTG",
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == 0) PurpleAccent else Color(0xFF9CA3AF)
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Text(
                                "Bluetooth",
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == 1) PurpleAccent else Color(0xFF9CA3AF)
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = {
                            Text(
                                "DPI & Ajustes",
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == 2) PurpleAccent else Color(0xFF9CA3AF)
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tab Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    when (selectedTab) {
                        0 -> {
                            val usbDevices = devices.filter { it.connectionType == ConnectionType.USB_OTG }
                            DeviceListTab(
                                devices = usbDevices,
                                title = "Dispositivos USB OTG",
                                subtitle = "Conecte seu HUB USB, Teclado ou Mouse diretamente no cabo OTG.",
                                onToggle = onToggleDevice
                            )
                        }
                        1 -> {
                            val btDevices = devices.filter { it.connectionType == ConnectionType.BLUETOOTH }
                            DeviceListTab(
                                devices = btDevices,
                                title = "Dispositivos Bluetooth",
                                subtitle = "Emparelhe mouses, teclados sem fio ou controles Bluetooth.",
                                onToggle = onToggleDevice
                            )
                        }
                        2 -> {
                            CalibrationTab(
                                mouseDpi = mouseDpi,
                                onDpiChange = { mouseDpi = it },
                                pollingRate = pollingRate,
                                onPollingChange = { pollingRate = it },
                                sensX = sensX,
                                onSensXChange = { sensX = it },
                                sensY = sensY,
                                onSensYChange = { sensY = it }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Bottom Action Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            onDisconnectAll()
                            Toast.makeText(context, "Todos periféricos desconectados", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AlertRed),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AlertRed),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Desconectar", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            onConnectAll()
                            Toast.makeText(context, "Periféricos conectados com taxa de 1000Hz!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1.3f)
                    ) {
                        Icon(imageVector = Icons.Default.FlashOn, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Conectar Todos", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun DeviceListTab(
    devices: List<PeripheralDevice>,
    title: String,
    subtitle: String,
    onToggle: (String) -> Unit
) {
    Column {
        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = subtitle,
            color = Color(0xFF9CA3AF),
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        if (devices.isEmpty()) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = FoxSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Nenhum dispositivo encontrado. Conecte o cabo OTG ou ative o Bluetooth.",
                    color = Color(0xFF9CA3AF),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            devices.forEach { dev ->
                DeviceItemCard(device = dev, onToggle = { onToggle(dev.id) })
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun DeviceItemCard(
    device: PeripheralDevice,
    onToggle: () -> Unit
) {
    val isConnected = device.isConnected

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = FoxSurface,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isConnected) EmeraldActive.copy(alpha = 0.6f) else FoxBorder
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isConnected) EmeraldActive.copy(alpha = 0.2f) else FoxSurfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (device.type) {
                            DeviceType.MOUSE -> Icons.Default.Mouse
                            DeviceType.KEYBOARD -> Icons.Default.Keyboard
                            DeviceType.GAMEPAD -> Icons.Default.SportsEsports
                            DeviceType.USB_HUB -> Icons.Default.Usb
                        },
                        contentDescription = null,
                        tint = if (isConnected) EmeraldActive else Color(0xFF9CA3AF),
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = device.name,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (isConnected) {
                            "Ativo • ${device.pollingRateHz}Hz • ${device.latencyMs}ms delay" +
                                    (if (device.batteryPercent != null) " • 🔋 ${device.batteryPercent}%" else "")
                        } else {
                            "Desconectado • Toque para emparelhar"
                        },
                        color = if (isConnected) EmeraldGlow else Color(0xFF9CA3AF),
                        fontSize = 11.sp
                    )
                }
            }

            Switch(
                checked = isConnected,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = EmeraldActive,
                    uncheckedThumbColor = Color(0xFF9CA3AF),
                    uncheckedTrackColor = FoxSurfaceVariant
                )
            )
        }
    }
}

@Composable
private fun CalibrationTab(
    mouseDpi: Int,
    onDpiChange: (Int) -> Unit,
    pollingRate: Int,
    onPollingChange: (Int) -> Unit,
    sensX: Float,
    onSensXChange: (Float) -> Unit,
    sensY: Float,
    onSensYChange: (Float) -> Unit
) {
    Column {
        Text(
            text = "Taxa de Atualização (Polling Rate):",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(125, 500, 1000).forEach { rate ->
                FilterChip(
                    selected = pollingRate == rate,
                    onClick = { onPollingChange(rate) },
                    label = { Text("${rate}Hz", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeonPurple,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "DPI do Sensor do Mouse: ${mouseDpi} DPI",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf(800, 1200, 1600, 2400, 3200).forEach { dpiVal ->
                FilterChip(
                    selected = mouseDpi == dpiVal,
                    onClick = { onDpiChange(dpiVal) },
                    label = { Text("$dpiVal", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ElectricViolet,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sensitivity Sliders
        Text(
            text = "Sensibilidade Eixo X (Horizontal): ${sensX.toInt()}%",
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
        Slider(
            value = sensX,
            onValueChange = onSensXChange,
            valueRange = 10f..100f,
            colors = SliderDefaults.colors(
                thumbColor = NeonPurple,
                activeTrackColor = NeonPurple,
                inactiveTrackColor = FoxSurfaceVariant
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Sensibilidade Eixo Y (Vertical): ${sensY.toInt()}%",
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
        Slider(
            value = sensY,
            onValueChange = onSensYChange,
            valueRange = 10f..100f,
            colors = SliderDefaults.colors(
                thumbColor = ElectricViolet,
                activeTrackColor = ElectricViolet,
                inactiveTrackColor = FoxSurfaceVariant
            )
        )
    }
}
