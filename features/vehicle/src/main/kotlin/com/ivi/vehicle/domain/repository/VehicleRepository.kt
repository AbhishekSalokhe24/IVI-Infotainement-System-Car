package com.ivi.vehicle.domain.repository

import com.ivi.vehicle.domain.model.DriveMode
import com.ivi.vehicle.domain.model.VehicleStatus
import kotlinx.coroutines.flow.Flow

interface VehicleRepository {
    fun observeVehicleStatus(): Flow<VehicleStatus>
    suspend fun setDriveMode(mode: DriveMode)
}
