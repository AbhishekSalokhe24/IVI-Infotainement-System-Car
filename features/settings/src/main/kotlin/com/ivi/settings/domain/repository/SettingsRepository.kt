package com.ivi.settings.domain.repository

import com.ivi.settings.domain.model.ConnectivitySettings
import com.ivi.settings.domain.model.DisplaySettings
import com.ivi.settings.domain.model.SoundSettings
import com.ivi.settings.domain.model.UserProfile
import com.ivi.settings.domain.model.VehicleSettingsState
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun observeDisplaySettings(): Flow<DisplaySettings>
    fun observeSoundSettings(): Flow<SoundSettings>
    fun observeConnectivitySettings(): Flow<ConnectivitySettings>
    fun observeVehicleSettings(): Flow<VehicleSettingsState>
    fun observeProfiles(): Flow<List<UserProfile>>

    suspend fun setBrightness(brightness: Float)
    suspend fun toggleAutoBrightness()
    suspend fun toggleNightMode()
    suspend fun setVolume(masterVolume: Float)
    suspend fun toggleSpeedCompensatedVolume()
    suspend fun toggleWifi()
    suspend fun toggleBluetooth()
    suspend fun toggleAutoDoorLock()
    suspend fun selectProfile(profileId: String)
}
