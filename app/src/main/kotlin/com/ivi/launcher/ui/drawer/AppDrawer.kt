package com.ivi.launcher.ui.drawer

import android.graphics.drawable.Drawable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ivi.designsystem.components.IVIIconButton
import com.ivi.designsystem.theme.IVIColors
import com.ivi.designsystem.theme.IVIDimensions
import com.ivi.designsystem.theme.IVIShapes
import com.ivi.designsystem.theme.IVISpacing
import com.ivi.designsystem.theme.IVITypography

data class AppInfo(
    val packageName: String,
    val label: String,
    val icon: Drawable? = null,
)

@Composable
fun AppDrawer(
    apps: List<AppInfo>,
    onAppClick: (AppInfo) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = IVIColors.Background.copy(alpha = 0.96f),
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
                    text = "Automotive Applications",
                    style = IVITypography.HeadlineMedium,
                    color = IVIColors.OnSurface,
                )
                IVIIconButton(
                    icon = Icons.Default.Close,
                    contentDescription = "Close App Drawer",
                    onClick = onDismiss,
                )
            }

            Spacer(Modifier.height(IVISpacing.LG))

            // App Grid
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 130.dp),
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
        Icon(
            imageVector = Icons.Default.DirectionsCar,
            contentDescription = appInfo.label,
            tint = IVIColors.Primary,
            modifier = Modifier.size(IVIDimensions.IconLarge)
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
