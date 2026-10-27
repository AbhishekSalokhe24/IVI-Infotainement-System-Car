package com.ivi.climate.domain.repository

import com.ivi.climate.domain.model.ClimateState
import kotlinx.coroutines.flow.Flow

interface ClimateRepository {
    fun observeClimateState(): Flow<ClimateState>
    suspend fun setDriverTemperature(temp: Float)
    suspend fun setPassengerTemperature(temp: Float)
    suspend fun setFanSpeed(speed: Int)
    suspend fun toggleAC()
    suspend fun toggleAutoMode()
    suspend fun toggleSync()
    suspend fun cycleDriverSeatHeat()
    suspend fun cyclePassengerSeatHeat()
}
