package com.ivi.launcher.ui.home

import com.ivi.carservice.MediaInfo
import com.ivi.core.base.UiState

data class HomeUiState(
    val greeting: String = "Good Morning",
    val dateTime: String = "",
    val nowPlaying: MediaInfo? = MediaInfo(),
    val temperature: Float = 22f,
    val isACOn: Boolean = true,
    val lastDestination: String? = "Grand Avenue, 14.2 km",
    val eta: String? = "18 min",
    val recentContact: String? = "Sarah Connor",
    val missedCallCount: Int = 0,
    val fuelLevel: Float = 0.72f,
    val range: String = "420 km",
    val tirePressureOk: Boolean = true,
) : UiState

sealed interface HomeAction {
    data object TogglePlayback : HomeAction
    data object NextTrack : HomeAction
    data object PreviousTrack : HomeAction
}

sealed interface HomeEvent {
    data class ShowError(val message: String) : HomeEvent
}
