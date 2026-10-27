package com.ivi.designsystem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.ivi.designsystem.theme.IVIDimensions
import com.ivi.designsystem.theme.IVIShapes
import com.ivi.designsystem.theme.IVISpacing

enum class IVIButtonSize {
    Medium,  // 80dp height
    Large    // 96dp height
}

@Composable
fun IVIButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: IVIButtonSize = IVIButtonSize.Medium,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    val haptic = LocalHapticFeedback.current
    val height = when (size) {
        IVIButtonSize.Medium -> IVIDimensions.TouchTargetDefault
        IVIButtonSize.Large -> IVIDimensions.TouchTargetLarge
    }

    Button(
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            onClick()
        },
        modifier = modifier.height(height),
        enabled = enabled,
        shape = IVIShapes.Large,
        contentPadding = PaddingValues(
            horizontal = IVISpacing.LG,
            vertical = IVISpacing.MD
        )
    ) {
        if (leadingIcon != null) {
            leadingIcon()
            Spacer(Modifier.width(IVISpacing.SM))
        }
        Text(text)
    }
}
