package com.ivi.launcher.ui.home

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ivi.carservice.MediaInfo
import com.ivi.designsystem.components.IVICard
import com.ivi.designsystem.components.IVIIconButton
import com.ivi.designsystem.theme.IVIColors
import com.ivi.designsystem.theme.IVISpacing
import com.ivi.designsystem.theme.IVITypography
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToMedia: () -> Unit,
    onNavigateToNavigation: () -> Unit,
    onNavigateToClimate: () -> Unit,
    onNavigateToVehicle: () -> Unit,
    onNavigateToPhone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier
            .fillMaxSize()
            .background(IVIColors.Background)
            .padding(IVISpacing.LG),
        verticalArrangement = Arrangement.spacedBy(IVISpacing.MD),
        horizontalArrangement = Arrangement.spacedBy(IVISpacing.MD),
    ) {
        // Greeting & Time Widget (Spans 3 Columns)
        item(span = { GridItemSpan(3) }) {
            GreetingWidget(
                greeting = uiState.greeting,
                dateTime = uiState.dateTime,
            )
        }

        // Now Playing Mini-Card (Spans 2 columns)
        if (uiState.nowPlaying != null) {
            item(span = { GridItemSpan(2) }) {
                NowPlayingCard(
                    mediaInfo = uiState.nowPlaying!!,
                    onTap = onNavigateToMedia,
                    onPlayPause = { viewModel.onAction(HomeAction.TogglePlayback) },
                    onNext = { viewModel.onAction(HomeAction.NextTrack) },
                )
            }
        }

        // Climate Quick Card
        item {
            ClimateQuickCard(
                temperature = uiState.temperature,
                isACOn = uiState.isACOn,
                onTap = onNavigateToClimate,
            )
        }

        // Navigation Quick Card
        item {
            NavigationQuickCard(
                destination = uiState.lastDestination ?: "Home",
                eta = uiState.eta ?: "--",
                onTap = onNavigateToNavigation,
            )
        }

        // Phone Quick Card
        item {
            PhoneQuickCard(
                recentContact = uiState.recentContact ?: "No Recent Calls",
                missedCalls = uiState.missedCallCount,
                onTap = onNavigateToPhone,
            )
        }

        // Vehicle Status Card
        item {
            VehicleStatusCard(
                fuelLevel = uiState.fuelLevel,
                range = uiState.range,
                tirePressureOk = uiState.tirePressureOk,
                onTap = onNavigateToVehicle,
            )
        }
    }
}

@Composable
private fun GreetingWidget(
    greeting: String,
    dateTime: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = IVISpacing.XS),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = greeting,
                style = IVITypography.HeadlineLarge,
                color = IVIColors.OnSurface
            )
            Text(
                text = dateTime,
                style = IVITypography.TitleMedium,
                color = IVIColors.OnSurfaceVariant
            )
        }
    }
}

@Composable
private fun NowPlayingCard(
    mediaInfo: MediaInfo,
    onTap: () -> Unit,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
) {
    IVICard(onClick = onTap) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(IVISpacing.LG),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "NOW PLAYING",
                    style = IVITypography.LabelSmall,
                    color = IVIColors.Primary
                )
                Text(
                    text = mediaInfo.title,
                    style = IVITypography.HeadlineSmall,
                    color = IVIColors.OnSurface
                )
                Text(
                    text = "${mediaInfo.artist} • ${mediaInfo.source}",
                    style = IVITypography.BodyMedium,
                    color = IVIColors.OnSurfaceVariant
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(IVISpacing.SM)
            ) {
                IVIIconButton(
                    icon = if (mediaInfo.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (mediaInfo.isPlaying) "Pause" else "Play",
                    selected = true,
                    onClick = onPlayPause
                )
                IVIIconButton(
                    icon = Icons.Default.SkipNext,
                    contentDescription = "Skip Track",
                    onClick = onNext
                )
            }
        }
    }
}

@Composable
private fun ClimateQuickCard(
    temperature: Float,
    isACOn: Boolean,
    onTap: () -> Unit,
) {
    IVICard(onClick = onTap) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(IVISpacing.LG),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "CABIN CLIMATE",
                    style = IVITypography.LabelSmall,
                    color = IVIColors.OnSurfaceVariant
                )
                Icon(
                    imageVector = Icons.Default.AcUnit,
                    contentDescription = null,
                    tint = if (isACOn) IVIColors.ClimateCool else IVIColors.OnSurfaceDim,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = "${String.format(Locale.US, "%.1f", temperature)}°C",
                style = IVITypography.DisplayMedium,
                color = IVIColors.OnSurface
            )
            Text(
                text = if (isACOn) "A/C Active • Sync Zone" else "A/C Off",
                style = IVITypography.BodyMedium,
                color = IVIColors.OnSurfaceVariant
            )
        }
    }
}

@Composable
private fun NavigationQuickCard(
    destination: String,
    eta: String,
    onTap: () -> Unit,
) {
    IVICard(onClick = onTap) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(IVISpacing.LG),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "NEXT DESTINATION",
                    style = IVITypography.LabelSmall,
                    color = IVIColors.OnSurfaceVariant
                )
                Icon(
                    imageVector = Icons.Default.Navigation,
                    contentDescription = null,
                    tint = IVIColors.Primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = destination,
                style = IVITypography.TitleLarge,
                color = IVIColors.OnSurface
            )
            Text(
                text = "ETA: $eta",
                style = IVITypography.TitleMedium,
                color = IVIColors.Primary
            )
        }
    }
}

@Composable
private fun PhoneQuickCard(
    recentContact: String,
    missedCalls: Int,
    onTap: () -> Unit,
) {
    IVICard(onClick = onTap) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(IVISpacing.LG),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "RECENT CALL",
                    style = IVITypography.LabelSmall,
                    color = IVIColors.OnSurfaceVariant
                )
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = null,
                    tint = IVIColors.Primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = recentContact,
                style = IVITypography.TitleLarge,
                color = IVIColors.OnSurface
            )
            Text(
                text = if (missedCalls > 0) "$missedCalls Missed Calls" else "Connected (5G)",
                style = IVITypography.BodyMedium,
                color = if (missedCalls > 0) IVIColors.Warning else IVIColors.Success
            )
        }
    }
}

@Composable
private fun VehicleStatusCard(
    fuelLevel: Float,
    range: String,
    tirePressureOk: Boolean,
    onTap: () -> Unit,
) {
    IVICard(onClick = onTap) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(IVISpacing.LG),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "VEHICLE STATUS",
                    style = IVITypography.LabelSmall,
                    color = IVIColors.OnSurfaceVariant
                )
                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = null,
                    tint = IVIColors.Success,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = range,
                style = IVITypography.DisplayMedium,
                color = IVIColors.OnSurface
            )
            Text(
                text = "Battery ${(fuelLevel * 100).toInt()}% • TPMS ${if (tirePressureOk) "OK" else "Alert"}",
                style = IVITypography.BodyMedium,
                color = IVIColors.OnSurfaceVariant
            )
        }
    }
}
