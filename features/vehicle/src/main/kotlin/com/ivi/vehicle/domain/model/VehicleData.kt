package com.ivi.vehicle.domain.model

data class VehicleStatus(
    val speedKmh: Float = 0f,
    val fuelLevel: Float = 0.72f,
    val rangeRemaining: String = "420 km",
    val batteryLevel: Int = 85,
    val tirePressureFrontLeftPsi: Float = 34.2f,
    val tirePressureFrontRightPsi: Float = 34.0f,
    val tirePressureRearLeftPsi: Float = 34.1f,
    val tirePressureRearRightPsi: Float = 34.3f,
    val areTiresOk: Boolean = true,
    val gear: String = "P",
    val odometerKm: Long = 18450L,
    val driveMode: DriveMode = DriveMode.COMFORT,
    val isDriverDoorOpen: Boolean = false,
    val isPassengerDoorOpen: Boolean = false,
)

enum class DriveMode {
    ECO, COMFORT, SPORT
}
