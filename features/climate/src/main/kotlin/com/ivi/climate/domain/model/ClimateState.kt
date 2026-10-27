package com.ivi.climate.domain.model

data class ClimateState(
    val driverTemperature: Float = 22.0f,    // °C
    val passengerTemperature: Float = 22.0f, // °C
    val fanSpeed: Int = 3,                   // 0-7 (0 = off)
    val isAutoMode: Boolean = true,
    val isACOn: Boolean = true,
    val isRecirculation: Boolean = false,
    val isSyncMode: Boolean = true,
    val airDistribution: AirDistribution = AirDistribution.AUTO,
    val driverSeatHeat: SeatHeatLevel = SeatHeatLevel.OFF,
    val passengerSeatHeat: SeatHeatLevel = SeatHeatLevel.OFF,
    val isRearDefrost: Boolean = false,
    val isSteeringWheelHeat: Boolean = false,
    val outsideTemperature: Float = 24.5f,   // °C
)

enum class AirDistribution {
    FACE, FEET, FACE_AND_FEET, DEFROST, AUTO
}

enum class SeatHeatLevel {
    OFF, LOW, MEDIUM, HIGH
}
