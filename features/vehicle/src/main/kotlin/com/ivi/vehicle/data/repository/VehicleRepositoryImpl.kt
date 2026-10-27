package com.ivi.vehicle.data.repository

import com.ivi.carservice.VehiclePropertyClient
import com.ivi.vehicle.domain.model.DriveMode
import com.ivi.vehicle.domain.model.VehicleStatus
import com.ivi.vehicle.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VehicleRepositoryImpl @Inject constructor(
    private val client: VehiclePropertyClient
) : VehicleRepository {

    private val currentDriveMode = MutableStateFlow(DriveMode.COMFORT)

    override fun observeVehicleStatus(): Flow<VehicleStatus> {
        return combine(client.properties, currentDriveMode) { props, mode ->
            VehicleStatus(
                speedKmh = props.speedKmh,
                fuelLevel = props.fuelLevel,
                rangeRemaining = props.range,
                batteryLevel = props.batteryLevel,
                areTiresOk = props.tirePressureOk,
                gear = props.gear,
                driveMode = mode
            )
        }
    }

    override suspend fun setDriveMode(mode: DriveMode) {
        currentDriveMode.value = mode
    }
}
