package com.ivi.testing

import com.ivi.carservice.CarClimateInfo
import com.ivi.carservice.MediaInfo
import com.ivi.carservice.VehicleProperties
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeCarService {
    val vehicleProperties = MutableStateFlow(VehicleProperties())
    val climateState = MutableStateFlow(CarClimateInfo())
    val nowPlaying = MutableStateFlow<MediaInfo?>(MediaInfo())

    fun emitSpeed(speed: Float) {
        vehicleProperties.value = vehicleProperties.value.copy(speedKmh = speed)
    }

    fun emitTemperature(driverTemp: Float, passengerTemp: Float) {
        climateState.value = climateState.value.copy(
            driverTemperature = driverTemp,
            passengerTemperature = passengerTemp
        )
    }
}
