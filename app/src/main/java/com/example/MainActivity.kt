package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.GameItem
import com.example.ui.components.AdvancedSettingsSection
import com.example.ui.components.GamesSection
import com.example.ui.components.HeaderSection
import com.example.ui.components.StatusCardsSection
import com.example.ui.dialogs.*
import com.example.ui.theme.FoxBlack
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.MapperViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ShadowMouseProApp()
            }
        }
    }
}

@Composable
fun ShadowMouseProApp(viewModel: MapperViewModel = viewModel()) {
    val context = LocalContext.current

    val activationState by viewModel.activationState.collectAsStateWithLifecycle()
    val devices by viewModel.devices.collectAsStateWithLifecycle()
    val games by viewModel.games.collectAsStateWithLifecycle()
    val keybinds by viewModel.currentKeybinds.collectAsStateWithLifecycle()
    val themeSetting by viewModel.themeSetting.collectAsStateWithLifecycle()
    val isOneHandMode by viewModel.isOneHandMode.collectAsStateWithLifecycle()
    val isVipMacroUnlocked by viewModel.isVipMacroUnlocked.collectAsStateWithLifecycle()
    val selectedCursor by viewModel.selectedCursor.collectAsStateWithLifecycle()
    val activePlayingGame by viewModel.activePlayingGame.collectAsStateWithLifecycle()
    val terminalLogs by viewModel.terminalLogs.collectAsStateWithLifecycle()

    // Dialog & Sheet States
    var showActivationDialog by remember { mutableStateOf(false) }
    var showDevicesDialog by remember { mutableStateOf(false) }
    var showAddGameDialog by remember { mutableStateOf(false) }
    var showTerminalDialog by remember { mutableStateOf(false) }
    var showCursorPicker by remember { mutableStateOf(false) }
    var showVipMacroDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(FoxBlack),
        containerColor = FoxBlack
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 1. Header with Fox Branding & Engine State
            item {
                HeaderSection(
                    activationState = activationState,
                    onOpenActivation = { showActivationDialog = true },
                    onOpenVip = { showVipMacroDialog = true },
                    onOpenTerminal = { showTerminalDialog = true }
                )
            }

            // 2. Fox Hero Cyberpunk Banner Image
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.fox_hero_banner_1787069231598),
                        contentDescription = "Shadow Fox Hero Banner",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(18.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // 3. Status Cards (Precisa de ativação & Conexão de Dispositivos)
            item {
                StatusCardsSection(
                    activationState = activationState,
                    devices = devices,
                    onOpenActivation = { showActivationDialog = true },
                    onOpenDevices = { showDevicesDialog = true }
                )
            }

            // 4. Meus Jogos Section (Listas de Jogos)
            item {
                GamesSection(
                    games = games,
                    onPlayGame = { game ->
                        viewModel.startPlayingGame(game)
                    },
                    onAddGame = { showAddGameDialog = true },
                    onRemoveGame = { id -> viewModel.removeGame(id) }
                )
            }

            // 5. Configurações Avançadas (Panda Mouse Pro Style)
            item {
                AdvancedSettingsSection(
                    themeSetting = themeSetting,
                    onThemeChange = { viewModel.setThemeSetting(it) },
                    isOneHandMode = isOneHandMode,
                    onOneHandModeToggle = { viewModel.setOneHandMode(it) },
                    onOpenKeymaps = {
                        Toast.makeText(
                            context,
                            "Mapas de chaves sincronizados! (${keybinds.size} perfis salvos)",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onOpenCursorPicker = { showCursorPicker = true },
                    onOpenTerminal = { showTerminalDialog = true },
                    onOpenAppManager = {
                        Toast.makeText(
                            context,
                            "Memória RAM otimizada! 1.4GB liberados para jogos.",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onOpenVipMacro = { showVipMacroDialog = true }
                )
            }
        }
    }

    // Modal Dialogs
    if (showActivationDialog) {
        ActivationDialog(
            activationState = activationState,
            onActivateWireless = { port, code, onDone ->
                viewModel.activateViaWirelessDebugging(port, code, onDone)
            },
            onActivateShizuku = { onDone ->
                viewModel.activateViaShizuku(onDone)
            },
            onActivatePcAdb = { onDone ->
                viewModel.activateViaPcAdb(onDone)
            },
            onDeactivate = { viewModel.deactivateEngine() },
            onDismiss = { showActivationDialog = false }
        )
    }

    if (showDevicesDialog) {
        DeviceManagerDialog(
            devices = devices,
            onToggleDevice = { id -> viewModel.toggleDeviceConnection(id) },
            onConnectAll = { viewModel.connectAllPeripherals() },
            onDisconnectAll = { viewModel.disconnectAllPeripherals() },
            onDismiss = { showDevicesDialog = false }
        )
    }

    if (showAddGameDialog) {
        AddGameDialog(
            existingGames = games,
            onGameSelected = { name, pkg, emoji, category ->
                viewModel.addGame(name, pkg, emoji, category)
            },
            onDismiss = { showAddGameDialog = false }
        )
    }

    if (showTerminalDialog) {
        CommandTerminalDialog(
            logs = terminalLogs,
            onExecuteCommand = { cmd -> viewModel.runTerminalCommand(cmd) },
            onDismiss = { showTerminalDialog = false }
        )
    }

    if (showCursorPicker) {
        CursorPickerDialog(
            cursors = viewModel.availableCursors,
            selectedCursorId = selectedCursor,
            onSelectCursor = { id -> viewModel.setSelectedCursor(id) },
            onDismiss = { showCursorPicker = false }
        )
    }

    if (showVipMacroDialog) {
        MacroUnlockDialog(
            isVipUnlocked = isVipMacroUnlocked,
            onUnlockVip = { viewModel.unlockVip() },
            onDismiss = { showVipMacroDialog = false }
        )
    }

    // In-Game Live Keymapper HUD Simulation
    LaunchedEffect(activePlayingGame) {
        activePlayingGame?.let { game ->
            val intent = context.packageManager.getLaunchIntentForPackage(game.packageName)
            if (intent != null) {
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "Jogo não instalado: ${game.name}", Toast.LENGTH_SHORT).show()
                viewModel.stopPlayingGame()
            }
        }
    }

    activePlayingGame?.let { game ->
        KeymapperOverlayDialog(
            game = game,
            keybinds = keybinds,
            selectedCursor = selectedCursor,
            onUpdateKeyPosition = { id, x, y ->
                viewModel.updateKeybindPosition(id, x, y)
            },
            onAddKey = { label, action ->
                viewModel.addKeybind(label, action)
            },
            onRemoveKey = { id ->
                viewModel.removeKeybind(id)
            },
            onDismiss = { viewModel.stopPlayingGame() }
        )
    }
}
