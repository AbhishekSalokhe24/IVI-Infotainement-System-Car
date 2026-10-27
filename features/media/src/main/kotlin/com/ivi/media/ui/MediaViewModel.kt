package com.ivi.media.ui

import androidx.lifecycle.viewModelScope
import com.ivi.core.base.BaseViewModel
import com.ivi.core.base.UiState
import com.ivi.media.domain.model.MediaSource
import com.ivi.media.domain.model.MediaTab
import com.ivi.media.domain.model.MediaTrack
import com.ivi.media.domain.model.RadioStation
import com.ivi.media.domain.repository.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MediaUiState(
    val currentTrack: MediaTrack = MediaTrack(
        id = "default",
        title = "Midnight City",
        artist = "M83",
        album = "Hurry Up, We're Dreaming",
        durationMs = 244_000L,
        currentPositionMs = 72_000L,
    ),
    val isPlaying: Boolean = true,
    val progress: Float = 0.3f,
    val selectedTab: MediaTab = MediaTab.NOW_PLAYING,
    val sources: List<MediaSource> = emptyList(),
    val radioStations: List<RadioStation> = emptyList(),
    val error: String? = null
) : UiState

sealed interface MediaAction {
    data object TogglePlayPause : MediaAction
    data object SkipNext : MediaAction
    data object SkipPrevious : MediaAction
    data class SeekTo(val fraction: Float) : MediaAction
    data class SelectTab(val tab: MediaTab) : MediaAction
    data class TuneToStation(val station: RadioStation) : MediaAction
}

sealed interface MediaEvent

@HiltViewModel
class MediaViewModel @Inject constructor(
    private val repository: MediaRepository
) : BaseViewModel<MediaUiState, MediaEvent, MediaAction>(MediaUiState()) {

    init {
        observeData()
    }

    private fun observeData() {
        viewModelScope.launch {
            repository.observeCurrentTrack()
                .catch { e -> updateState { copy(error = e.message) } }
                .collect { track ->
                    if (track != null) {
                        val progress = if (track.durationMs > 0) {
                            track.currentPositionMs.toFloat() / track.durationMs
                        } else 0f
                        updateState { copy(currentTrack = track, progress = progress) }
                    }
                }
        }

        viewModelScope.launch {
            repository.observeSources().collect { sources ->
                updateState { copy(sources = sources) }
            }
        }

        viewModelScope.launch {
            repository.observeRadioStations().collect { stations ->
                updateState { copy(radioStations = stations) }
            }
        }
    }

    override fun onAction(action: MediaAction) {
        viewModelScope.launch {
            when (action) {
                is MediaAction.TogglePlayPause -> {
                    updateState { copy(isPlaying = !isPlaying) }
                    repository.togglePlayPause()
                }
                is MediaAction.SkipNext -> repository.skipNext()
                is MediaAction.SkipPrevious -> repository.skipPrevious()
                is MediaAction.SeekTo -> {
                    val duration = currentState.currentTrack.durationMs
                    repository.seekTo((action.fraction * duration).toLong())
                }
                is MediaAction.SelectTab -> updateState { copy(selectedTab = action.tab) }
                is MediaAction.TuneToStation -> repository.tuneToStation(action.station)
            }
        }
    }
}
