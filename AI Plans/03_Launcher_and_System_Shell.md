# 🏠 Phase 1: Launcher & System Shell

> **Parent Plan:** [00_Master_Implementation_Plan.md](file:///c:/Users/abhis/AOSP/Projects/IVI%20Project/AI%20Plans/00_Master_Implementation_Plan.md)  
> **Module:** `app/` (Launcher)  
> **Duration:** Weeks 3–5  
> **Goal:** Build the main home screen, system shell (status bar + nav rail + quick controls), and app drawer

---

## 1. System Shell Architecture

The system shell is the always-visible frame around the content area. It consists of three persistent elements:

```
┌──────────────────────────────────────────────────────────────────┐
│  STATUS BAR  │ 🔵 Bluetooth │ 📶 Signal │ 🔋 Battery │ 10:30 AM │
├──────────┬───┴──────────────────────────────────────────────────┤
│          │                                                      │
│  NAV     │                                                      │
│  RAIL    │               CONTENT AREA                           │
│          │           (Home / Feature Screens)                    │
│  🏠 Home │                                                      │
│  🎵 Media│                                                      │
│  🗺️ Nav  │                                                      │
│  ❄️ HVAC │                                                      │
│  📞 Phone│                                                      │
│  ⚙️ More │                                                      │
│          │                                                      │
├──────────┴──────────────────────────────────────────────────────┤
│  QUICK CONTROLS  │ ❄️ 22°C │ 🔊 Vol 7 │ 🔆 Brightness │ ☀️/🌙 │
└──────────────────────────────────────────────────────────────────┘
```

### 1.1 Shell Composable Structure

```kotlin
// app/src/main/kotlin/com/ivi/launcher/ui/shell/SystemShell.kt
package com.ivi.launcher.ui.shell

@Composable
fun SystemShell(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    vehicleState: VehicleState,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        // Top: Status Bar
        StatusBar(
            time = vehicleState.time,
            bluetoothState = vehicleState.bluetoothState,
            signalStrength = vehicleState.signalStrength,
            batteryLevel = vehicleState.batteryLevel,
        )

        // Middle: Nav Rail + Content
        Row(modifier = Modifier.weight(1f)) {
            NavigationRail(
                currentRoute = currentRoute,
                onNavigate = onNavigate,
            )
            Box(modifier = Modifier.weight(1f)) {
                content()
            }
        }

        // Bottom: Quick Controls
        QuickControlsBar(
            temperature = vehicleState.temperature,
            volume = vehicleState.volume,
            brightness = vehicleState.brightness,
            onTemperatureChange = { /* ... */ },
            onVolumeChange = { /* ... */ },
            onBrightnessChange = { /* ... */ },
        )
    }
}
```

---

## 2. Status Bar Implementation

### 2.1 Status Bar Layout

```kotlin
// app/src/main/kotlin/com/ivi/launcher/ui/shell/StatusBar.kt
package com.ivi.launcher.ui.shell

@Composable
fun StatusBar(
    time: String,
    bluetoothState: BluetoothState,
    signalStrength: Int, // 0-4
    batteryLevel: Int,   // 0-100
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(IVIDimensions.StatusBarHeight),
        color = IVIColors.Surface.copy(alpha = 0.95f),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = IVISpacing.LG),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Left: OEM Logo / Brand Name
            Text(
                text = "IVI",
                style = IVITypography.LabelLarge,
                color = IVIColors.Primary,
            )

            Spacer(Modifier.weight(1f))

            // Center/Right: Status Icons
            Row(
                horizontalArrangement = Arrangement.spacedBy(IVISpacing.MD),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Bluetooth
                StatusIcon(
                    icon = if (bluetoothState == BluetoothState.CONNECTED)
                        Icons.Default.Bluetooth else Icons.Default.BluetoothDisabled,
                    active = bluetoothState == BluetoothState.CONNECTED,
                    contentDescription = "Bluetooth"
                )

                // Signal
                SignalStrengthIcon(strength = signalStrength)

                // Battery
                BatteryIcon(level = batteryLevel)

                // Divider
                VerticalDivider(
                    modifier = Modifier.height(24.dp),
                    color = IVIColors.OnSurfaceDim,
                )

                // Clock
                Text(
                    text = time,
                    style = IVITypography.TitleMedium,
                    color = IVIColors.OnSurface,
                )
            }
        }
    }
}
```

### 2.2 Status Bar Data

```kotlin
// app/src/main/kotlin/com/ivi/launcher/ui/shell/StatusBarViewModel.kt
package com.ivi.launcher.ui.shell

@HiltViewModel
class StatusBarViewModel @Inject constructor(
    private val carServiceManager: CarServiceManager,
) : ViewModel() {

    private val _statusState = MutableStateFlow(StatusBarState())
    val statusState: StateFlow<StatusBarState> = _statusState.asStateFlow()

    init {
        observeTime()
        observeBluetoothState()
        observeSignalStrength()
    }

    private fun observeTime() {
        viewModelScope.launch {
            while (true) {
                _statusState.update {
                    it.copy(time = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date()))
                }
                delay(30_000) // Update every 30s
            }
        }
    }

    private fun observeBluetoothState() {
        // Observe via CarPropertyManager or BluetoothAdapter
    }
}

data class StatusBarState(
    val time: String = "",
    val bluetoothState: BluetoothState = BluetoothState.DISCONNECTED,
    val signalStrength: Int = 0,
    val batteryLevel: Int = 100,
    val isNightMode: Boolean = false,
)
```

---

## 3. Navigation Rail

### 3.1 Navigation Destinations

```kotlin
// app/src/main/kotlin/com/ivi/launcher/navigation/IVIDestinations.kt
package com.ivi.launcher.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class IVIDestination(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    HOME(
        route = "home",
        label = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    MEDIA(
        route = "media",
        label = "Media",
        selectedIcon = Icons.Filled.MusicNote,
        unselectedIcon = Icons.Outlined.MusicNote,
    ),
    NAVIGATION(
        route = "navigation",
        label = "Maps",
        selectedIcon = Icons.Filled.Map,
        unselectedIcon = Icons.Outlined.Map,
    ),
    CLIMATE(
        route = "climate",
        label = "Climate",
        selectedIcon = Icons.Filled.AcUnit,
        unselectedIcon = Icons.Outlined.AcUnit,
    ),
    PHONE(
        route = "phone",
        label = "Phone",
        selectedIcon = Icons.Filled.Phone,
        unselectedIcon = Icons.Outlined.Phone,
    ),
    SETTINGS(
        route = "settings",
        label = "Settings",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
    ),
}
```

### 3.2 Navigation Rail Composable

```kotlin
// app/src/main/kotlin/com/ivi/launcher/ui/shell/NavigationRail.kt
package com.ivi.launcher.ui.shell

@Composable
fun IVINavigationRail(
    currentRoute: String,
    onNavigate: (IVIDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .width(IVIDimensions.NavRailWidth)
            .fillMaxHeight(),
        color = IVIColors.Surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = IVISpacing.MD),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                IVISpacing.XS,
                Alignment.Top
            ),
        ) {
            IVIDestination.entries.forEach { destination ->
                val selected = currentRoute == destination.route

                IVIIconButton(
                    icon = if (selected) destination.selectedIcon
                           else destination.unselectedIcon,
                    contentDescription = destination.label,
                    onClick = { onNavigate(destination) },
                    selected = selected,
                )

                Text(
                    text = destination.label,
                    style = IVITypography.LabelSmall,
                    color = if (selected) IVIColors.Primary
                            else IVIColors.OnSurfaceDim,
                )
            }
        }
    }
}
```

---

## 4. Home Screen

### 4.1 Home Screen Layout

The home screen shows a dashboard-style layout with:
- **Greeting / Time widget** (top)
- **Quick Access Cards** (media, navigation, climate, recent app)
- **Now Playing Mini-Card** (if media is active)
- **Weather / Vehicle Status widgets**

```kotlin
// app/src/main/kotlin/com/ivi/launcher/ui/home/HomeScreen.kt
package com.ivi.launcher.ui.home

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToMedia: () -> Unit,
    onNavigateToNavigation: () -> Unit,
    onNavigateToClimate: () -> Unit,
    onNavigateToPhone: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize()
            .padding(IVISpacing.LG),
        verticalArrangement = Arrangement.spacedBy(IVISpacing.MD),
        horizontalArrangement = Arrangement.spacedBy(IVISpacing.MD),
    ) {
        // Greeting + Clock (spans full width)
        item(span = { GridItemSpan(3) }) {
            GreetingWidget(
                greeting = uiState.greeting,
                dateTime = uiState.dateTime,
            )
        }

        // Now Playing Card (spans 2 columns)
        if (uiState.nowPlaying != null) {
            item(span = { GridItemSpan(2) }) {
                NowPlayingCard(
                    mediaInfo = uiState.nowPlaying!!,
                    onTap = onNavigateToMedia,
                    onPlayPause = { viewModel.onAction(HomeAction.TogglePlayback) },
                    onNext = { viewModel.onAction(HomeAction.NextTrack) },
                )
            }
        }

        // Climate Quick Card
        item {
            ClimateQuickCard(
                temperature = uiState.temperature,
                isACOn = uiState.isACOn,
                onTap = onNavigateToClimate,
            )
        }

        // Navigation Quick Card
        item {
            NavigationQuickCard(
                destination = uiState.lastDestination,
                eta = uiState.eta,
                onTap = onNavigateToNavigation,
            )
        }

        // Phone Quick Card
        item {
            PhoneQuickCard(
                recentContact = uiState.recentContact,
                missedCalls = uiState.missedCallCount,
                onTap = onNavigateToPhone,
            )
        }

        // Vehicle Status Card
        item {
            VehicleStatusCard(
                fuelLevel = uiState.fuelLevel,
                range = uiState.range,
                tirePressureOk = uiState.tirePressureOk,
            )
        }
    }
}
```

### 4.2 Home ViewModel

```kotlin
// app/src/main/kotlin/com/ivi/launcher/ui/home/HomeViewModel.kt
package com.ivi.launcher.ui.home

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val vehiclePropertyClient: VehiclePropertyClient,
    private val mediaClient: MediaClient,
    private val climateClient: ClimateClient,
) : BaseViewModel<HomeUiState, HomeEvent, HomeAction>(HomeUiState()) {

    init {
        observeVehicleData()
        observeMediaState()
        observeClimateState()
        updateGreeting()
    }

    override fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.TogglePlayback -> mediaClient.togglePlayPause()
            is HomeAction.NextTrack -> mediaClient.skipNext()
            is HomeAction.PreviousTrack -> mediaClient.skipPrevious()
        }
    }

    private fun observeVehicleData() {
        viewModelScope.launch {
            vehiclePropertyClient.observeProperties(
                listOf(
                    VehiclePropertyIds.FUEL_LEVEL,
                    VehiclePropertyIds.RANGE_REMAINING,
                    VehiclePropertyIds.TIRE_PRESSURE,
                )
            ).collect { properties ->
                updateState {
                    copy(
                        fuelLevel = properties.fuelLevel,
                        range = properties.range,
                        tirePressureOk = properties.tirePressureOk,
                    )
                }
            }
        }
    }

    private fun observeMediaState() {
        viewModelScope.launch {
            mediaClient.nowPlaying.collect { mediaInfo ->
                updateState { copy(nowPlaying = mediaInfo) }
            }
        }
    }

    private fun observeClimateState() {
        viewModelScope.launch {
            climateClient.climateState.collect { climate ->
                updateState {
                    copy(
                        temperature = climate.driverTemperature,
                        isACOn = climate.isACOn,
                    )
                }
            }
        }
    }

    private fun updateGreeting() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greeting = when {
            hour < 12 -> "Good Morning"
            hour < 17 -> "Good Afternoon"
            else -> "Good Evening"
        }
        updateState { copy(greeting = greeting) }
    }
}

data class HomeUiState(
    val greeting: String = "",
    val dateTime: String = "",
    val nowPlaying: MediaInfo? = null,
    val temperature: Float = 22f,
    val isACOn: Boolean = false,
    val lastDestination: String? = null,
    val eta: String? = null,
    val recentContact: String? = null,
    val missedCallCount: Int = 0,
    val fuelLevel: Float = 0f,
    val range: String = "",
    val tirePressureOk: Boolean = true,
) : UiState

sealed interface HomeAction {
    data object TogglePlayback : HomeAction
    data object NextTrack : HomeAction
    data object PreviousTrack : HomeAction
}

sealed interface HomeEvent {
    data class ShowError(val message: String) : HomeEvent
}
```

---

## 5. Quick Controls Bar

```kotlin
// app/src/main/kotlin/com/ivi/launcher/ui/shell/QuickControlsBar.kt
package com.ivi.launcher.ui.shell

@Composable
fun QuickControlsBar(
    temperature: Float,
    volume: Float,
    brightness: Float,
    onTemperatureChange: (Float) -> Unit,
    onVolumeChange: (Float) -> Unit,
    onBrightnessChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(IVIDimensions.QuickControlsHeight),
        color = IVIColors.Surface,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = IVISpacing.LG),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(IVISpacing.XL),
        ) {
            // Temperature
            QuickControlItem(
                icon = Icons.Default.Thermostat,
                label = "${temperature.roundToInt()}°C",
                value = temperature,
                valueRange = 16f..30f,
                onValueChange = onTemperatureChange,
                modifier = Modifier.weight(1f),
            )

            // Volume
            QuickControlItem(
                icon = Icons.Default.VolumeUp,
                label = "Vol ${(volume * 15).roundToInt()}",
                value = volume,
                onValueChange = onVolumeChange,
                modifier = Modifier.weight(1f),
            )

            // Brightness
            QuickControlItem(
                icon = Icons.Default.BrightnessHigh,
                label = "${(brightness * 100).roundToInt()}%",
                value = brightness,
                onValueChange = onBrightnessChange,
                modifier = Modifier.weight(1f),
            )

            // Day/Night Toggle
            IVIIconButton(
                icon = Icons.Default.DarkMode,
                contentDescription = "Toggle Day/Night",
                onClick = { /* toggle theme */ },
            )
        }
    }
}
```

---

## 6. App Drawer

```kotlin
// app/src/main/kotlin/com/ivi/launcher/ui/drawer/AppDrawer.kt
package com.ivi.launcher.ui.drawer

@Composable
fun AppDrawer(
    apps: List<AppInfo>,
    onAppClick: (AppInfo) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Full-screen overlay with app grid
    Surface(
        modifier = modifier.fillMaxSize(),
        color = IVIColors.Background.copy(alpha = 0.95f),
    ) {
        Column(
            modifier = Modifier.padding(IVISpacing.LG)
        ) {
            // Header with close button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "All Apps",
                    style = IVITypography.HeadlineMedium,
                    color = IVIColors.OnSurface,
                )
                IVIIconButton(
                    icon = Icons.Default.Close,
                    contentDescription = "Close",
                    onClick = onDismiss,
                )
            }

            Spacer(Modifier.height(IVISpacing.LG))

            // App Grid
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 120.dp),
                verticalArrangement = Arrangement.spacedBy(IVISpacing.MD),
                horizontalArrangement = Arrangement.spacedBy(IVISpacing.MD),
            ) {
                items(apps) { app ->
                    AppGridItem(
                        appInfo = app,
                        onClick = { onAppClick(app) },
                    )
                }
            }
        }
    }
}

@Composable
fun AppGridItem(
    appInfo: AppInfo,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .size(IVIDimensions.TouchTargetLarge)
            .clip(IVIShapes.Medium)
            .clickable(onClick = onClick)
            .padding(IVISpacing.XS),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // App Icon (loaded from package manager)
        AsyncImage(
            model = appInfo.icon,
            contentDescription = appInfo.label,
            modifier = Modifier.size(IVIDimensions.IconXLarge),
        )
        Spacer(Modifier.height(IVISpacing.XS))
        Text(
            text = appInfo.label,
            style = IVITypography.LabelSmall,
            color = IVIColors.OnSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
```

---

## 7. AndroidManifest.xml

```xml
<!-- app/src/main/AndroidManifest.xml -->
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.ivi.launcher">

    <!-- Car Permissions -->
    <uses-permission android:name="android.car.permission.CAR_INFO" />
    <uses-permission android:name="android.car.permission.CAR_ENERGY" />
    <uses-permission android:name="android.car.permission.CAR_SPEED" />
    <uses-permission android:name="android.car.permission.CONTROL_CAR_CLIMATE" />
    <uses-permission android:name="android.car.permission.CAR_CONTROL_AUDIO_VOLUME" />
    <uses-permission android:name="android.car.permission.CAR_EXTERIOR_ENVIRONMENT" />

    <!-- Standard Permissions -->
    <uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
    <uses-permission android:name="android.permission.READ_CONTACTS" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />

    <!-- Automotive Feature -->
    <uses-feature android:name="android.hardware.type.automotive" />

    <application
        android:name=".IVIApplication"
        android:label="IVI Launcher"
        android:theme="@style/Theme.IVI"
        android:supportsRtl="true">

        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:launchMode="singleTask"
            android:windowSoftInputMode="adjustResize">

            <!-- Home Intent Filter (makes this the launcher) -->
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.HOME" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
```

---

## 8. Verification Checklist

- [ ] System shell renders with status bar, nav rail, and quick controls
- [ ] Navigation rail highlights current destination
- [ ] Screen transitions are smooth (< 300ms)
- [ ] Home screen grid displays all widget cards
- [ ] Now Playing card updates with media state changes
- [ ] Climate quick card shows real-time temperature
- [ ] Quick controls bar sliders update vehicle properties
- [ ] App drawer opens/closes with animation
- [ ] App drawer loads all installed apps from PackageManager
- [ ] Status bar shows real-time clock, Bluetooth, signal, battery
- [ ] Landscape orientation locked, no rotation
- [ ] Works on 1920×1080 and 2560×1600 resolutions

---

*Next: [04_Climate_and_Vehicle.md](file:///c:/Users/abhis/AOSP/Projects/IVI%20Project/AI%20Plans/04_Climate_and_Vehicle.md) — HVAC controls, vehicle dashboard, Car Property Manager integration*
