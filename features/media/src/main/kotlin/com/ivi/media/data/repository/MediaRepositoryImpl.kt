package com.ivi.media.data.repository

import com.ivi.carservice.MediaClient
import com.ivi.media.domain.model.MediaSource
import com.ivi.media.domain.model.MediaSourceType
import com.ivi.media.domain.model.MediaTrack
import com.ivi.media.domain.model.RadioStation
import com.ivi.media.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaRepositoryImpl @Inject constructor(
    private val client: MediaClient
) : MediaRepository {

    private val availableSources = MutableStateFlow(
        listOf(
            MediaSource("spotify", "Spotify", MediaSourceType.SPOTIFY),
            MediaSource("ytmusic", "YouTube Music", MediaSourceType.YOUTUBE_MUSIC),
            MediaSource("bluetooth", "Pixel 8 Pro (Bluetooth)", MediaSourceType.BLUETOOTH),
            MediaSource("radio_fm", "FM Radio", MediaSourceType.RADIO_FM),
            MediaSource("radio_dab", "DAB+ Digital Radio", MediaSourceType.RADIO_DAB),
        )
    )

    private val radioStations = MutableStateFlow(
        listOf(
            RadioStation(88.5f, "NPR News & Talk", "News/Talk", true),
            RadioStation(91.1f, "Jazz Masters FM", "Jazz", true),
            RadioStation(97.3f, "K-Rock Classic", "Rock", true),
            RadioStation(102.7f, "KIIS Top 40", "Pop", false),
            RadioStation(104.3f, "Electra Dance Radio", "Electronic", true),
        )
    )

    override fun observeCurrentTrack(): Flow<MediaTrack?> {
        return client.nowPlaying.map { info ->
            info?.let {
                MediaTrack(
                    id = "track_current",
                    title = it.title,
                    artist = it.artist,
                    album = it.album,
                    durationMs = it.durationMs,
                    currentPositionMs = it.currentPositionMs,
                    artworkUri = it.albumArtUri,
                    source = it.source
                )
            }
        }
    }

    override fun observeSources(): Flow<List<MediaSource>> = availableSources.asStateFlow()

    override fun observeRadioStations(): Flow<List<RadioStation>> = radioStations.asStateFlow()

    override suspend fun togglePlayPause() = client.togglePlayPause()

    override suspend fun skipNext() = client.skipNext()

    override suspend fun skipPrevious() = client.skipPrevious()

    override suspend fun seekTo(positionMs: Long) = client.seekTo(positionMs)

    override suspend fun tuneToStation(station: RadioStation) {
        // Tuner frequency adjustment
    }
}
