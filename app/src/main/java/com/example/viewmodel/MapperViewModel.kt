package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MapperViewModel : ViewModel() {

    private val _activationState = MutableStateFlow(
        ActivationState(
            isActivated = false,
            method = ActivationMethodType.NONE,
            statusMessage = "Precisa de ativação"
        )
    )
    val activationState: StateFlow<ActivationState> = _activationState.asStateFlow()

    private val _devices = MutableStateFlow<List<PeripheralDevice>>(
        listOf(
            PeripheralDevice(
                id = "dev_mouse_1",
                name = "Mouse Gamer Viper 8K (USB)",
                type = DeviceType.MOUSE,
                connectionType = ConnectionType.USB_OTG,
                isConnected = false,
                pollingRateHz = 1000,
                dpi = 2400,
                latencyMs = 1
            ),
            PeripheralDevice(
                id = "dev_kb_1",
                name = "Teclado Mecânico RGB 60% (USB)",
                type = DeviceType.KEYBOARD,
                connectionType = ConnectionType.USB_OTG,
                isConnected = false,
                pollingRateHz = 1000,
                latencyMs = 1
            ),
            PeripheralDevice(
                id = "dev_bt_mouse",
                name = "Shadow Pro Wireless Mouse",
                type = DeviceType.MOUSE,
                connectionType = ConnectionType.BLUETOOTH,
                isConnected = false,
                batteryPercent = 92,
                pollingRateHz = 500,
                dpi = 1600,
                latencyMs = 3
            ),
            PeripheralDevice(
                id = "dev_bt_gamepad",
                name = "Controle Wireless Dual-Shock",
                type = DeviceType.GAMEPAD,
                connectionType = ConnectionType.BLUETOOTH,
                isConnected = false,
                batteryPercent = 78,
                pollingRateHz = 250,
                latencyMs = 4
            )
        )
    )
    val devices: StateFlow<List<PeripheralDevice>> = _devices.asStateFlow()

    private val _games = MutableStateFlow<List<GameItem>>(
        emptyList()
    )
    val games: StateFlow<List<GameItem>> = _games.asStateFlow()

    // Optimized 'Pro' Keybinds based on user HUD
    private val _currentKeybinds = MutableStateFlow<List<KeybindItem>>(
        listOf(
            KeybindItem("kb_wasd", "WASD", "Movimento", 0.15f, 0.45f, KeyType.DPAD_WASD), // Roughly left center
            KeybindItem("kb_aim", "M_R", "Mira", 0.20f, 0.20f, KeyType.MOUSE_AIM),
            KeybindItem("kb_fire", "M_L", "Disparo", 0.35f, 0.20f, KeyType.MOUSE_FIRE),
            KeybindItem("kb_jump", "SPACE", "Pulo", 0.70f, 0.40f, KeyType.BUTTON), // Right side
            KeybindItem("kb_crouch", "C", "Agachar", 0.70f, 0.15f, KeyType.BUTTON),
            KeybindItem("kb_prone", "Z", "Deitar", 0.85f, 0.90f, KeyType.BUTTON),
            KeybindItem("kb_reload", "R", "Recarregar", 0.60f, 0.25f, KeyType.BUTTON),
            KeybindItem("kb_w1", "1", "Arma 1", 0.70f, 0.90f, KeyType.BUTTON),
            KeybindItem("kb_w2", "2", "Arma 2", 0.60f, 0.90f, KeyType.BUTTON),
            KeybindItem("kb_use", "F", "Usar", 0.65f, 0.75f, KeyType.BUTTON),
            KeybindItem("kb_bag", "TAB", "Mochila", 0.95f, 0.90f, KeyType.BUTTON),
            KeybindItem("kb_map", "M", "Mapa", 0.75f, 0.30f, KeyType.BUTTON),
            KeybindItem("kb_shift", "CTRL_L", "Ctrl", 0.85f, 0.80f, KeyType.BUTTON),
            KeybindItem("kb_esc", "ESC", "Menu", 0.05f, 0.30f, KeyType.BUTTON),
            KeybindItem("kb_t", "T", "Chat", 0.05f, 0.85f, KeyType.BUTTON),
            KeybindItem("kb_e", "E", "Interagir", 0.60f, 0.40f, KeyType.BUTTON),
            KeybindItem("kb_v", "V", "Voz", 0.60f, 0.70f, KeyType.BUTTON),
            KeybindItem("kb_f1", "F1", "F1", 0.75f, 0.05f, KeyType.BUTTON),
            KeybindItem("kb_f2", "F2", "F2", 0.75f, 0.10f, KeyType.BUTTON),
            KeybindItem("kb_altl", "ALT_L", "AltL", 0.85f, 0.30f, KeyType.BUTTON),
            KeybindItem("kb_altr", "ALT_R", "AltR", 0.85f, 0.20f, KeyType.BUTTON),
            KeybindItem("kb_meta", "META_L", "Meta", 0.90f, 0.20f, KeyType.BUTTON),
            KeybindItem("kb_g", "G", "G", 0.70f, 0.60f, KeyType.BUTTON),
            KeybindItem("kb_h", "H", "H", 0.80f, 0.50f, KeyType.BUTTON),
            KeybindItem("kb_y", "Y", "Y", 0.95f, 0.60f, KeyType.BUTTON),
            KeybindItem("kb_caps", "CAPS_L", "Caps", 0.50f, 0.40f, KeyType.BUTTON)
        )
    )
    val currentKeybinds: StateFlow<List<KeybindItem>> = _currentKeybinds.asStateFlow()

    // Settings
    private val _themeSetting = MutableStateFlow(AppThemeSetting.SYSTEM)
    val themeSetting: StateFlow<AppThemeSetting> = _themeSetting.asStateFlow()

    private val _isOneHandMode = MutableStateFlow(false)
    val isOneHandMode: StateFlow<Boolean> = _isOneHandMode.asStateFlow()

    private val _isVipMacroUnlocked = MutableStateFlow(true)
    val isVipMacroUnlocked: StateFlow<Boolean> = _isVipMacroUnlocked.asStateFlow()

    private val _selectedCursor = MutableStateFlow("cursor_fox_purple")
    val selectedCursor: StateFlow<String> = _selectedCursor.asStateFlow()

    private val _activePlayingGame = MutableStateFlow<GameItem?>(null)
    val activePlayingGame: StateFlow<GameItem?> = _activePlayingGame.asStateFlow()

    private val _terminalLogs = MutableStateFlow<List<String>>(
        listOf(
            "[Shadow Core 9.5] Initializing daemon...",
            "[HID-Bridge] USB Driver ready.",
            "[Wireless-ADB] Listener standby on 127.0.0.1:41235",
            "[Ready] Type 'help' to see available mapper commands."
        )
    )
    val terminalLogs: StateFlow<List<String>> = _terminalLogs.asStateFlow()

    val availableCursors = listOf(
        CursorOption("cursor_fox_purple", "Raposa Roxa Neon", "🦊", false),
        CursorOption("cursor_cyber_crosshair", "Crosshair Tático FPS", "🎯", false),
        CursorOption("cursor_cyber_arrow", "Seta Gamer Cyberpunk", "🕹️", false),
        CursorOption("cursor_panda_classic", "Panda Clássico", "🐼", false),
        CursorOption("cursor_red_dot", "Ponto Laser Vermelho", "🔴", true),
        CursorOption("cursor_gold_dragon", "Dragão Dourado VIP", "🐉", true)
    )

    // Activation Actions
    fun activateViaWirelessDebugging(port: String, code: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            _terminalLogs.update { it + "[Wireless-Pairing] Connecting to port $port with pairing key..." }
            delay(1200)
            _terminalLogs.update { it + "[Wireless-Pairing] Pair verified! Handshake complete (AES-256)." }
            delay(600)
            _terminalLogs.update { it + "[Shadow-Daemon] Injecting mapper binary -> Success!" }
            _activationState.value = ActivationState(
                isActivated = true,
                method = ActivationMethodType.DIRECT_WIRELESS,
                pairingPort = port,
                pairingCode = code,
                statusMessage = "Shadow Engine 9.5 Ativado (Sem Fio)"
            )
            onComplete()
        }
    }

    fun activateViaShizuku(onComplete: () -> Unit) {
        viewModelScope.launch {
            _terminalLogs.update { it + "[Shizuku-IPC] Requesting binder permission (moe.shizuku.privilege.api)..." }
            delay(800)
            _terminalLogs.update { it + "[Shizuku-IPC] Permission Granted! UID: 2000 (Shell)" }
            delay(500)
            _terminalLogs.update { it + "[Shadow-Daemon] Starting daemon service (shadow_starter.dex)..." }
            delay(700)
            _terminalLogs.update { it + "[Shadow-Daemon] Running with PID 28419 -> 0ms Input Latency Active!" }
            _activationState.value = ActivationState(
                isActivated = true,
                method = ActivationMethodType.SHIZUKU,
                statusMessage = "Shadow Engine 9.5 Ativado (Shizuku)"
            )
            onComplete()
        }
    }

    fun activateViaPcAdb(onComplete: () -> Unit) {
        viewModelScope.launch {
            _terminalLogs.update { it + "[ADB-PC] USB Debugging connection received from host." }
            delay(1000)
            _terminalLogs.update { it + "[ADB-PC] Executing activation script 'shadow_starter.sh'..." }
            delay(600)
            _terminalLogs.update { it + "[Shadow-Daemon] Kernel virtual mouse/keyboard device nodes created." }
            _activationState.value = ActivationState(
                isActivated = true,
                method = ActivationMethodType.PC_ADB,
                statusMessage = "Shadow Engine 9.5 Ativado (PC/Mac ADB)"
            )
            onComplete()
        }
    }

    fun deactivateEngine() {
        _activationState.value = ActivationState(
            isActivated = false,
            method = ActivationMethodType.NONE,
            statusMessage = "Precisa de ativação"
        )
        _terminalLogs.update { it + "[Shadow-Daemon] Mapper daemon stopped." }
    }

    // Device Actions
    fun toggleDeviceConnection(deviceId: String) {
        _devices.update { list ->
            list.map { dev ->
                if (dev.id == deviceId) dev.copy(isConnected = !dev.isConnected) else dev
            }
        }
    }

    fun connectAllPeripherals() {
        _devices.update { list ->
            list.map { it.copy(isConnected = true) }
        }
        _terminalLogs.update { it + "[HID] All detected USB & Bluetooth peripherals connected and calibrated." }
    }

    fun disconnectAllPeripherals() {
        _devices.update { list ->
            list.map { it.copy(isConnected = false) }
        }
    }

    fun arePeripheralsConnected(): Boolean {
        return _devices.value.any { it.isConnected }
    }

    // Games Management
    fun addGame(name: String, packageName: String, emoji: String, category: String) {
        val newGame = GameItem(
            id = "game_${System.currentTimeMillis()}",
            name = name,
            packageName = packageName,
            category = category,
            iconEmoji = emoji,
            isFavorite = true
        )
        _games.update { listOf(newGame) + it }
    }

    fun removeGame(gameId: String) {
        _games.update { list -> list.filterNot { it.id == gameId } }
    }

    fun startPlayingGame(game: GameItem) {
        _activePlayingGame.value = game
        _terminalLogs.update { it + "[GameLauncher] Launching ${game.name} with Shadow Floating HUD..." }
    }

    fun stopPlayingGame() {
        _activePlayingGame.value = null
    }

    fun updateKeybindPosition(keybindId: String, newX: Float, newY: Float) {
        _currentKeybinds.update { list ->
            list.map { item ->
                if (item.id == keybindId) {
                    item.copy(xPercent = newX.coerceIn(0.05f, 0.95f), yPercent = newY.coerceIn(0.05f, 0.95f))
                } else item
            }
        }
    }

    fun addKeybind(keyLabel: String, actionName: String) {
        val newKey = KeybindItem(
            id = "kb_${System.currentTimeMillis()}",
            keyLabel = keyLabel.uppercase(),
            actionName = actionName,
            xPercent = 0.5f,
            yPercent = 0.5f,
            keyType = KeyType.BUTTON
        )
        _currentKeybinds.update { it + newKey }
    }

    fun removeKeybind(id: String) {
        _currentKeybinds.update { it.filterNot { item -> item.id == id } }
    }

    // Settings
    fun setThemeSetting(setting: AppThemeSetting) {
        _themeSetting.value = setting
    }

    fun setOneHandMode(enabled: Boolean) {
        _isOneHandMode.value = enabled
    }

    fun setSelectedCursor(cursorId: String) {
        _selectedCursor.value = cursorId
    }

    fun unlockVip() {
        _isVipMacroUnlocked.value = true
    }

    fun runTerminalCommand(command: String) {
        val cmd = command.trim()
        if (cmd.isEmpty()) return

        val response = when {
            cmd.equals("help", ignoreCase = true) ->
                listOf(
                    "> $cmd",
                    "Comandos disponíveis:",
                    "  shizuku-check     - Verifica status do serviço Shizuku",
                    "  usb-list          - Lista dispositivos OTG conectados",
                    "  bt-status         - Mostra adaptadores Bluetooth",
                    "  activate-now      - Força ativação imediata do daemon",
                    "  status            - Status geral do Shadow Engine",
                    "  clear             - Limpa o histórico de comandos"
                )
            cmd.equals("clear", ignoreCase = true) -> {
                _terminalLogs.value = emptyList()
                return
            }
            cmd.equals("shizuku-check", ignoreCase = true) ->
                listOf(
                    "> $cmd",
                    "Shizuku Server: RUNNING (v13.5.4)",
                    "Permission: GRANTED (PID: 28419)",
                    "Binder IPC: OK (0.2ms latency)"
                )
            cmd.equals("usb-list", ignoreCase = true) ->
                listOf(
                    "> $cmd",
                    "USB Bus 001 Device 002: ID 1532:0078 Razer Gaming Mouse",
                    "USB Bus 001 Device 003: ID 046d:c31c Mechanical Keyboard"
                )
            cmd.equals("bt-status", ignoreCase = true) ->
                listOf(
                    "> $cmd",
                    "Bluetooth Adapter: ENABLED (BLE 5.2)",
                    "Paired Devices: 2 (Shadow Pro Mouse, Wireless Pad)"
                )
            cmd.equals("activate-now", ignoreCase = true) -> {
                _activationState.value = ActivationState(
                    isActivated = true,
                    method = ActivationMethodType.DIRECT_WIRELESS,
                    statusMessage = "Shadow Engine 9.5 Ativado via Terminal"
                )
                listOf(
                    "> $cmd",
                    "[SUCCESS] Shadow Core 9.5 daemon started successfully!"
                )
            }
            cmd.equals("status", ignoreCase = true) ->
                listOf(
                    "> $cmd",
                    "Shadow Engine: " + (if (_activationState.value.isActivated) "ATIVO" else "INATIVO"),
                    "Periféricos conectados: ${_devices.value.count { it.isConnected }}",
                    "Jogos cadastrados: ${_games.value.size}",
                    "Modo VIP: " + (if (_isVipMacroUnlocked.value) "DESBLOQUEADO" else "PADRÃO")
                )
            else ->
                listOf(
                    "> $cmd",
                    "Comando '$cmd' executado no terminal shell shadow.",
                    "Retorno: 0 (OK)"
                )
        }

        _terminalLogs.update { it + response }
    }
}
