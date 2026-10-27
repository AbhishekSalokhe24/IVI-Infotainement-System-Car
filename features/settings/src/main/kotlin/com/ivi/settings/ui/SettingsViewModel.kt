package com.ivi.settings.ui

import androidx.lifecycle.viewModelScope
import com.ivi.core.base.BaseViewModel
import com.ivi.core.base.UiState
import com.ivi.settings.domain.model.ConnectivitySettings
import com.ivi.settings.domain.model.DisplaySettings
import com.ivi.settings.domain.model.SettingsCategory
import com.ivi.settings.domain.model.SoundSettings
import com.ivi.settings.domain.model.UserProfile
import com.ivi.settings.domain.model.VehicleSettingsState
import com.ivi.settings.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val selectedCategory: SettingsCategory = SettingsCategory.DISPLAY,
    val display: DisplaySettings = DisplaySettings(),
    val sound: SoundSettings = SoundSettings(),
    val connectivity: ConnectivitySettings = ConnectivitySettings(),
    val vehicle: VehicleSettingsState = VehicleSettingsState(),
    val profiles: List<UserProfile> = emptyList(),
    val error: String? = null
) : UiState

sealed interface SettingsAction {
    data class SelectCategory(val category: SettingsCategory) : SettingsAction
    data class SetBrightness(val brightness: Float) : SettingsAction
    data object ToggleAutoBrightness : SettingsAction
    data object ToggleNightMode : SettingsAction
    data class SetMasterVolume(val volume: Float) : SettingsAction
    data object ToggleSpeedCompensatedVolume : SettingsAction
    data object ToggleWifi : SettingsAction
    data object ToggleBluetooth : SettingsAction
    data object ToggleAutoDoorLock : SettingsAction
    data class SelectProfile(val profileId: String) : SettingsAction
}

sealed interface SettingsEvent

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
) : BaseViewModel<SettingsUiState, SettingsEvent, SettingsAction>(SettingsUiState()) {

    init {
        observeSettings()
    }

    private fun observeSettings() {
        viewModelScope.launch {
            repository.observeDisplaySettings().collect { display ->
                updateState { copy(display = display) }
            }
        }
        viewModelScope.launch {
            repository.observeSoundSettings().collect { sound ->
                updateState { copy(sound = sound) }
            }
        }
        viewModelScope.launch {
            repository.observeConnectivitySettings().collect { conn ->
                updateState { copy(connectivity = conn) }
            }
        }
        viewModelScope.launch {
            repository.observeVehicleSettings().collect { veh ->
                updateState { copy(vehicle = veh) }
            }
        }
        viewModelScope.launch {
            repository.observeProfiles().collect { list ->
                updateState { copy(profiles = list) }
            }
        }
    }

    override fun onAction(action: SettingsAction) {
        viewModelScope.launch {
            when (action) {
                is SettingsAction.SelectCategory -> updateState { copy(selectedCategory = action.category) }
                is SettingsAction.SetBrightness -> repository.setBrightness(action.brightness)
                is SettingsAction.ToggleAutoBrightness -> repository.toggleAutoBrightness()
                is SettingsAction.ToggleNightMode -> repository.toggleNightMode()
                is SettingsAction.SetMasterVolume -> repository.setVolume(action.volume)
                is SettingsAction.ToggleSpeedCompensatedVolume -> repository.toggleSpeedCompensatedVolume()
                is SettingsAction.ToggleWifi -> repository.toggleWifi()
                is SettingsAction.ToggleBluetooth -> repository.toggleBluetooth()
                is SettingsAction.ToggleAutoDoorLock -> repository.toggleAutoDoorLock()
                is SettingsAction.SelectProfile -> repository.selectProfile(action.profileId)
            }
        }
    }
}
