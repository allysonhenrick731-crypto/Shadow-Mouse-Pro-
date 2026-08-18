package com.example.ui.dialogs

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.ActivationMethodType
import com.example.model.ActivationState
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivationDialog(
    activationState: ActivationState,
    onActivateWireless: (port: String, code: String, onDone: () -> Unit) -> Unit,
    onActivateShizuku: (onDone: () -> Unit) -> Unit,
    onActivatePcAdb: (onDone: () -> Unit) -> Unit,
    onDeactivate: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Sem Fio, 1: Shizuku, 2: PC/Mac
    var isProcessing by remember { mutableStateOf(false) }

    var portInput by remember { mutableStateOf(activationState.pairingPort) }
    var codeInput by remember { mutableStateOf(activationState.pairingCode) }

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
            border = androidx.compose.foundation.BorderStroke(1.5.dp, NeonPurple.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Top Dialog Header
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
                                .background(NeonPurple.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = NeonPurple,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Central de Ativação",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                fontSize = 19.sp
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

                // If already active, show Active Banner with deactivate option
                if (activationState.isActivated) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = EmeraldActive.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldActive),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = EmeraldActive,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Shadow Engine Ativo!",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = "0ms input latency • Daemon PID 28419",
                                        color = EmeraldGlow,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            OutlinedButton(
                                onClick = {
                                    onDeactivate()
                                    Toast.makeText(context, "Mapeador desativado", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = AlertRed),
                                border = androidx.compose.foundation.BorderStroke(1.dp, AlertRed),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text("Desativar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // 3 Tabs inspired by Panda Mouse Pro
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
                                "Sem Fio (11+)",
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
                                "Shizuku",
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
                                "PC / Mac",
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == 2) PurpleAccent else Color(0xFF9CA3AF)
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tab Content Scrollable
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    when (selectedTab) {
                        0 -> WirelessDirectActivationTab(
                            port = portInput,
                            onPortChange = { portInput = it },
                            code = codeInput,
                            onCodeChange = { codeInput = it },
                            isProcessing = isProcessing,
                            onStartPairing = {
                                isProcessing = true
                                onActivateWireless(portInput, codeInput) {
                                    isProcessing = false
                                    Toast.makeText(context, "Ativação Sem Fio Realizada com Sucesso!", Toast.LENGTH_LONG).show()
                                    onDismiss()
                                }
                            }
                        )
                        1 -> ShizukuActivationTab(
                            isProcessing = isProcessing,
                            shizukuVersion = activationState.shizukuVersion,
                            onActivate = {
                                isProcessing = true
                                onActivateShizuku {
                                    isProcessing = false
                                    Toast.makeText(context, "Shadow Engine Ativado via Shizuku!", Toast.LENGTH_LONG).show()
                                    onDismiss()
                                }
                            }
                        )
                        2 -> PcAdbActivationTab(
                            isProcessing = isProcessing,
                            onActivateSimulation = {
                                isProcessing = true
                                onActivatePcAdb {
                                    isProcessing = false
                                    Toast.makeText(context, "Ativado via ADB com sucesso!", Toast.LENGTH_LONG).show()
                                    onDismiss()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WirelessDirectActivationTab(
    port: String,
    onPortChange: (String) -> Unit,
    code: String,
    onCodeChange: (String) -> Unit,
    isProcessing: Boolean,
    onStartPairing: () -> Unit
) {
    Column {
        // Yellow/Gold Card (Like Screenshot 4)
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFEAB308).copy(alpha = 0.15f),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEAB308)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "⚡ Ativação Direta (Android 11+)",
                    color = Color(0xFFFDE047),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ative diretamente no celular usando a Depuração por Wi-Fi, sem precisar de PC!",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Passo a Passo:",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        InstructionStep(number = "1", text = "Abra Configurações do Celular > Opções do Desenvolvedor")
        InstructionStep(number = "2", text = "Ative 'Depuração sem fio' e toque em 'Parear dispositivo com código'")
        InstructionStep(number = "3", text = "Digite a Porta e o Código de pareamento abaixo:")

        Spacer(modifier = Modifier.height(12.dp))

        // Input Fields
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
                value = port,
                onValueChange = onPortChange,
                label = { Text("Porta (Ex: 41235)") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonPurple,
                    unfocusedBorderColor = FoxBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.weight(1f),
                singleLine = true
            )

            OutlinedTextField(
                value = code,
                onValueChange = onCodeChange,
                label = { Text("Código (6 dígitos)") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonPurple,
                    unfocusedBorderColor = FoxBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.weight(1.2f),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = onStartPairing,
            enabled = !isProcessing && port.isNotEmpty() && code.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            if (isProcessing) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Pareando & Injetando Daemon...", color = Color.White, fontWeight = FontWeight.Bold)
            } else {
                Icon(imageVector = Icons.Default.FlashOn, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Parear e Ativar Agora", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ShizukuActivationTab(
    isProcessing: Boolean,
    shizukuVersion: String,
    onActivate: () -> Unit
) {
    val context = LocalContext.current

    Column {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF6366F1).copy(alpha = 0.15f),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF6366F1)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF6366F1)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("S", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Shizuku Manager",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Status: Serviço Detectado ($shizukuVersion)",
                            color = EmeraldGlow,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Vantagens da Ativação Shizuku:",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        InstructionStep(number = "✓", text = "Ativação em 1 toque sem reiniciar")
        InstructionStep(number = "✓", text = "Acesso direto à API de entrada Shell para latência ultra-baixa")
        InstructionStep(number = "✓", text = "Reativação automática ao ligar o celular")

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onActivate,
            enabled = !isProcessing,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            if (isProcessing) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Iniciando via Shizuku Binder...", color = Color.White)
            } else {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ativar Shadow com Shizuku", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun PcAdbActivationTab(
    isProcessing: Boolean,
    onActivateSimulation: () -> Unit
) {
    val context = LocalContext.current
    val scriptCommand = "adb shell sh /sdcard/Android/data/com.aistudio.foxmapper/activate.sh"

    Column {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF3B82F6).copy(alpha = 0.15f),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF3B82F6)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "💻 Ativação por Computador (PC / Mac)",
                    color = Color(0xFF93C5FD),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Método tradicional via cabo USB e script ADB.",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Comando ADB Oficial:",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(6.dp))

        // Code block container
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = FoxSurfaceVariant,
            border = androidx.compose.foundation.BorderStroke(1.dp, FoxBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = scriptCommand,
                    color = CyanGlow,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Shadow ADB Script", scriptCommand)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "Comando copiado para a Área de Transferência!", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copiar",
                        tint = PurpleAccent
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        InstructionStep(number = "1", text = "Conecte o celular ao PC via Cabo USB")
        InstructionStep(number = "2", text = "Abra o CMD / Terminal no computador")
        InstructionStep(number = "3", text = "Cole o comando acima e pressione ENTER")

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = onActivateSimulation,
            enabled = !isProcessing,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            if (isProcessing) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Verificando conexão ADB...", color = Color.White)
            } else {
                Icon(imageVector = Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Verificar Ativação do PC", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun InstructionStep(number: String, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(NeonPurple.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                color = PurpleAccent,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 12.sp
        )
    }
}
