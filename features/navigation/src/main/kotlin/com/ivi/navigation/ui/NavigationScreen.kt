package com.ivi.navigation.ui

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EvStation
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.ivi.designsystem.theme.IVIColors
import com.ivi.designsystem.theme.IVIShapes
import com.ivi.designsystem.theme.IVISpacing
import com.ivi.designsystem.theme.IVITypography
import com.ivi.navigation.domain.model.POI
import com.ivi.navigation.ui.map.MapAction
import com.ivi.navigation.ui.map.MapViewModel

@Composable
fun NavigationScreen(
    viewModel: MapViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(IVIColors.Background)
    ) {
        // Map Canvas / Simulated 3D Vector Map Surface
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(IVIColors.SurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Navigation,
                    contentDescription = null,
                    tint = IVIColors.Primary,
                    modifier = Modifier.size(96.dp)
                )
                Spacer(Modifier.height(IVISpacing.SM))
                Text(
                    text = "MapLibre Automotive 3D Engine",
                    style = IVITypography.HeadlineMedium,
                    color = IVIColors.OnSurface
                )
                Text(
                    text = "GPS: ${uiState.currentLocation.latitude}, ${uiState.currentLocation.longitude} • Zoom: ${uiState.zoomLevel.toInt()}x",
                    style = IVITypography.BodyMedium,
                    color = IVIColors.OnSurfaceVariant
                )
            }
        }

        // Top Search Bar
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(IVISpacing.LG)
                .fillMaxWidth(0.65f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(IVISpacing.SM)
        ) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.onAction(MapAction.SearchQueryChanged(it)) },
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp),
                placeholder = { Text("Search EV charging, addresses, POIs...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = IVIColors.Primary) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = IVIColors.Surface.copy(alpha = 0.95f),
                    unfocusedContainerColor = IVIColors.Surface.copy(alpha = 0.95f),
                    focusedTextColor = IVIColors.OnSurface,
                    unfocusedTextColor = IVIColors.OnSurface,
                    focusedBorderColor = IVIColors.Primary,
                    unfocusedBorderColor = IVIColors.SurfaceElevated,
                ),
                shape = IVIShapes.Large,
                singleLine = true
            )
        }

        // Search Results Drawer / Overlay (if query is typed)
        if (uiState.searchQuery.isNotBlank() && uiState.searchResults.isNotEmpty()) {
            IVICard(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 90.dp)
                    .fillMaxWidth(0.65f)
            ) {
                LazyColumn(modifier = Modifier.padding(IVISpacing.MD)) {
                    items(uiState.searchResults) { poi ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.onAction(MapAction.SelectPOI(poi))
                                    viewModel.onAction(MapAction.SearchQueryChanged(""))
                                }
                                .padding(vertical = IVISpacing.SM),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(IVISpacing.MD)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EvStation,
                                    contentDescription = null,
                                    tint = IVIColors.Primary
                                )
                                Column {
                                    Text(
                                        text = poi.name,
                                        style = IVITypography.TitleMedium,
                                        color = IVIColors.OnSurface
                                    )
                                    Text(
                                        text = "${poi.address} • ${poi.category}",
                                        style = IVITypography.BodyMedium,
                                        color = IVIColors.OnSurfaceVariant
                                    )
                                }
                            }
                            Text(
                                text = "${poi.distanceKm} km",
                                style = IVITypography.LabelLarge,
                                color = IVIColors.Primary
                            )
                        }
                    }
                }
            }
        }

        // Active Turn-by-Turn Guidance Maneuver Card (Top Left)
        if (uiState.isNavigating && uiState.activeRoute != null) {
            val route = uiState.activeRoute!!
            IVICard(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(IVISpacing.LG)
                    .width(380.dp)
            ) {
                Column(modifier = Modifier.padding(IVISpacing.LG)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TURN-BY-TURN GUIDANCE",
                            style = IVITypography.LabelSmall,
                            color = IVIColors.Primary
                        )
                        IVIIconButton(
                            icon = Icons.Default.Close,
                            contentDescription = "Stop Navigation",
                            onClick = { viewModel.onAction(MapAction.StopNavigation) },
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(Modifier.height(IVISpacing.SM))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(IVISpacing.MD)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AltRoute,
                            contentDescription = null,
                            tint = IVIColors.Primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Column {
                            Text(
                                text = route.currentManeuver.instruction,
                                style = IVITypography.TitleLarge,
                                color = IVIColors.OnSurface
                            )
                            Text(
                                text = "ETA ${route.etaMinutes} min • ${route.distanceRemainingKm} km remaining",
                                style = IVITypography.BodyMedium,
                                color = IVIColors.Success
                            )
                        }
                    }
                }
            }
        }

        // Right Map Viewport Controls
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(IVISpacing.LG),
            verticalArrangement = Arrangement.spacedBy(IVISpacing.SM)
        ) {
            IVIIconButton(
                icon = Icons.Default.MyLocation,
                contentDescription = "My Location",
                onClick = { viewModel.onAction(MapAction.RecenterLocation) }
            )
            IVIIconButton(
                icon = Icons.Default.Add,
                contentDescription = "Zoom In",
                onClick = { viewModel.onAction(MapAction.ZoomIn) }
            )
            IVIIconButton(
                icon = Icons.Default.Remove,
                contentDescription = "Zoom Out",
                onClick = { viewModel.onAction(MapAction.ZoomOut) }
            )
            IVIIconButton(
                icon = Icons.Default.Terrain,
                contentDescription = "Toggle 3D View",
                selected = uiState.is3DMode,
                onClick = { viewModel.onAction(MapAction.Toggle3D) }
            )
        }
    }
}
