package com.ivi.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.ivi.designsystem.theme.IVIColors
import com.ivi.designsystem.theme.IVIDimensions
import com.ivi.designsystem.theme.IVITypography

@Composable
fun IVIToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val haptic = LocalHapticFeedback.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IVIDimensions.TouchTargetDefault),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = IVITypography.TitleMedium,
            color = if (enabled) IVIColors.OnSurface else IVIColors.OnSurfaceDim,
        )

        Switch(
            checked = checked,
            onCheckedChange = { isChecked ->
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onCheckedChange(isChecked)
            },
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = IVIColors.OnPrimary,
                checkedTrackColor = IVIColors.Primary,
                uncheckedThumbColor = IVIColors.OnSurfaceVariant,
                uncheckedTrackColor = IVIColors.SurfaceElevated,
            )
        )
    }
}
