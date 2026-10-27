package com.ivi.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ivi.designsystem.components.IVIButton
import com.ivi.designsystem.components.IVICard
import com.ivi.designsystem.components.IVISlider
import com.ivi.designsystem.components.IVIToggle
import com.ivi.designsystem.theme.IVIColors
import com.ivi.designsystem.theme.IVIShapes
import com.ivi.designsystem.theme.IVISpacing
import com.ivi.designsystem.theme.IVITypography
import com.ivi.settings.domain.model.SettingsCategory
import com.ivi.settings.domain.model.UserProfile

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Row(
        modifier = modifier
            .fillMaxSize()
            .background(IVIColors.Background)
    ) {
        // Left Column: Category Navigation Panel
        Column(
            modifier = Modifier
                .width(280.dp)
                .fillMaxHeight()
                .background(IVIColors.Surface)
                .padding(IVISpacing.MD),
            verticalArrangement = Arrangement.spacedBy(IVISpacing.XS)
        ) {
            Text(
                text = "SETTINGS",
                style = IVITypography.LabelSmall,
                color = IVIColors.OnSurfaceDim,
                modifier = Modifier.padding(horizontal = IVISpacing.SM, vertical = IVISpacing.XS)
            )

            SettingsCategory.entries.forEach { category ->
                val isSelected = uiState.selectedCategory == category
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clip(IVIShapes.Medium)
                        .background(if (isSelected) IVIColors.PrimaryContainer else IVIColors.Surface)
                        .clickable { viewModel.onAction(SettingsAction.SelectCategory(category)) }
                        .padding(horizontal = IVISpacing.MD),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(IVISpacing.MD)
                ) {
                    Icon(
                        imageVector = when (category) {
                            SettingsCategory.DISPLAY -> Icons.Default.BrightnessMedium
                            SettingsCategory.SOUND -> Icons.Default.VolumeUp
                            SettingsCategory.CONNECTIVITY -> Icons.Default.Wifi
                            SettingsCategory.VEHICLE -> Icons.Default.DirectionsCar
                            SettingsCategory.PROFILES -> Icons.Default.AccountCircle
                            SettingsCategory.SYSTEM -> Icons.Default.Info
                        },
                        contentDescription = null,
                        tint = if (isSelected) IVIColors.Primary else IVIColors.OnSurfaceVariant
                    )
                    Text(
                        text = when (category) {
                            SettingsCategory.DISPLAY -> "Display"
                            SettingsCategory.SOUND -> "Audio & Sound"
                            SettingsCategory.CONNECTIVITY -> "Connectivity"
                            SettingsCategory.VEHICLE -> "Vehicle Settings"
                            SettingsCategory.PROFILES -> "Driver Profiles"
                            SettingsCategory.SYSTEM -> "System Info"
                        },
                        style = IVITypography.TitleMedium,
                        color = if (isSelected) IVIColors.OnSurface else IVIColors.OnSurfaceVariant
                    )
                }
            }
        }

        // Divider
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp),
            color = IVIColors.SurfaceBright
        )

        // Right Column: Detail Settings Pane
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(IVISpacing.LG)
        ) {
            when (uiState.selectedCategory) {
                SettingsCategory.DISPLAY -> DisplayPane(uiState = uiState, onAction = viewModel::onAction)
                SettingsCategory.SOUND -> SoundPane(uiState = uiState, onAction = viewModel::onAction)
                SettingsCategory.CONNECTIVITY -> ConnectivityPane(uiState = uiState, onAction = viewModel::onAction)
                SettingsCategory.VEHICLE -> VehiclePane(uiState = uiState, onAction = viewModel::onAction)
                SettingsCategory.PROFILES -> ProfilesPane(profiles = uiState.profiles, onSelect = { viewModel.onAction(SettingsAction.SelectProfile(it)) })
                SettingsCategory.SYSTEM -> SystemPane()
            }
        }
    }
}

@Composable
private fun DisplayPane(uiState: SettingsUiState, onAction: (SettingsAction) -> Unit) {
    IVICard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(IVISpacing.LG),
            verticalArrangement = Arrangement.spacedBy(IVISpacing.MD)
        ) {
            Text(text = "Display & Screen Settings", style = IVITypography.TitleLarge, color = IVIColors.OnSurface)
            IVIToggle(
                checked = uiState.display.isNightMode,
                onCheckedChange = { onAction(SettingsAction.ToggleNightMode) },
                label = "Night Mode (Dark Theme)"
            )
            IVIToggle(
                checked = uiState.display.isAutoBrightness,
                onCheckedChange = { onAction(SettingsAction.ToggleAutoBrightness) },
                label = "Ambient Light Sensor (Auto-Brightness)"
            )
            IVISlider(
                value = uiState.display.brightness,
                onValueChange = { onAction(SettingsAction.SetBrightness(it)) },
                label = "Screen Brightness",
                valueLabel = "${(uiState.display.brightness * 100).toInt()}%"
            )
        }
    }
}

@Composable
private fun SoundPane(uiState: SettingsUiState, onAction: (SettingsAction) -> Unit) {
    IVICard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(IVISpacing.LG),
            verticalArrangement = Arrangement.spacedBy(IVISpacing.MD)
        ) {
            Text(text = "Sound & Audio Controls", style = IVITypography.TitleLarge, color = IVIColors.OnSurface)
            IVISlider(
                value = uiState.sound.masterVolume,
                onValueChange = { onAction(SettingsAction.SetMasterVolume(it)) },
                label = "Master Volume",
                valueLabel = "${(uiState.sound.masterVolume * 15).toInt()} / 15"
            )
            IVIToggle(
                checked = uiState.sound.speedCompensatedVolume,
                onCheckedChange = { onAction(SettingsAction.ToggleSpeedCompensatedVolume) },
                label = "Speed-Compensated Audio Level"
            )
        }
    }
}

@Composable
private fun ConnectivityPane(uiState: SettingsUiState, onAction: (SettingsAction) -> Unit) {
    IVICard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(IVISpacing.LG),
            verticalArrangement = Arrangement.spacedBy(IVISpacing.MD)
        ) {
            Text(text = "Wireless & Connectivity", style = IVITypography.TitleLarge, color = IVIColors.OnSurface)
            IVIToggle(
                checked = uiState.connectivity.isWifiEnabled,
                onCheckedChange = { onAction(SettingsAction.ToggleWifi) },
                label = "Wi-Fi: ${if (uiState.connectivity.isWifiEnabled) uiState.connectivity.connectedWifiSsid ?: "Enabled" else "Off"}"
            )
            IVIToggle(
                checked = uiState.connectivity.isBluetoothEnabled,
                onCheckedChange = { onAction(SettingsAction.ToggleBluetooth) },
                label = "Bluetooth: ${if (uiState.connectivity.isBluetoothEnabled) uiState.connectivity.connectedBtDevice ?: "Enabled" else "Off"}"
            )
        }
    }
}

@Composable
private fun VehiclePane(uiState: SettingsUiState, onAction: (SettingsAction) -> Unit) {
    IVICard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(IVISpacing.LG),
            verticalArrangement = Arrangement.spacedBy(IVISpacing.MD)
        ) {
            Text(text = "Vehicle Convenience & Safety", style = IVITypography.TitleLarge, color = IVIColors.OnSurface)
            IVIToggle(
                checked = uiState.vehicle.autoDoorLock,
                onCheckedChange = { onAction(SettingsAction.ToggleAutoDoorLock) },
                label = "Speed-Sensitive Auto Door Locks"
            )
            IVIToggle(
                checked = uiState.vehicle.speedLimitWarning,
                onCheckedChange = {},
                label = "Visual Speed Limit Exceed Alert"
            )
        }
    }
}

@Composable
private fun ProfilesPane(profiles: List<UserProfile>, onSelect: (String) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(IVISpacing.MD),
        modifier = Modifier.fillMaxSize()
    ) {
        items(profiles) { profile ->
            IVICard(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onSelect(profile.id) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(IVISpacing.LG),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(IVISpacing.MD)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(IVIShapes.Full)
                                .background(IVIColors.PrimaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = profile.avatarInitials,
                                style = IVITypography.TitleMedium,
                                color = IVIColors.Primary
                            )
                        }
                        Column {
                            Text(text = profile.name, style = IVITypography.TitleLarge, color = IVIColors.OnSurface)
                            Text(
                                text = "Seat Memory Pos ${profile.seatMemoryPosition} • Pref Temp ${profile.preferredTempCelsius}°C",
                                style = IVITypography.BodyMedium,
                                color = IVIColors.OnSurfaceVariant
                            )
                        }
                    }

                    if (profile.isCurrent) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(IVISpacing.XS)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = IVIColors.Success)
                            Text(text = "ACTIVE", style = IVITypography.LabelLarge, color = IVIColors.Success)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SystemPane() {
    IVICard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(IVISpacing.LG),
            verticalArrangement = Arrangement.spacedBy(IVISpacing.SM)
        ) {
            Text(text = "System Information", style = IVITypography.TitleLarge, color = IVIColors.OnSurface)
            Text(text = "OS: Android Automotive OS (AOSP API 34)", style = IVITypography.BodyLarge, color = IVIColors.OnSurfaceVariant)
            Text(text = "Hardware: IVI Snapdragon Automotive Cockpit Platform", style = IVITypography.BodyLarge, color = IVIColors.OnSurfaceVariant)
            Text(text = "Software Build: 2026.10-AAOS14-RELEASE", style = IVITypography.BodyLarge, color = IVIColors.OnSurfaceVariant)
            Text(text = "VIN: 1HGCR2F83HA029381", style = IVITypography.BodyLarge, color = IVIColors.OnSurfaceVariant)
        }
    }
}
