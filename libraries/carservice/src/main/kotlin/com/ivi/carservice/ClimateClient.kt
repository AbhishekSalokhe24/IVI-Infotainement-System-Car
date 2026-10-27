package com.ivi.carservice

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

data class CarClimateInfo(
    val driverTemperature: Float = 22.0f,
    val passengerTemperature: Float = 22.0f,
    val fanSpeed: Int = 3,
    val isAutoMode: Boolean = true,
    val isACOn: Boolean = true,
    val isRecirculation: Boolean = false,
    val isSyncMode: Boolean = true,
    val driverSeatHeat: Int = 0,    // 0 = Off, 1 = Low, 2 = Med, 3 = High
    val passengerSeatHeat: Int = 0,
    val isRearDefrost: Boolean = false,
    val isSteeringWheelHeat: Boolean = false,
)

@Singleton
class ClimateClient @Inject constructor() {

    private val _climateState = MutableStateFlow(CarClimateInfo())
    val climateState: Flow<CarClimateInfo> = _climateState.asStateFlow()

    fun setDriverTemperature(temp: Float) {
        _climateState.update {
            val updated = it.copy(driverTemperature = temp)
            if (it.isSyncMode) updated.copy(passengerTemperature = temp) else updated
        }
    }

    fun setPassengerTemperature(temp: Float) {
        _climateState.update { it.copy(passengerTemperature = temp) }
    }

    fun setFanSpeed(speed: Int) {
        _climateState.update { it.copy(fanSpeed = speed.coerceIn(0, 7)) }
    }

    fun toggleAC() {
        _climateState.update { it.copy(isACOn = !it.isACOn) }
    }

    fun toggleAutoMode() {
        _climateState.update { it.copy(isAutoMode = !it.isAutoMode) }
    }

    fun toggleRecirculation() {
        _climateState.update { it.copy(isRecirculation = !it.isRecirculation) }
    }

    fun toggleSync() {
        _climateState.update {
            val newSync = !it.isSyncMode
            if (newSync) it.copy(isSyncMode = true, passengerTemperature = it.driverTemperature)
            else it.copy(isSyncMode = false)
        }
    }

    fun cycleDriverSeatHeat() {
        _climateState.update { it.copy(driverSeatHeat = (it.driverSeatHeat + 1) % 4) }
    }

    fun cyclePassengerSeatHeat() {
        _climateState.update { it.copy(passengerSeatHeat = (it.passengerSeatHeat + 1) % 4) }
    }
}
