package com.ivi.launcher.ui.shell

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ivi.designsystem.components.IVIIconButton
import com.ivi.designsystem.theme.IVIColors
import com.ivi.designsystem.theme.IVIDimensions
import com.ivi.designsystem.theme.IVISpacing
import com.ivi.designsystem.theme.IVITypography
import com.ivi.launcher.navigation.IVIDestination

@Composable
fun IVINavigationRail(
    currentRoute: String,
    onNavigate: (IVIDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .width(IVIDimensions.NavRailWidth)
            .fillMaxHeight(),
        color = IVIColors.Surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = IVISpacing.SM),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                IVISpacing.XS,
                Alignment.Top
            ),
        ) {
            IVIDestination.entries.forEach { destination ->
                val selected = currentRoute == destination.route

                IVIIconButton(
                    icon = if (selected) destination.selectedIcon
                    else destination.unselectedIcon,
                    contentDescription = destination.label,
                    onClick = { onNavigate(destination) },
                    selected = selected,
                )

                Text(
                    text = destination.label,
                    style = IVITypography.LabelSmall,
                    color = if (selected) IVIColors.Primary
                    else IVIColors.OnSurfaceDim,
                )
            }
        }
    }
}
