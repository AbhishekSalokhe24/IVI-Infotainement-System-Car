package com.ivi.carservice

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

data class VehicleProperties(
    val speedKmh: Float = 65.0f,
    val fuelLevel: Float = 0.72f,      // 0.0 to 1.0 (72%)
    val range: String = "420 km",
    val tirePressureOk: Boolean = true,
    val gear: String = "D",
    val batteryLevel: Int = 85,
    val outsideTemperature: Float = 24.5f,
)

object VehiclePropertyIds {
    const val PERF_VEHICLE_SPEED = 0x11600207
    const val FUEL_LEVEL = 0x11600205
    const val RANGE_REMAINING = 0x11600208
    const val TIRE_PRESSURE = 0x11600301
    const val GEAR_SELECTION = 0x11400400
}

@Singleton
class VehiclePropertyClient @Inject constructor() {

    private val _properties = MutableStateFlow(VehicleProperties())
    val properties: Flow<VehicleProperties> = _properties.asStateFlow()

    fun observeProperties(propertyIds: List<Int>): Flow<VehicleProperties> {
        return _properties.asStateFlow()
    }

    fun updateSpeed(speedKmh: Float) {
        _properties.update { it.copy(speedKmh = speedKmh) }
    }

    fun updateFuelLevel(fuelLevel: Float) {
        _properties.update { it.copy(fuelLevel = fuelLevel) }
    }

    fun updateGear(gear: String) {
        _properties.update { it.copy(gear = gear) }
    }
}
