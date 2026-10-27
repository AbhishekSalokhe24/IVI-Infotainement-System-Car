package com.ivi.carservice

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

data class MediaInfo(
    val title: String = "Midnight City",
    val artist: String = "M83",
    val album: String = "Hurry Up, We're Dreaming",
    val durationMs: Long = 244_000L,
    val currentPositionMs: Long = 72_000L,
    val isPlaying: Boolean = true,
    val albumArtUri: String? = null,
    val source: String = "Spotify",
)

@Singleton
class MediaClient @Inject constructor() {

    private val _nowPlaying = MutableStateFlow<MediaInfo?>(MediaInfo())
    val nowPlaying: Flow<MediaInfo?> = _nowPlaying.asStateFlow()

    fun togglePlayPause() {
        _nowPlaying.update { current ->
            current?.copy(isPlaying = !current.isPlaying)
        }
    }

    fun skipNext() {
        _nowPlaying.update { current ->
            current?.copy(
                title = "Starboy",
                artist = "The Weeknd",
                album = "Starboy",
                currentPositionMs = 0L,
                durationMs = 230_000L,
                isPlaying = true
            )
        }
    }

    fun skipPrevious() {
        _nowPlaying.update { current ->
            current?.copy(
                title = "Blinding Lights",
                artist = "The Weeknd",
                album = "After Hours",
                currentPositionMs = 0L,
                durationMs = 200_000L,
                isPlaying = true
            )
        }
    }

    fun seekTo(positionMs: Long) {
        _nowPlaying.update { current ->
            current?.copy(currentPositionMs = positionMs)
        }
    }
}
