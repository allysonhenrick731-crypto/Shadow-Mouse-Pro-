package com.example.ui.dialogs

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.GameItem
import com.example.model.KeyType
import com.example.model.KeybindItem
import com.example.ui.theme.*
import kotlin.math.roundToInt

@Composable
fun KeymapperOverlayDialog(
    game: GameItem,
    keybinds: List<KeybindItem>,
    selectedCursor: String,
    onUpdateKeyPosition: (id: String, xPercent: Float, yPercent: Float) -> Unit,
    onAddKey: (label: String, action: String) -> Unit,
    onRemoveKey: (id: String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var isEditMode by remember { mutableStateOf(false) }
    var showFloatingMenu by remember { mutableStateOf(false) }
    var keyOpacity by remember { mutableFloatStateOf(0.85f) }
    var mouseAimLocked by remember { mutableStateOf(true) }
    var currentFps by remember { mutableIntStateOf(120) }

    // Simulate FPS fluctuation
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(1000)
            currentFps = (115..120).random()
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF07040C))
        ) {
            val screenWidth = maxWidth
            val screenHeight = maxHeight

            // 1. Simulated Game Background Canvas with subtle shooter HUD grid
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFF1E0E35), Color(0xFF090312)),
                            radius = 900f
                        )
                    )
            )

            // Crosshair in the middle of screen
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (selectedCursor == "cursor_panda_classic") "🦊" else if (selectedCursor == "cursor_gold_dragon") "🐉" else "🎯",
                        fontSize = 32.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (mouseAimLocked) "[ MOUSE TRAVADO - MIRA ATIVA ]" else "[ MOUSE LIVRE ] (Pressione Botão Direito)",
                        color = if (mouseAimLocked) EmeraldGlow else AmberWarning,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(FoxBlack.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // 2. Top Gamer Status Bar
            Surface(
                color = FoxBlack.copy(alpha = 0.85f),
                border = androidx.compose.foundation.BorderStroke(1.dp, FoxBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Game & Engine info
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = game.iconEmoji, fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = game.name,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = EmeraldActive.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "$currentFps FPS • 1ms Latency",
                                color = EmeraldGlow,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // Mode Actions
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        FilterChip(
                            selected = isEditMode,
                            onClick = {
                                isEditMode = !isEditMode
                                Toast.makeText(
                                    context,
                                    if (isEditMode) "Modo Edição: Arraste as teclas para reposicionar" else "Modo Jogo Ativado",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            label = {
                                Text(
                                    if (isEditMode) "Salvar Posições" else "Editar Teclas",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = if (isEditMode) Icons.Default.Check else Icons.Default.Edit,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = EmeraldActive,
                                selectedLabelColor = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(AlertRed.copy(alpha = 0.2f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Sair do Jogo",
                                tint = AlertRed,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // 3. Render all interactive On-Screen Keybinds (WASD, Buttons, Aim, Fire)
            keybinds.forEach { item ->
                DraggableKeybindItem(
                    item = item,
                    screenWidth = screenWidth.value,
                    screenHeight = screenHeight.value,
                    isEditMode = isEditMode,
                    opacity = keyOpacity,
                    onPositionChanged = { newX, newY ->
                        onUpdateKeyPosition(item.id, newX, newY)
                    },
                    onTap = {
                        if (!isEditMode) {
                            if (item.keyType == KeyType.MOUSE_AIM) {
                                mouseAimLocked = !mouseAimLocked
                            }
                            Toast.makeText(context, "Tecla [${item.keyLabel}]: ${item.actionName}", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }

            // 4. Floating Shadow Fox Action Ball (Can be tapped to toggle HUD controls)
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 56.dp, end = 16.dp)
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    // Glowing Ball
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(NeonPurple, DeepPurple, FoxBlack)
                                )
                            )
                            .border(2.dp, if (showFloatingMenu) EmeraldActive else PurpleAccent, CircleShape)
                            .clickable { showFloatingMenu = !showFloatingMenu },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🦊", fontSize = 24.sp)
                    }

                    // Floating Menu Expanded
                    AnimatedVisibility(visible = showFloatingMenu) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = FoxSurface.copy(alpha = 0.95f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NeonPurple),
                            modifier = Modifier
                                .width(220.dp)
                                .padding(top = 8.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "Menu Shadow 9.5",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                // Opacity slider
                                Text(
                                    text = "Opacidade das Teclas: ${(keyOpacity * 100).toInt()}%",
                                    color = PurpleAccent,
                                    fontSize = 11.sp
                                )
                                Slider(
                                    value = keyOpacity,
                                    onValueChange = { keyOpacity = it },
                                    valueRange = 0.2f..1f,
                                    colors = SliderDefaults.colors(
                                        thumbColor = NeonPurple,
                                        activeTrackColor = NeonPurple
                                    )
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                // Add Key quick
                                Button(
                                    onClick = {
                                        onAddKey("G", "Granada")
                                        Toast.makeText(context, "Nova tecla [G] adicionada no centro!", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = FoxSurfaceVariant),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("+ Nova Tecla", fontSize = 12.sp, color = Color.White)
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Button(
                                    onClick = {
                                        mouseAimLocked = !mouseAimLocked
                                        Toast.makeText(context, if (mouseAimLocked) "Mira Travada" else "Cursor Livre", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = DeepPurple),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(imageVector = Icons.Default.Mouse, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(if (mouseAimLocked) "Destravar Mouse" else "Travar Mira", fontSize = 12.sp, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DraggableKeybindItem(
    item: KeybindItem,
    screenWidth: Float,
    screenHeight: Float,
    isEditMode: Boolean,
    opacity: Float,
    onPositionChanged: (Float, Float) -> Unit,
    onTap: () -> Unit
) {
    var offsetX by remember(item.xPercent) { mutableFloatStateOf(item.xPercent * screenWidth) }
    var offsetY by remember(item.yPercent) { mutableFloatStateOf(item.yPercent * screenHeight) }

    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .pointerInput(isEditMode) {
                if (isEditMode) {
                    detectDragGestures(
                        onDragEnd = {
                            val newXPercent = (offsetX / screenWidth).coerceIn(0.05f, 0.95f)
                            val newYPercent = (offsetY / screenHeight).coerceIn(0.05f, 0.95f)
                            onPositionChanged(newXPercent, newYPercent)
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                }
            }
            .clickable { onTap() }
    ) {
        when (item.keyType) {
            KeyType.DPAD_WASD -> {
                // WASD Joystick D-Pad box
                Box(
                    modifier = Modifier
                        .size(86.dp)
                        .clip(CircleShape)
                        .background(DeepPurple.copy(alpha = opacity * 0.5f))
                        .border(1.5.dp, NeonPurple.copy(alpha = opacity), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "▲ W",
                            color = Color.White.copy(alpha = opacity),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "◄ A", color = Color.White.copy(alpha = opacity), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = "D ►", color = Color.White.copy(alpha = opacity), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(
                            text = "▼ S",
                            color = Color.White.copy(alpha = opacity),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            KeyType.MOUSE_AIM, KeyType.MOUSE_FIRE -> {
                // Mouse Action Button
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(
                            if (item.keyType == KeyType.MOUSE_FIRE) AlertRed.copy(alpha = opacity * 0.6f)
                            else ElectricViolet.copy(alpha = opacity * 0.6f)
                        )
                        .border(
                            1.5.dp,
                            if (item.keyType == KeyType.MOUSE_FIRE) AlertRedGlow else PurpleAccent,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = if (item.keyType == KeyType.MOUSE_FIRE) Icons.Default.GpsFixed else Icons.Default.Mouse,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = opacity),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = item.keyLabel,
                            color = Color.White.copy(alpha = opacity),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
            else -> {
                // Standard Keybind Button (Space, C, R, 1, 2, etc.)
                Box(
                    modifier = Modifier
                        .size(if (item.keyLabel.length > 2) 58.dp else 40.dp, 40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF1F1B24), Color(0xFF100E14))
                            )
                        )
                        .border(1.5.dp, NeonPurple.copy(alpha = opacity * 0.8f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.keyLabel,
                        color = Color.White.copy(alpha = opacity),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = if (item.keyLabel.length > 3) 10.sp else 13.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
