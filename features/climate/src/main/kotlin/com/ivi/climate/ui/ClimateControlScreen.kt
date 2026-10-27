package com.ivi.climate.ui

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Chair
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Sync
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
import com.ivi.climate.domain.model.ClimateState
import com.ivi.designsystem.components.IVIButton
import com.ivi.designsystem.components.IVICard
import com.ivi.designsystem.components.IVIIconButton
import com.ivi.designsystem.components.IVISlider
import com.ivi.designsystem.theme.IVIColors
import com.ivi.designsystem.theme.IVIDimensions
import com.ivi.designsystem.theme.IVIShapes
import com.ivi.designsystem.theme.IVISpacing
import com.ivi.designsystem.theme.IVITypography
import java.util.Locale

@Composable
fun ClimateControlScreen(
    viewModel: ClimateViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ClimateControlContent(
        climate = uiState.climate,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
fun ClimateControlContent(
    climate: ClimateState,
    onAction: (ClimateAction) -> Unit,
    modifier: Modifier = Modifier
) {
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
                text = "Climate Control",
                style = IVITypography.HeadlineLarge,
                color = IVIColors.OnSurface
            )
            Text(
                text = "Outside: ${String.format(Locale.US, "%.1f", climate.outsideTemperature)}°C",
                style = IVITypography.TitleMedium,
                color = IVIColors.OnSurfaceVariant
            )
        }

        // Zone Temperatures + Visual
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(IVISpacing.LG)
        ) {
            // Driver Zone
            ZoneTempCard(
                title = "Driver Zone",
                temperature = climate.driverTemperature,
                seatHeatLevel = climate.driverSeatHeat.name,
                onTempDecrease = {
                    onAction(ClimateAction.SetDriverTemp((climate.driverTemperature - 0.5f).coerceAtLeast(16f)))
                },
                onTempIncrease = {
                    onAction(ClimateAction.SetDriverTemp((climate.driverTemperature + 0.5f).coerceAtMost(30f)))
                },
                onSeatHeatClick = { onAction(ClimateAction.CycleDriverSeatHeat) },
                modifier = Modifier.weight(1f)
            )

            // Vehicle Center HVAC Display
            IVICard(
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(IVISpacing.MD),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Air,
                        contentDescription = "Airflow Distribution",
                        tint = if (climate.isACOn) IVIColors.ClimateCool else IVIColors.OnSurfaceDim,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(Modifier.height(IVISpacing.SM))
                    Text(
                        text = if (climate.isAutoMode) "AUTO CLIMATE ACTIVE" else "MANUAL AIRFLOW",
                        style = IVITypography.LabelLarge,
                        color = if (climate.isAutoMode) IVIColors.Primary else IVIColors.OnSurfaceVariant
                    )
                    Spacer(Modifier.height(IVISpacing.XS))
                    Text(
                        text = "Distribution: ${climate.airDistribution.name}",
                        style = IVITypography.BodyMedium,
                        color = IVIColors.OnSurfaceVariant
                    )
                }
            }

            // Passenger Zone
            ZoneTempCard(
                title = "Passenger Zone",
                temperature = climate.passengerTemperature,
                seatHeatLevel = climate.passengerSeatHeat.name,
                onTempDecrease = {
                    onAction(ClimateAction.SetPassengerTemp((climate.passengerTemperature - 0.5f).coerceAtLeast(16f)))
                },
                onTempIncrease = {
                    onAction(ClimateAction.SetPassengerTemp((climate.passengerTemperature + 0.5f).coerceAtMost(30f)))
                },
                onSeatHeatClick = { onAction(ClimateAction.CyclePassengerSeatHeat) },
                modifier = Modifier.weight(1f)
            )
        }

        // Fan Speed Slider
        IVISlider(
            value = climate.fanSpeed.toFloat(),
            onValueChange = { onAction(ClimateAction.SetFanSpeed(it.toInt())) },
            valueRange = 0f..7f,
            steps = 6,
            label = "Fan Speed",
            valueLabel = if (climate.fanSpeed == 0) "OFF" else "Level ${climate.fanSpeed}"
        )

        // Bottom Action Bar: AC, Recirc, Sync, Auto
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(IVISpacing.MD),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IVIButton(
                text = if (climate.isACOn) "A/C ON" else "A/C OFF",
                onClick = { onAction(ClimateAction.ToggleAC) },
                leadingIcon = { Icon(Icons.Default.AcUnit, contentDescription = null) },
                modifier = Modifier.weight(1f)
            )

            IVIButton(
                text = if (climate.isAutoMode) "AUTO ON" else "AUTO OFF",
                onClick = { onAction(ClimateAction.ToggleAuto) },
                leadingIcon = { Icon(Icons.Default.Autorenew, contentDescription = null) },
                modifier = Modifier.weight(1f)
            )

            IVIButton(
                text = if (climate.isSyncMode) "SYNC ON" else "SYNC OFF",
                onClick = { onAction(ClimateAction.ToggleSync) },
                leadingIcon = { Icon(Icons.Default.Sync, contentDescription = null) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ZoneTempCard(
    title: String,
    temperature: Float,
    seatHeatLevel: String,
    onTempDecrease: () -> Unit,
    onTempIncrease: () -> Unit,
    onSeatHeatClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IVICard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(IVISpacing.MD),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = IVITypography.TitleMedium,
                color = IVIColors.OnSurfaceVariant
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(IVISpacing.MD)
            ) {
                IVIIconButton(
                    icon = Icons.Default.Remove,
                    contentDescription = "Decrease Temperature",
                    onClick = onTempDecrease
                )

                Text(
                    text = "${String.format(Locale.US, "%.1f", temperature)}°",
                    style = IVITypography.DisplayMedium,
                    color = IVIColors.OnSurface
                )

                IVIIconButton(
                    icon = Icons.Default.Add,
                    contentDescription = "Increase Temperature",
                    onClick = onTempIncrease
                )
            }

            IVIButton(
                text = "Seat: $seatHeatLevel",
                onClick = onSeatHeatClick,
                leadingIcon = { Icon(Icons.Default.Chair, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
