package com.ivi.settings.data.repository

import com.ivi.settings.domain.model.ConnectivitySettings
import com.ivi.settings.domain.model.DisplaySettings
import com.ivi.settings.domain.model.SoundSettings
import com.ivi.settings.domain.model.UserProfile
import com.ivi.settings.domain.model.VehicleSettingsState
import com.ivi.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor() : SettingsRepository {

    private val displaySettings = MutableStateFlow(DisplaySettings())
    private val soundSettings = MutableStateFlow(SoundSettings())
    private val connectivitySettings = MutableStateFlow(ConnectivitySettings())
    private val vehicleSettings = MutableStateFlow(VehicleSettingsState())
    private val profiles = MutableStateFlow(
        listOf(
            UserProfile("p1", "Primary Driver (Alex)", "A", isCurrent = true, 1, 22.0f),
            UserProfile("p2", "Partner (Jordan)", "J", isCurrent = false, 2, 23.5f),
            UserProfile("p3", "Guest Driver", "G", isCurrent = false, 3, 21.0f),
        )
    )

    override fun observeDisplaySettings(): Flow<DisplaySettings> = displaySettings.asStateFlow()
    override fun observeSoundSettings(): Flow<SoundSettings> = soundSettings.asStateFlow()
    override fun observeConnectivitySettings(): Flow<ConnectivitySettings> = connectivitySettings.asStateFlow()
    override fun observeVehicleSettings(): Flow<VehicleSettingsState> = vehicleSettings.asStateFlow()
    override fun observeProfiles(): Flow<List<UserProfile>> = profiles.asStateFlow()

    override suspend fun setBrightness(brightness: Float) {
        displaySettings.update { it.copy(brightness = brightness) }
    }

    override suspend fun toggleAutoBrightness() {
        displaySettings.update { it.copy(isAutoBrightness = !it.isAutoBrightness) }
    }

    override suspend fun toggleNightMode() {
        displaySettings.update { it.copy(isNightMode = !it.isNightMode) }
    }

    override suspend fun setVolume(masterVolume: Float) {
        soundSettings.update { it.copy(masterVolume = masterVolume) }
    }

    override suspend fun toggleSpeedCompensatedVolume() {
        soundSettings.update { it.copy(speedCompensatedVolume = !it.speedCompensatedVolume) }
    }

    override suspend fun toggleWifi() {
        connectivitySettings.update { it.copy(isWifiEnabled = !it.isWifiEnabled) }
    }

    override suspend fun toggleBluetooth() {
        connectivitySettings.update { it.copy(isBluetoothEnabled = !it.isBluetoothEnabled) }
    }

    override suspend fun toggleAutoDoorLock() {
        vehicleSettings.update { it.copy(autoDoorLock = !it.autoDoorLock) }
    }

    override suspend fun selectProfile(profileId: String) {
        profiles.update { list ->
            list.map { it.copy(isCurrent = it.id == profileId) }
        }
    }
}
