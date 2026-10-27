package com.ivi.vehicle.ui

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TireRepair
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ivi.core.base.BaseViewModel
import com.ivi.core.base.UiState
import com.ivi.designsystem.components.IVIButton
import com.ivi.designsystem.components.IVICard
import com.ivi.designsystem.theme.IVIColors
import com.ivi.designsystem.theme.IVISpacing
import com.ivi.designsystem.theme.IVITypography
import com.ivi.vehicle.domain.model.DriveMode
import com.ivi.vehicle.domain.model.VehicleStatus
import com.ivi.vehicle.domain.repository.VehicleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VehicleUiState(
    val status: VehicleStatus = VehicleStatus(),
    val error: String? = null
) : UiState

sealed interface VehicleAction {
    data class SetDriveMode(val mode: DriveMode) : VehicleAction
}

sealed interface VehicleEvent

@HiltViewModel
class VehicleViewModel @Inject constructor(
    private val repository: VehicleRepository
) : BaseViewModel<VehicleUiState, VehicleEvent, VehicleAction>(VehicleUiState()) {

    init {
        observeStatus()
    }

    private fun observeStatus() {
        viewModelScope.launch {
            repository.observeVehicleStatus()
                .catch { e -> updateState { copy(error = e.message) } }
                .collect { status ->
                    updateState { copy(status = status, error = null) }
                }
        }
    }

    override fun onAction(action: VehicleAction) {
        viewModelScope.launch {
            when (action) {
                is VehicleAction.SetDriveMode -> repository.setDriveMode(action.mode)
            }
        }
    }
}

@Composable
fun VehicleStatusScreen(
    viewModel: VehicleViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val status = uiState.status

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(IVIColors.Background)
            .padding(IVISpacing.LG),
        verticalArrangement = Arrangement.spacedBy(IVISpacing.LG)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Vehicle Status & Diagnostics",
                style = IVITypography.HeadlineLarge,
                color = IVIColors.OnSurface
            )
            Text(
                text = "Odometer: ${status.odometerKm} km",
                style = IVITypography.TitleMedium,
                color = IVIColors.OnSurfaceVariant
            )
        }

        // Metrics Grid (Speed, Energy, TPMS)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(IVISpacing.LG)
        ) {
            // Speedometer card
            IVICard(modifier = Modifier.weight(1f)) {
                Column(
                    modifier = Modifier.padding(IVISpacing.LG),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = "Vehicle Speed",
                        tint = IVIColors.Primary,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(Modifier.height(IVISpacing.XS))
                    Text(
                        text = "${status.speedKmh.toInt()} km/h",
                        style = IVITypography.DisplayLarge,
                        color = IVIColors.OnSurface
                    )
                    Text(
                        text = "Gear: ${status.gear}",
                        style = IVITypography.TitleMedium,
                        color = IVIColors.Primary
                    )
                }
            }

            // Energy & Range Card
            IVICard(modifier = Modifier.weight(1f)) {
                Column(
                    modifier = Modifier.padding(IVISpacing.LG),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.BatteryChargingFull,
                        contentDescription = "Battery and Range",
                        tint = IVIColors.Success,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(Modifier.height(IVISpacing.XS))
                    Text(
                        text = status.rangeRemaining,
                        style = IVITypography.DisplayMedium,
                        color = IVIColors.OnSurface
                    )
                    Text(
                        text = "EV Battery: ${status.batteryLevel}%",
                        style = IVITypography.TitleMedium,
                        color = IVIColors.OnSurfaceVariant
                    )
                }
            }

            // Tire Pressure Monitor Card
            IVICard(modifier = Modifier.weight(1f)) {
                Column(
                    modifier = Modifier.padding(IVISpacing.LG),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.TireRepair,
                        contentDescription = "TPMS",
                        tint = if (status.areTiresOk) IVIColors.Success else IVIColors.Warning,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(Modifier.height(IVISpacing.XS))
                    Text(
                        text = if (status.areTiresOk) "NORMAL" else "CHECK",
                        style = IVITypography.DisplayMedium,
                        color = if (status.areTiresOk) IVIColors.Success else IVIColors.Warning
                    )
                    Text(
                        text = "FL: ${status.tirePressureFrontLeftPsi} | FR: ${status.tirePressureFrontRightPsi} PSI",
                        style = IVITypography.BodyMedium,
                        color = IVIColors.OnSurfaceVariant
                    )
                }
            }
        }

        // Drive Mode Selector
        IVICard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(IVISpacing.LG)) {
                Text(
                    text = "Powertrain & Drive Mode",
                    style = IVITypography.TitleLarge,
                    color = IVIColors.OnSurface
                )
                Spacer(Modifier.height(IVISpacing.MD))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(IVISpacing.MD)
                ) {
                    DriveMode.entries.forEach { mode ->
                        val isSelected = status.driveMode == mode
                        IVIButton(
                            text = mode.name,
                            onClick = { viewModel.onAction(VehicleAction.SetDriveMode(mode)) },
                            modifier = Modifier.weight(1f),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.DirectionsCar,
                                    contentDescription = null,
                                    tint = if (isSelected) IVIColors.Primary else IVIColors.OnSurfaceDim
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
