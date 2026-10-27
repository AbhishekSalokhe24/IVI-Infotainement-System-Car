package com.ivi.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import com.ivi.designsystem.theme.IVIColors
import com.ivi.designsystem.theme.IVIDimensions
import com.ivi.designsystem.theme.IVIShapes

@Composable
fun IVIIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    badge: String? = null,
    enabled: Boolean = true,
) {
    val backgroundColor = if (selected) {
        IVIColors.PrimaryContainer
    } else {
        IVIColors.Surface.copy(alpha = 0f)
    }
    val iconColor = if (selected) {
        IVIColors.Primary
    } else {
        IVIColors.OnSurfaceVariant
    }

    Box(
        modifier = modifier
            .size(IVIDimensions.TouchTargetDefault)
            .clip(IVIShapes.Large)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onClick,
            enabled = enabled,
        ) {
            if (badge != null) {
                BadgedBox(
                    badge = {
                        Badge { Text(badge) }
                    }
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = contentDescription,
                        tint = iconColor,
                        modifier = Modifier.size(IVIDimensions.IconMedium)
                    )
                }
            } else {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = iconColor,
                    modifier = Modifier.size(IVIDimensions.IconMedium)
                )
            }
        }
    }
}
