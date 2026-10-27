package com.ivi.launcher.ui.shell

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BluetoothDisabled
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.NetworkCell
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ivi.designsystem.components.IVIIconButton
import com.ivi.designsystem.theme.IVIColors
import com.ivi.designsystem.theme.IVIDimensions
import com.ivi.designsystem.theme.IVISpacing
import com.ivi.designsystem.theme.IVITypography

@Composable
fun StatusBar(
    time: String,
    bluetoothState: BluetoothState,
    signalStrength: Int,
    batteryLevel: Int,
    onVoiceClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
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
            // Left: OEM Brand Name + Quick Trigger
            Text(
                text = "IVI CONNECT",
                style = IVITypography.LabelLarge,
                color = IVIColors.Primary,
            )

            Spacer(Modifier.width(IVISpacing.LG))

            // Voice Assistant Trigger in Status Bar
            IVIIconButton(
                icon = Icons.Default.Mic,
                contentDescription = "Voice Assistant",
                onClick = onVoiceClick,
                modifier = Modifier.size(36.dp)
            )

            // Notifications Center Trigger
            IVIIconButton(
                icon = Icons.Default.Notifications,
                contentDescription = "Notifications",
                onClick = onNotificationsClick,
                modifier = Modifier.size(36.dp)
            )

            Spacer(Modifier.weight(1f))

            // Right: Status Icons + Clock
            Row(
                horizontalArrangement = Arrangement.spacedBy(IVISpacing.MD),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Bluetooth
                Icon(
                    imageVector = if (bluetoothState == BluetoothState.CONNECTED)
                        Icons.Default.Bluetooth else Icons.Default.BluetoothDisabled,
                    contentDescription = "Bluetooth",
                    tint = if (bluetoothState == BluetoothState.CONNECTED)
                        IVIColors.Primary else IVIColors.OnSurfaceDim,
                    modifier = Modifier.size(24.dp)
                )

                // Network Signal
                Icon(
                    imageVector = Icons.Default.NetworkCell,
                    contentDescription = "Signal $signalStrength/4",
                    tint = IVIColors.OnSurface,
                    modifier = Modifier.size(24.dp)
                )

                // Battery / Charge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(IVISpacing.XXS)
                ) {
                    Icon(
                        imageVector = Icons.Default.BatteryChargingFull,
                        contentDescription = "Battery $batteryLevel%",
                        tint = IVIColors.Success,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "$batteryLevel%",
                        style = IVITypography.LabelSmall,
                        color = IVIColors.OnSurfaceVariant
                    )
                }

                // Vertical Divider
                Divider(
                    modifier = Modifier
                        .height(20.dp)
                        .padding(horizontal = IVISpacing.XS),
                    color = IVIColors.OnSurfaceDim
                )

                // System Clock
                Text(
                    text = time,
                    style = IVITypography.TitleMedium,
                    color = IVIColors.OnSurface,
                )
            }
        }
    }
}
