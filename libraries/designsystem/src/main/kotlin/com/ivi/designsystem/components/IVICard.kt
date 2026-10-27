package com.ivi.designsystem.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.ivi.designsystem.theme.IVIColors
import com.ivi.designsystem.theme.IVIDimensions
import com.ivi.designsystem.theme.IVIShapes

@Composable
fun IVICard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = IVIShapes.Large
    val colors = CardDefaults.cardColors(
        containerColor = IVIColors.SurfaceVariant,
        contentColor = IVIColors.OnSurface,
    )

    if (onClick != null) {
        ElevatedCard(
            onClick = onClick,
            modifier = modifier
                .defaultMinSize(minHeight = IVIDimensions.CardMinHeight)
                .clip(shape),
            shape = shape,
            colors = colors,
            content = content,
        )
    } else {
        ElevatedCard(
            modifier = modifier
                .defaultMinSize(minHeight = IVIDimensions.CardMinHeight)
                .clip(shape),
            shape = shape,
            colors = colors,
            content = content,
        )
    }
}
