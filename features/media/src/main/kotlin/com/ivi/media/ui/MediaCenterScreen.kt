package com.ivi.media.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ivi.designsystem.components.IVIButton
import com.ivi.designsystem.components.IVICard
import com.ivi.designsystem.components.IVIIconButton
import com.ivi.designsystem.components.IVISlider
import com.ivi.designsystem.theme.IVIColors
import com.ivi.designsystem.theme.IVIShapes
import com.ivi.designsystem.theme.IVISpacing
import com.ivi.designsystem.theme.IVITypography
import com.ivi.media.domain.model.MediaTab
import com.ivi.media.domain.model.RadioStation

@Composable
fun MediaCenterScreen(
    viewModel: MediaViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(IVIColors.Background)
            .padding(IVISpacing.LG),
        verticalArrangement = Arrangement.spacedBy(IVISpacing.MD)
    ) {
        // Tab Navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(IVISpacing.MD),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MediaTab.entries.forEach { tab ->
                val isSelected = uiState.selectedTab == tab
                IVIButton(
                    text = when (tab) {
                        MediaTab.NOW_PLAYING -> "Now Playing"
                        MediaTab.BROWSE -> "Sources & Library"
                        MediaTab.RADIO -> "Radio Tuner"
                    },
                    onClick = { viewModel.onAction(MediaAction.SelectTab(tab)) },
                    modifier = Modifier.weight(1f),
                    leadingIcon = {
                        Icon(
                            imageVector = when (tab) {
                                MediaTab.NOW_PLAYING -> Icons.Default.GraphicEq
                                MediaTab.BROWSE -> Icons.Default.LibraryMusic
                                MediaTab.RADIO -> Icons.Default.Radio
                            },
                            contentDescription = null,
                            tint = if (isSelected) IVIColors.Primary else IVIColors.OnSurfaceDim
                        )
                    }
                )
            }
        }

        // Active Tab Content
        when (uiState.selectedTab) {
            MediaTab.NOW_PLAYING -> {
                NowPlayingTab(uiState = uiState, viewModel = viewModel)
            }
            MediaTab.BROWSE -> {
                SourcesTab(uiState = uiState)
            }
            MediaTab.RADIO -> {
                RadioTab(
                    stations = uiState.radioStations,
                    onTune = { viewModel.onAction(MediaAction.TuneToStation(it)) }
                )
            }
        }
    }
}

@Composable
private fun NowPlayingTab(
    uiState: MediaUiState,
    viewModel: MediaViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = IVISpacing.MD),
        horizontalArrangement = Arrangement.spacedBy(IVISpacing.XL),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Album Art
        Box(
            modifier = Modifier
                .size(240.dp)
                .clip(IVIShapes.ExtraLarge)
                .background(IVIColors.SurfaceElevated),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Album,
                contentDescription = "Album Artwork",
                tint = IVIColors.Primary,
                modifier = Modifier.size(120.dp)
            )
        }

        // Track metadata and transport
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = uiState.currentTrack.title,
                style = IVITypography.HeadlineLarge,
                color = IVIColors.OnSurface
            )
            Spacer(Modifier.height(IVISpacing.XS))
            Text(
                text = "${uiState.currentTrack.artist} • ${uiState.currentTrack.album}",
                style = IVITypography.TitleLarge,
                color = IVIColors.OnSurfaceVariant
            )
            Spacer(Modifier.height(IVISpacing.MD))

            IVISlider(
                value = uiState.progress,
                onValueChange = { viewModel.onAction(MediaAction.SeekTo(it)) },
                valueRange = 0f..1f,
                label = "Progress",
                valueLabel = "${(uiState.progress * 100).toInt()}%"
            )

            Spacer(Modifier.height(IVISpacing.LG))

            Row(
                horizontalArrangement = Arrangement.spacedBy(IVISpacing.LG),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IVIIconButton(
                    icon = Icons.Default.SkipPrevious,
                    contentDescription = "Previous Track",
                    onClick = { viewModel.onAction(MediaAction.SkipPrevious) }
                )

                IVIIconButton(
                    icon = if (uiState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (uiState.isPlaying) "Pause" else "Play",
                    selected = true,
                    onClick = { viewModel.onAction(MediaAction.TogglePlayPause) }
                )

                IVIIconButton(
                    icon = Icons.Default.SkipNext,
                    contentDescription = "Next Track",
                    onClick = { viewModel.onAction(MediaAction.SkipNext) }
                )
            }
        }
    }
}

@Composable
private fun SourcesTab(uiState: MediaUiState) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(IVISpacing.MD),
        modifier = Modifier.fillMaxSize()
    ) {
        items(uiState.sources) { source ->
            IVICard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(IVISpacing.LG),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = source.name,
                            style = IVITypography.TitleLarge,
                            color = IVIColors.OnSurface
                        )
                        Text(
                            text = "Audio Source • ${source.type.name}",
                            style = IVITypography.BodyMedium,
                            color = IVIColors.OnSurfaceVariant
                        )
                    }
                    Text(
                        text = if (source.isConnected) "CONNECTED" else "AVAILABLE",
                        style = IVITypography.LabelLarge,
                        color = if (source.isConnected) IVIColors.Success else IVIColors.OnSurfaceDim
                    )
                }
            }
        }
    }
}

@Composable
private fun RadioTab(
    stations: List<RadioStation>,
    onTune: (RadioStation) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(IVISpacing.MD),
        modifier = Modifier.fillMaxSize()
    ) {
        items(stations) { station ->
            IVICard(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onTune(station) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(IVISpacing.LG),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "${station.frequencyMhz} MHz — ${station.name}",
                            style = IVITypography.TitleLarge,
                            color = IVIColors.OnSurface
                        )
                        Text(
                            text = "Format: ${station.genre}",
                            style = IVITypography.BodyMedium,
                            color = IVIColors.OnSurfaceVariant
                        )
                    }
                    if (station.isFavorite) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Favorite Station",
                            tint = IVIColors.Warning
                        )
                    }
                }
            }
        }
    }
}
