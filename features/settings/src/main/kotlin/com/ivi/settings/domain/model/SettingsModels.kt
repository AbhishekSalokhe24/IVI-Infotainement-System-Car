package com.ivi.settings.domain.model

enum class SettingsCategory {
    DISPLAY,
    SOUND,
    CONNECTIVITY,
    VEHICLE,
    PROFILES,
    SYSTEM
}

data class UserProfile(
    val id: String,
    val name: String,
    val avatarInitials: String,
    val isCurrent: Boolean = false,
    val seatMemoryPosition: Int = 1,
    val preferredTempCelsius: Float = 22.0f
)

data class DisplaySettings(
    val brightness: Float = 0.85f,
    val isAutoBrightness: Boolean = true,
    val isNightMode: Boolean = true,
    val screenTimeoutSeconds: Int = 120
)

data class SoundSettings(
    val masterVolume: Float = 0.7f,
    val mediaVolume: Float = 0.8f,
    val navVolume: Float = 0.9f,
    val bassBoost: Float = 0.5f,
    val treble: Float = 0.5f,
    val speedCompensatedVolume: Boolean = true
)

data class ConnectivitySettings(
    val isWifiEnabled: Boolean = true,
    val connectedWifiSsid: String? = "Tesla-Guest-5G",
    val isBluetoothEnabled: Boolean = true,
    val connectedBtDevice: String? = "Pixel 8 Pro",
    val isHotspotEnabled: Boolean = false
)

data class VehicleSettingsState(
    val useMetricUnits: Boolean = true,
    val autoDoorLock: Boolean = true,
    val mirrorAutoFold: Boolean = true,
    val ambientLightColorHex: String = "#3B82F6",
    val speedLimitWarning: Boolean = true
)
