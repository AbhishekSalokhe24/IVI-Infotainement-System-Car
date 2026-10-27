package com.ivi.launcher.ui.shell

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessHigh
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.ivi.designsystem.components.IVIIconButton
import com.ivi.designsystem.theme.IVIColors
import com.ivi.designsystem.theme.IVIDimensions
import com.ivi.designsystem.theme.IVISpacing
import com.ivi.designsystem.theme.IVITypography
import kotlin.math.roundToInt

@Composable
fun QuickControlsBar(
    temperature: Float,
    volume: Float,
    brightness: Float,
    isNightMode: Boolean,
    onTemperatureChange: (Float) -> Unit,
    onVolumeChange: (Float) -> Unit,
    onBrightnessChange: (Float) -> Unit,
    onToggleNightMode: () -> Unit,
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
            // Temperature Slider
            QuickControlSliderItem(
                icon = Icons.Default.Thermostat,
                label = "${temperature.roundToInt()}°C",
                value = temperature,
                valueRange = 16f..30f,
                onValueChange = onTemperatureChange,
                modifier = Modifier.weight(1f),
            )

            // Volume Slider
            QuickControlSliderItem(
                icon = Icons.Default.VolumeUp,
                label = "Vol ${(volume * 15).roundToInt()}",
                value = volume,
                valueRange = 0f..1f,
                onValueChange = onVolumeChange,
                modifier = Modifier.weight(1f),
            )

            // Brightness Slider
            QuickControlSliderItem(
                icon = Icons.Default.BrightnessHigh,
                label = "${(brightness * 100).roundToInt()}%",
                value = brightness,
                valueRange = 0f..1f,
                onValueChange = onBrightnessChange,
                modifier = Modifier.weight(1f),
            )

            // Day / Night Toggle
            IVIIconButton(
                icon = if (isNightMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                contentDescription = "Toggle Night Mode",
                selected = isNightMode,
                onClick = onToggleNightMode,
            )
        }
    }
}

@Composable
private fun QuickControlSliderItem(
    icon: ImageVector,
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(IVISpacing.SM)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = IVIColors.Primary,
            modifier = Modifier.height(28.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = IVITypography.LabelSmall,
                color = IVIColors.OnSurface
            )
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = valueRange,
                colors = SliderDefaults.colors(
                    thumbColor = IVIColors.Primary,
                    activeTrackColor = IVIColors.Primary,
                    inactiveTrackColor = IVIColors.SurfaceBright
                )
            )
        }
    }
}
