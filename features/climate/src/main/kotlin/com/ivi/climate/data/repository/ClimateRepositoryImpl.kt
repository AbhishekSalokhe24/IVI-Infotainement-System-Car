package com.ivi.climate.data.repository

import com.ivi.carservice.ClimateClient
import com.ivi.climate.domain.model.AirDistribution
import com.ivi.climate.domain.model.ClimateState
import com.ivi.climate.domain.model.SeatHeatLevel
import com.ivi.climate.domain.repository.ClimateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClimateRepositoryImpl @Inject constructor(
    private val climateClient: ClimateClient
) : ClimateRepository {

    override fun observeClimateState(): Flow<ClimateState> {
        return climateClient.climateState.map { info ->
            ClimateState(
                driverTemperature = info.driverTemperature,
                passengerTemperature = info.passengerTemperature,
                fanSpeed = info.fanSpeed,
                isAutoMode = info.isAutoMode,
                isACOn = info.isACOn,
                isRecirculation = info.isRecirculation,
                isSyncMode = info.isSyncMode,
                airDistribution = AirDistribution.AUTO,
                driverSeatHeat = when (info.driverSeatHeat) {
                    1 -> SeatHeatLevel.LOW
                    2 -> SeatHeatLevel.MEDIUM
                    3 -> SeatHeatLevel.HIGH
                    else -> SeatHeatLevel.OFF
                },
                passengerSeatHeat = when (info.passengerSeatHeat) {
                    1 -> SeatHeatLevel.LOW
                    2 -> SeatHeatLevel.MEDIUM
                    3 -> SeatHeatLevel.HIGH
                    else -> SeatHeatLevel.OFF
                },
                isRearDefrost = info.isRearDefrost,
                isSteeringWheelHeat = info.isSteeringWheelHeat,
                outsideTemperature = 24.5f
            )
        }
    }

    override suspend fun setDriverTemperature(temp: Float) {
        climateClient.setDriverTemperature(temp)
    }

    override suspend fun setPassengerTemperature(temp: Float) {
        climateClient.setPassengerTemperature(temp)
    }

    override suspend fun setFanSpeed(speed: Int) {
        climateClient.setFanSpeed(speed)
    }

    override suspend fun toggleAC() {
        climateClient.toggleAC()
    }

    override suspend fun toggleAutoMode() {
        climateClient.toggleAutoMode()
    }

    override suspend fun toggleSync() {
        climateClient.toggleSync()
    }

    override suspend fun cycleDriverSeatHeat() {
        climateClient.cycleDriverSeatHeat()
    }

    override suspend fun cyclePassengerSeatHeat() {
        climateClient.cyclePassengerSeatHeat()
    }
}
