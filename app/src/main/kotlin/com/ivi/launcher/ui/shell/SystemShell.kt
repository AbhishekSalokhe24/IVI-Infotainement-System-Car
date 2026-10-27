package com.ivi.launcher.ui.shell

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ivi.designsystem.components.IVIButton
import com.ivi.designsystem.components.IVICard
import com.ivi.designsystem.components.IVIIconButton
import com.ivi.designsystem.theme.IVIColors
import com.ivi.designsystem.theme.IVISpacing
import com.ivi.designsystem.theme.IVITypography
import com.ivi.launcher.navigation.IVIDestination
import com.ivi.launcher.ui.notifications.HeadsUpNotification
import com.ivi.launcher.ui.notifications.IVINotification
import com.ivi.launcher.ui.voice.VoiceAssistantOverlay
import com.ivi.launcher.ui.voice.VoiceState

@Composable
fun SystemShell(
    currentRoute: String,
    onNavigate: (IVIDestination) -> Unit,
    statusBarState: StatusBarState,
    temperature: Float,
    volume: Float,
    brightness: Float,
    isNightMode: Boolean,
    onTemperatureChange: (Float) -> Unit,
    onVolumeChange: (Float) -> Unit,
    onBrightnessChange: (Float) -> Unit,
    onToggleNightMode: () -> Unit,
    // Voice & Notifications State
    voiceState: VoiceState,
    onVoiceClick: () -> Unit,
    onVoiceCommand: (String) -> Unit,
    onVoiceDismiss: () -> Unit,
    headsUpNotification: IVINotification?,
    onDismissHeadsUp: () -> Unit,
    notificationCenterOpen: Boolean,
    onToggleNotificationCenter: () -> Unit,
    notificationsList: List<IVINotification>,
    onClearAllNotifications: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top: Status Bar
            StatusBar(
                time = statusBarState.time,
                bluetoothState = statusBarState.bluetoothState,
                signalStrength = statusBarState.signalStrength,
                batteryLevel = statusBarState.batteryLevel,
                onVoiceClick = onVoiceClick,
                onNotificationsClick = onToggleNotificationCenter
            )

            // Center Area: Navigation Rail on left + Screen content on right
            Row(modifier = Modifier.weight(1f)) {
                IVINavigationRail(
                    currentRoute = currentRoute,
                    onNavigate = onNavigate,
                )
                Box(modifier = Modifier.weight(1f)) {
                    content()
                }
            }

            // Bottom: Quick Controls Bar
            QuickControlsBar(
                temperature = temperature,
                volume = volume,
                brightness = brightness,
                isNightMode = isNightMode,
                onTemperatureChange = onTemperatureChange,
                onVolumeChange = onVolumeChange,
                onBrightnessChange = onBrightnessChange,
                onToggleNightMode = onToggleNightMode,
            )
        }

        // Heads-Up Notification Banner (Top Center)
        HeadsUpNotification(
            notification = headsUpNotification,
            onDismiss = onDismissHeadsUp,
            onPrimaryAction = {},
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // Notification Center Drawer (Pull-Down from Top)
        AnimatedVisibility(
            visible = notificationCenterOpen,
            enter = slideInVertically { -it } + fadeIn(),
            exit = slideOutVertically { -it } + fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(IVIColors.Scrim)
                    .clickable { onToggleNotificationCenter() }
            ) {
                IVICard(
                    modifier = Modifier
                        .fillMaxWidth(0.55f)
                        .fillMaxHeight(0.75f)
                        .align(Alignment.TopCenter)
                        .clickable(enabled = false) {}
                ) {
                    Column(modifier = Modifier.padding(IVISpacing.LG)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(IVISpacing.SM)
                            ) {
                                Icon(Icons.Default.Notifications, contentDescription = null, tint = IVIColors.Primary)
                                Text(text = "NOTIFICATION CENTER", style = IVITypography.TitleLarge, color = IVIColors.OnSurface)
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(IVISpacing.SM)) {
                                if (notificationsList.isNotEmpty()) {
                                    IVIIconButton(
                                        icon = Icons.Default.DeleteSweep,
                                        contentDescription = "Clear All",
                                        onClick = onClearAllNotifications
                                    )
                                }
                                IVIIconButton(
                                    icon = Icons.Default.Close,
                                    contentDescription = "Close",
                                    onClick = onToggleNotificationCenter
                                )
                            }
                        }

                        Spacer(Modifier.height(IVISpacing.MD))
                        Divider(color = IVIColors.SurfaceBright)
                        Spacer(Modifier.height(IVISpacing.MD))

                        if (notificationsList.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No New Notifications",
                                    style = IVITypography.BodyLarge,
                                    color = IVIColors.OnSurfaceDim
                                )
                            }
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(IVISpacing.MD),
                                modifier = Modifier.weight(1f)
                            ) {
                                items(notificationsList) { notif ->
                                    IVICard(modifier = Modifier.fillMaxWidth()) {
                                        Column(modifier = Modifier.padding(IVISpacing.MD)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(text = notif.title, style = IVITypography.TitleMedium, color = IVIColors.OnSurface)
                                                Text(text = notif.timestampFormatted, style = IVITypography.LabelSmall, color = IVIColors.OnSurfaceDim)
                                            }
                                            Text(text = notif.message, style = IVITypography.BodyMedium, color = IVIColors.OnSurfaceVariant)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Voice Assistant Full-Screen Scrim Overlay
        VoiceAssistantOverlay(
            voiceState = voiceState,
            onSimulateSampleCommand = onVoiceCommand,
            onDismiss = onVoiceDismiss
        )
    }
}
