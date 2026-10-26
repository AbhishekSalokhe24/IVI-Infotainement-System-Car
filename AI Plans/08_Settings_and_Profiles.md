# ⚙️ Phase 6: Settings & User Profiles

> **Parent Plan:** [00_Master_Implementation_Plan.md](file:///c:/Users/abhis/AOSP/Projects/IVI%20Project/AI%20Plans/00_Master_Implementation_Plan.md)  
> **Module:** `features/settings`  
> **Duration:** Weeks 20–21  
> **Goal:** System settings, user profile management, connectivity, and OEM customization hooks

---

## 1. Settings Categories

### 1.1 Settings Structure

```
Settings
├── Display & Brightness
│   ├── Brightness slider
│   ├── Auto-brightness toggle
│   ├── Day/Night mode (Auto / Day / Night)
│   └── Screen timeout
│
├── Sound & Audio
│   ├── Master volume
│   ├── Media volume
│   ├── Navigation volume
│   ├── Call volume
│   ├── Alert volume
│   ├── Equalizer (preset or custom)
│   ├── Audio balance (Front/Rear, Left/Right)
│   └── Speed-compensated volume
│
├── Connectivity
│   ├── Wi-Fi
│   │   ├── Available networks
│   │   ├── Saved networks
│   │   └── Hotspot settings
│   ├── Bluetooth
│   │   ├── Paired devices
│   │   ├── Available devices
│   │   ├── Device profiles (HFP, A2DP, PBAP, MAP)
│   │   └── Visibility
│   └── Mobile Data (if equipped)
│       ├── Data usage
│       └── APN settings
│
├── Vehicle
│   ├── Units (Metric / Imperial)
│   ├── Door lock settings
│   ├── Ambient lighting color
│   ├── Mirror auto-fold
│   └── Drive mode (Eco / Comfort / Sport)
│
├── User Profiles
│   ├── Current profile
│   ├── Create new profile
│   ├── Edit profile
│   ├── Delete profile
│   └── Guest mode
│
├── Privacy & Security
│   ├── Location permissions
│   ├── App permissions
│   ├── Data collection consent
│   └── Reset to factory
│
├── System
│   ├── Language & Input
│   ├── Date & Time
│   ├── Software Update
│   ├── Storage
│   ├── About (Model, VIN, OS version, Build)
│   └── Developer options
│
└── Apps
    ├── Installed apps list
    ├── App permissions
    ├── Default apps
    └── App storage/cache
```

### 1.2 Settings Screen Layout

```
┌──────────────────────────────────────────────────────────────┐
│                        SETTINGS                              │
├─────────────────┬────────────────────────────────────────────┤
│                 │                                            │
│  Categories     │          Settings Detail                   │
│  (Left Panel)   │          (Right Panel)                     │
│                 │                                            │
│  > Display      │  ┌──────────────────────────────────────┐  │
│    Sound        │  │  Brightness                          │  │
│    Connectivity │  │  ████████████████░░░░░░  75%         │  │
│    Vehicle      │  ├──────────────────────────────────────┤  │
│    Profiles     │  │  Auto-Brightness          [Toggle]   │  │
│    Privacy      │  ├──────────────────────────────────────┤  │
│    System       │  │  Theme Mode                          │  │
│    Apps         │  │  ○ Auto  ● Day  ○ Night              │  │
│                 │  ├──────────────────────────────────────┤  │
│                 │  │  Screen Timeout                      │  │
│                 │  │  [1 min ▼]                           │  │
│                 │  └──────────────────────────────────────┘  │
│                 │                                            │
├─────────────────┴────────────────────────────────────────────┤
└──────────────────────────────────────────────────────────────┘
```

---

## 2. Settings Screen Implementation

### 2.1 Main Settings Screen (Two-Pane)

```kotlin
// features/settings/src/main/kotlin/com/ivi/settings/ui/SettingsScreen.kt
package com.ivi.settings.ui

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Row(modifier = Modifier.fillMaxSize()) {
        // Left: Category list
        SettingsCategoryList(
            categories = SettingsCategory.entries,
            selectedCategory = uiState.selectedCategory,
            onCategorySelected = { viewModel.onAction(SettingsAction.SelectCategory(it)) },
            modifier = Modifier
                .width(300.dp)
                .fillMaxHeight()
                .background(IVIColors.Surface),
        )

        // Divider
        VerticalDivider(color = IVIColors.SurfaceBright)

        // Right: Detail pane
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(IVISpacing.LG),
        ) {
            when (uiState.selectedCategory) {
                SettingsCategory.DISPLAY -> DisplaySettings(viewModel)
                SettingsCategory.SOUND -> SoundSettings(viewModel)
                SettingsCategory.CONNECTIVITY -> ConnectivitySettings(viewModel)
                SettingsCategory.VEHICLE -> VehicleSettings(viewModel)
                SettingsCategory.PROFILES -> ProfileSettings(viewModel)
                SettingsCategory.PRIVACY -> PrivacySettings(viewModel)
                SettingsCategory.SYSTEM -> SystemSettings(viewModel)
                SettingsCategory.APPS -> AppSettings(viewModel)
            }
        }
    }
}

enum class SettingsCategory(val label: String, val icon: ImageVector) {
    DISPLAY("Display", Icons.Default.Brightness6),
    SOUND("Sound", Icons.Default.VolumeUp),
    CONNECTIVITY("Connectivity", Icons.Default.Wifi),
    VEHICLE("Vehicle", Icons.Default.DirectionsCar),
    PROFILES("Profiles", Icons.Default.Person),
    PRIVACY("Privacy", Icons.Default.Lock),
    SYSTEM("System", Icons.Default.Settings),
    APPS("Apps", Icons.Default.Apps),
}
```

### 2.2 Display Settings

```kotlin
// features/settings/src/main/kotlin/com/ivi/settings/ui/sections/DisplaySettings.kt
package com.ivi.settings.ui.sections

@Composable
fun DisplaySettings(viewModel: SettingsViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(IVISpacing.SM),
    ) {
        item {
            Text(
                text = "Display & Brightness",
                style = IVITypography.HeadlineSmall,
                color = IVIColors.OnSurface,
            )
        }

        // Brightness
        item {
            SettingsSliderItem(
                title = "Brightness",
                value = uiState.brightness,
                onValueChange = { viewModel.onAction(SettingsAction.SetBrightness(it)) },
                icon = Icons.Default.BrightnessHigh,
                valueLabel = "${(uiState.brightness * 100).roundToInt()}%",
            )
        }

        // Auto-Brightness
        item {
            SettingsToggleItem(
                title = "Auto-Brightness",
                subtitle = "Adjust brightness based on ambient light",
                isChecked = uiState.autoBrightness,
                onToggle = { viewModel.onAction(SettingsAction.ToggleAutoBrightness) },
                icon = Icons.Default.BrightnessAuto,
            )
        }

        // Theme Mode
        item {
            SettingsRadioGroup(
                title = "Theme Mode",
                options = listOf("Auto", "Day", "Night"),
                selectedIndex = uiState.themeMode.ordinal,
                onSelected = {
                    viewModel.onAction(SettingsAction.SetThemeMode(ThemeMode.entries[it]))
                },
                icon = Icons.Default.DarkMode,
            )
        }
    }
}
```

### 2.3 Reusable Settings Components

```kotlin
// features/settings/src/main/kotlin/com/ivi/settings/ui/components/SettingsComponents.kt
package com.ivi.settings.ui.components

@Composable
fun SettingsToggleItem(
    title: String,
    subtitle: String? = null,
    isChecked: Boolean,
    onToggle: () -> Unit,
    icon: ImageVector,
) {
    IVICard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(IVISpacing.MD),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = IVIColors.Primary,
                modifier = Modifier.size(IVIDimensions.IconMedium),
            )
            Spacer(Modifier.width(IVISpacing.MD))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = IVITypography.TitleMedium,
                    color = IVIColors.OnSurface,
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = IVITypography.BodyMedium,
                        color = IVIColors.OnSurfaceVariant,
                    )
                }
            }
            Switch(
                checked = isChecked,
                onCheckedChange = { onToggle() },
                modifier = Modifier.size(IVIDimensions.TouchTargetMin),
            )
        }
    }
}

@Composable
fun SettingsSliderItem(
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    icon: ImageVector,
    valueLabel: String? = null,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
) {
    IVICard {
        Column(modifier = Modifier.padding(IVISpacing.MD)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = IVIColors.Primary,
                    modifier = Modifier.size(IVIDimensions.IconMedium),
                )
                Spacer(Modifier.width(IVISpacing.MD))
                Text(
                    text = title,
                    style = IVITypography.TitleMedium,
                    color = IVIColors.OnSurface,
                    modifier = Modifier.weight(1f),
                )
                if (valueLabel != null) {
                    Text(
                        text = valueLabel,
                        style = IVITypography.TitleMedium,
                        color = IVIColors.Primary,
                    )
                }
            }
            IVISlider(
                value = value,
                onValueChange = onValueChange,
                valueRange = valueRange,
            )
        }
    }
}
```

---

## 3. User Profile System

### 3.1 Profile Features

| Feature | Description |
|---------|-------------|
| **Seat Position** | Remember driver seat, mirror, steering wheel positions |
| **Climate Prefs** | Default temperature, fan speed |
| **Audio Prefs** | EQ settings, volume levels, last station/media |
| **Navigation** | Home/Work addresses, recent destinations |
| **Display** | Brightness, theme mode |
| **Phone** | Paired Bluetooth device, favorites |
| **App Layout** | Home screen widget arrangement |

### 3.2 Profile Data Model

```kotlin
// features/settings/src/main/kotlin/com/ivi/settings/domain/model/UserProfile.kt
package com.ivi.settings.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String,
    val name: String,
    val avatarColor: Long = 0xFF3B82F6,
    val isGuest: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),

    // Preferences
    val displayPrefs: DisplayPrefs = DisplayPrefs(),
    val audioPrefs: AudioPrefs = AudioPrefs(),
    val climatePrefs: ClimatePrefs = ClimatePrefs(),
    val navigationPrefs: NavigationPrefs = NavigationPrefs(),
    val bluetoothDeviceAddress: String? = null,
)

@Serializable
data class DisplayPrefs(
    val brightness: Float = 0.75f,
    val autoBrightness: Boolean = true,
    val themeMode: ThemeMode = ThemeMode.AUTO,
)

@Serializable
data class AudioPrefs(
    val masterVolume: Float = 0.5f,
    val mediaVolume: Float = 0.6f,
    val navVolume: Float = 0.7f,
    val callVolume: Float = 0.8f,
    val balance: Float = 0f,     // -1.0 (left) to 1.0 (right)
    val fade: Float = 0f,       // -1.0 (rear) to 1.0 (front)
    val eqPreset: String = "flat",
)

@Serializable
data class ClimatePrefs(
    val driverTemp: Float = 22f,
    val passengerTemp: Float = 22f,
    val fanSpeed: Int = 3,
    val isACOn: Boolean = true,
)

@Serializable
data class NavigationPrefs(
    val homeAddress: String? = null,
    val workAddress: String? = null,
    val avoidTolls: Boolean = false,
    val avoidHighways: Boolean = false,
    val mapStyle: String = "dark",
)

enum class ThemeMode { AUTO, DAY, NIGHT }
```

### 3.3 Profile Storage

```kotlin
// features/settings/src/main/kotlin/com/ivi/settings/data/ProfileRepository.kt
package com.ivi.settings.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val json: Json,
) : ProfileRepository {

    companion object {
        val ACTIVE_PROFILE_KEY = stringPreferencesKey("active_profile_id")
        val PROFILES_KEY = stringPreferencesKey("profiles_json")
    }

    override fun getActiveProfile(): Flow<UserProfile?> = dataStore.data.map { prefs ->
        val activeId = prefs[ACTIVE_PROFILE_KEY] ?: return@map null
        val profiles = getProfileList(prefs)
        profiles.find { it.id == activeId }
    }

    override fun getAllProfiles(): Flow<List<UserProfile>> = dataStore.data.map { prefs ->
        getProfileList(prefs)
    }

    override suspend fun setActiveProfile(profileId: String) {
        dataStore.edit { prefs ->
            prefs[ACTIVE_PROFILE_KEY] = profileId
        }
    }

    override suspend fun createProfile(profile: UserProfile) {
        dataStore.edit { prefs ->
            val profiles = getProfileList(prefs).toMutableList()
            profiles.add(profile)
            prefs[PROFILES_KEY] = json.encodeToString(profiles)
        }
    }

    override suspend fun updateProfile(profile: UserProfile) {
        dataStore.edit { prefs ->
            val profiles = getProfileList(prefs).toMutableList()
            val index = profiles.indexOfFirst { it.id == profile.id }
            if (index >= 0) {
                profiles[index] = profile
                prefs[PROFILES_KEY] = json.encodeToString(profiles)
            }
        }
    }

    override suspend fun deleteProfile(profileId: String) {
        dataStore.edit { prefs ->
            val profiles = getProfileList(prefs).filter { it.id != profileId }
            prefs[PROFILES_KEY] = json.encodeToString(profiles)
        }
    }

    private fun getProfileList(prefs: Preferences): List<UserProfile> {
        val raw = prefs[PROFILES_KEY] ?: return emptyList()
        return try {
            json.decodeFromString<List<UserProfile>>(raw)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
```

---

## 4. Connectivity Settings

### 4.1 Bluetooth Pairing UI

```kotlin
// features/settings/src/main/kotlin/com/ivi/settings/ui/sections/BluetoothSettings.kt
package com.ivi.settings.ui.sections

@Composable
fun BluetoothSettings(
    viewModel: SettingsViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(IVISpacing.SM),
    ) {
        item {
            // Bluetooth toggle
            SettingsToggleItem(
                title = "Bluetooth",
                isChecked = uiState.isBluetoothEnabled,
                onToggle = { viewModel.onAction(SettingsAction.ToggleBluetooth) },
                icon = Icons.Default.Bluetooth,
            )
        }

        // Paired Devices
        item {
            Text(
                text = "Paired Devices",
                style = IVITypography.HeadlineSmall,
                color = IVIColors.OnSurface,
            )
        }

        items(uiState.pairedDevices) { device ->
            BluetoothDeviceItem(
                device = device,
                onConnect = { viewModel.onAction(SettingsAction.ConnectBtDevice(device)) },
                onForget = { viewModel.onAction(SettingsAction.ForgetBtDevice(device)) },
            )
        }

        // Available Devices (scanning)
        if (uiState.isBluetoothScanning) {
            item {
                Text(
                    text = "Available Devices",
                    style = IVITypography.HeadlineSmall,
                    color = IVIColors.OnSurface,
                )
            }

            items(uiState.availableDevices) { device ->
                BluetoothDeviceItem(
                    device = device,
                    onConnect = { viewModel.onAction(SettingsAction.PairBtDevice(device)) },
                )
            }

            item {
                CircularProgressIndicator(
                    modifier = Modifier.padding(IVISpacing.MD),
                    color = IVIColors.Primary,
                )
            }
        }
    }
}
```

---

## 5. OEM Customization Hooks

```kotlin
// features/settings/src/main/kotlin/com/ivi/settings/oem/OEMConfig.kt
package com.ivi.settings.oem

/**
 * OEM configuration overlay.
 * OEMs can override these via Runtime Resource Overlays (RRO).
 */
data class OEMConfig(
    val brandName: String = "IVI",
    val brandLogoResId: Int = 0,
    val primaryColorHex: String = "#3B82F6",
    val enabledFeatures: Set<String> = setOf(
        "media", "navigation", "climate", "phone", "settings"
    ),
    val disabledSettings: Set<String> = emptySet(),
    val maxProfiles: Int = 5,
    val enableGuestMode: Boolean = true,
    val defaultUnits: Units = Units.METRIC,
    val showDeveloperOptions: Boolean = false,
)

enum class Units { METRIC, IMPERIAL }
```

---

## 6. Verification Checklist

- [ ] Settings two-pane layout renders correctly
- [ ] All settings categories navigate properly
- [ ] Brightness slider updates display brightness
- [ ] Day/Night/Auto theme switching works
- [ ] Volume sliders update CarAudioManager
- [ ] Wi-Fi list shows available networks
- [ ] Bluetooth pairing flow works end-to-end
- [ ] User profiles create, edit, delete correctly
- [ ] Profile switching applies saved preferences
- [ ] Guest mode creates temporary profile
- [ ] Language change updates all UI strings
- [ ] Software update screen shows current version
- [ ] Factory reset confirmation dialog works
- [ ] OEM config overlay applies custom branding

---

*Next: [09_Notifications_and_Voice.md](file:///c:/Users/abhis/AOSP/Projects/IVI%20Project/AI%20Plans/09_Notifications_and_Voice.md) — Notification center, heads-up notifications, voice assistant*
