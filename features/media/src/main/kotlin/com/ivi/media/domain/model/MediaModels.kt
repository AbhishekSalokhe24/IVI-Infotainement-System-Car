package com.ivi.media.domain.model

data class MediaSource(
    val id: String,
    val name: String,
    val type: MediaSourceType,
    val isConnected: Boolean = true,
)

enum class MediaSourceType {
    SPOTIFY,
    YOUTUBE_MUSIC,
    BLUETOOTH,
    USB,
    RADIO_FM,
    RADIO_AM,
    RADIO_DAB
}

data class MediaTrack(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val durationMs: Long,
    val currentPositionMs: Long = 0L,
    val artworkUri: String? = null,
    val source: String = "Spotify"
)

data class RadioStation(
    val frequencyMhz: Float,
    val name: String,
    val genre: String,
    val isFavorite: Boolean = false
)

enum class MediaTab {
    NOW_PLAYING, BROWSE, RADIO
}
