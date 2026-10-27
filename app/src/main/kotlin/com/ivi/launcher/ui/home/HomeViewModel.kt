package com.ivi.launcher.ui.home

import androidx.lifecycle.viewModelScope
import com.ivi.carservice.ClimateClient
import com.ivi.carservice.MediaClient
import com.ivi.carservice.VehiclePropertyClient
import com.ivi.carservice.VehiclePropertyIds
import com.ivi.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val vehiclePropertyClient: VehiclePropertyClient,
    private val mediaClient: MediaClient,
    private val climateClient: ClimateClient,
) : BaseViewModel<HomeUiState, HomeEvent, HomeAction>(HomeUiState()) {

    init {
        observeVehicleData()
        observeMediaState()
        observeClimateState()
        updateGreeting()
    }

    override fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.TogglePlayback -> mediaClient.togglePlayPause()
            is HomeAction.NextTrack -> mediaClient.skipNext()
            is HomeAction.PreviousTrack -> mediaClient.skipPrevious()
        }
    }

    private fun observeVehicleData() {
        viewModelScope.launch {
            vehiclePropertyClient.observeProperties(
                listOf(
                    VehiclePropertyIds.FUEL_LEVEL,
                    VehiclePropertyIds.RANGE_REMAINING,
                    VehiclePropertyIds.TIRE_PRESSURE,
                )
            ).catch { e ->
                sendEvent(HomeEvent.ShowError(e.message ?: "Vehicle data error"))
            }.collect { properties ->
                updateState {
                    copy(
                        fuelLevel = properties.fuelLevel,
                        range = properties.range,
                        tirePressureOk = properties.tirePressureOk,
                    )
                }
            }
        }
    }

    private fun observeMediaState() {
        viewModelScope.launch {
            mediaClient.nowPlaying.catch { e ->
                sendEvent(HomeEvent.ShowError(e.message ?: "Media state error"))
            }.collect { mediaInfo ->
                updateState { copy(nowPlaying = mediaInfo) }
            }
        }
    }

    private fun observeClimateState() {
        viewModelScope.launch {
            climateClient.climateState.catch { e ->
                sendEvent(HomeEvent.ShowError(e.message ?: "Climate state error"))
            }.collect { climate ->
                updateState {
                    copy(
                        temperature = climate.driverTemperature,
                        isACOn = climate.isACOn,
                    )
                }
            }
        }
    }

    private fun updateGreeting() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val greeting = when {
            hour < 12 -> "Good Morning, Driver"
            hour < 17 -> "Good Afternoon, Driver"
            else -> "Good Evening, Driver"
        }
        val dateString = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(Date())

        updateState {
            copy(
                greeting = greeting,
                dateTime = dateString
            )
        }
    }
}
