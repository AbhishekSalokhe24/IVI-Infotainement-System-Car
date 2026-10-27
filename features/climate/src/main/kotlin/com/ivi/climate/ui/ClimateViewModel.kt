package com.ivi.climate.ui

import androidx.lifecycle.viewModelScope
import com.ivi.climate.domain.model.ClimateState
import com.ivi.climate.domain.model.SeatHeatLevel
import com.ivi.climate.domain.repository.ClimateRepository
import com.ivi.climate.domain.usecase.ClimateZone
import com.ivi.climate.domain.usecase.GetClimateStateUseCase
import com.ivi.climate.domain.usecase.SetTemperatureUseCase
import com.ivi.core.base.BaseViewModel
import com.ivi.core.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ClimateUiState(
    val climate: ClimateState = ClimateState(),
    val isLoading: Boolean = false,
    val error: String? = null
) : UiState

sealed interface ClimateAction {
    data class SetDriverTemp(val temp: Float) : ClimateAction
    data class SetPassengerTemp(val temp: Float) : ClimateAction
    data class SetFanSpeed(val speed: Int) : ClimateAction
    data object ToggleAC : ClimateAction
    data object ToggleAuto : ClimateAction
    data object ToggleRecirc : ClimateAction
    data object ToggleSync : ClimateAction
    data object CycleDriverSeatHeat : ClimateAction
    data object CyclePassengerSeatHeat : ClimateAction
}

sealed interface ClimateEvent {
    data class ShowMessage(val text: String) : ClimateEvent
}

@HiltViewModel
class ClimateViewModel @Inject constructor(
    private val getClimateStateUseCase: GetClimateStateUseCase,
    private val setTemperatureUseCase: SetTemperatureUseCase,
    private val climateRepository: ClimateRepository
) : BaseViewModel<ClimateUiState, ClimateEvent, ClimateAction>(ClimateUiState()) {

    init {
        observeClimate()
    }

    private fun observeClimate() {
        viewModelScope.launch {
            getClimateStateUseCase()
                .catch { e ->
                    updateState { copy(error = e.message) }
                }
                .collect { state ->
                    updateState { copy(climate = state, error = null) }
                }
        }
    }

    override fun onAction(action: ClimateAction) {
        viewModelScope.launch {
            when (action) {
                is ClimateAction.SetDriverTemp -> {
                    setTemperatureUseCase(ClimateZone.DRIVER, action.temp)
                }
                is ClimateAction.SetPassengerTemp -> {
                    setTemperatureUseCase(ClimateZone.PASSENGER, action.temp)
                }
                is ClimateAction.SetFanSpeed -> {
                    climateRepository.setFanSpeed(action.speed)
                }
                is ClimateAction.ToggleAC -> {
                    climateRepository.toggleAC()
                }
                is ClimateAction.ToggleAuto -> {
                    climateRepository.toggleAutoMode()
                }
                is ClimateAction.ToggleRecirc -> {
                    // Toggle recirculation
                }
                is ClimateAction.ToggleSync -> {
                    climateRepository.toggleSync()
                }
                is ClimateAction.CycleDriverSeatHeat -> {
                    climateRepository.cycleDriverSeatHeat()
                }
                is ClimateAction.CyclePassengerSeatHeat -> {
                    climateRepository.cyclePassengerSeatHeat()
                }
            }
        }
    }
}
