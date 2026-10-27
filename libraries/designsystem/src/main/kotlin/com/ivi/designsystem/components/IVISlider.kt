package com.ivi.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.ivi.designsystem.theme.IVIColors
import com.ivi.designsystem.theme.IVIDimensions
import com.ivi.designsystem.theme.IVISpacing
import com.ivi.designsystem.theme.IVITypography

@Composable
fun IVISlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    label: String? = null,
    valueLabel: String? = null,
    enabled: Boolean = true,
) {
    val haptic = LocalHapticFeedback.current

    Column(modifier = modifier) {
        if (label != null || valueLabel != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (label != null) {
                    Text(
                        text = label,
                        style = IVITypography.LabelLarge,
                        color = IVIColors.OnSurface,
                    )
                }
                if (valueLabel != null) {
                    Text(
                        text = valueLabel,
                        style = IVITypography.TitleMedium,
                        color = IVIColors.Primary,
                    )
                }
            }
            Spacer(Modifier.height(IVISpacing.SM))
        }

        Slider(
            value = value,
            onValueChange = { newValue ->
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onValueChange(newValue)
            },
            valueRange = valueRange,
            steps = steps,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(IVIDimensions.TouchTargetMin),
            colors = SliderDefaults.colors(
                thumbColor = IVIColors.Primary,
                activeTrackColor = IVIColors.Primary,
                inactiveTrackColor = IVIColors.SurfaceBright,
            )
        )
    }
}
