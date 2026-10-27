package com.ivi.media.domain.repository

import com.ivi.media.domain.model.MediaSource
import com.ivi.media.domain.model.MediaTrack
import com.ivi.media.domain.model.RadioStation
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun observeCurrentTrack(): Flow<MediaTrack?>
    fun observeSources(): Flow<List<MediaSource>>
    fun observeRadioStations(): Flow<List<RadioStation>>
    suspend fun togglePlayPause()
    suspend fun skipNext()
    suspend fun skipPrevious()
    suspend fun seekTo(positionMs: Long)
    suspend fun tuneToStation(station: RadioStation)
}
