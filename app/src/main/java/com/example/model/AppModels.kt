package com.example.model

enum class DeviceType {
    MOUSE, KEYBOARD, GAMEPAD, USB_HUB
}

enum class ConnectionType {
    USB_OTG, BLUETOOTH, WIRELESS_DONGLE
}

data class PeripheralDevice(
    val id: String,
    val name: String,
    val type: DeviceType,
    val connectionType: ConnectionType,
    val isConnected: Boolean,
    val batteryPercent: Int? = null,
    val pollingRateHz: Int = 1000,
    val dpi: Int = 1600,
    val latencyMs: Int = 1
)

enum class ActivationMethodType {
    NONE,
    DIRECT_WIRELESS,
    SHIZUKU,
    PC_ADB
}

data class ActivationState(
    val isActivated: Boolean = false,
    val method: ActivationMethodType = ActivationMethodType.NONE,
    val engineVersion: String = "Shadow Core v9.5.2",
    val shizukuRunning: Boolean = true,
    val shizukuVersion: String = "v13.5.4",
    val pairingPort: String = "41235",
    val pairingCode: String = "592814",
    val statusMessage: String = "Precisa de ativação"
)

data class GameItem(
    val id: String,
    val name: String,
    val packageName: String,
    val category: String,
    val iconEmoji: String,
    val sensitivityX: Float = 85f,
    val sensitivityY: Float = 80f,
    val isFavorite: Boolean = false,
    val customKeybindsCount: Int = 16
)

enum class KeyType {
    BUTTON,
    DPAD_WASD,
    MOUSE_AIM,
    MOUSE_FIRE,
    MACRO
}

data class KeybindItem(
    val id: String,
    val keyLabel: String,
    val actionName: String,
    val xPercent: Float, // 0.0 to 1.0
    val yPercent: Float, // 0.0 to 1.0
    val keyType: KeyType = KeyType.BUTTON
)

data class CursorOption(
    val id: String,
    val name: String,
    val iconSymbol: String,
    val isVip: Boolean = false
)

enum class AppThemeSetting {
    SYSTEM,
    LIGHT,
    DARK
}
