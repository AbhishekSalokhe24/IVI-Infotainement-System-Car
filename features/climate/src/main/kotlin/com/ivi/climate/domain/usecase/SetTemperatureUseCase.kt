package com.ivi.climate.domain.usecase

import com.ivi.climate.domain.repository.ClimateRepository
import javax.inject.Inject

enum class ClimateZone {
    DRIVER, PASSENGER
}

class SetTemperatureUseCase @Inject constructor(
    private val repository: ClimateRepository
) {
    suspend operator fun invoke(zone: ClimateZone, temperature: Float) {
        when (zone) {
            ClimateZone.DRIVER -> repository.setDriverTemperature(temperature)
            ClimateZone.PASSENGER -> repository.setPassengerTemperature(temperature)
        }
    }
}
