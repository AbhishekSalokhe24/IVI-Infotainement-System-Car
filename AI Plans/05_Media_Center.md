# 🎵 Phase 3: Media Center

> **Parent Plan:** [00_Master_Implementation_Plan.md](file:///c:/Users/abhis/AOSP/Projects/IVI%20Project/AI%20Plans/00_Master_Implementation_Plan.md)  
> **Module:** `features/media`  
> **Duration:** Weeks 9–12  
> **Goal:** Build a complete media center with browsing, playback, radio, and multi-zone audio

---

## 1. Media Architecture

### 1.1 Android Automotive Media Model

```
┌────────────────────────────────────────────────────────┐
│                    IVI Media Center                     │
│              (MediaBrowserCompat Client)                │
├────────────────────────────────────────────────────────┤
│                MediaSessionCompat                       │
│           (Transport Controls, Metadata)                │
├────────────────────────────────────────────────────────┤
│            MediaBrowserServiceCompat                    │
│    ┌──────────┐ ┌──────────┐ ┌──────────┐             │
│    │  Spotify  │ │  Local   │ │  Radio   │  ...        │
│    │  Service  │ │  Media   │ │  Service │             │
│    └──────────┘ └──────────┘ └──────────┘             │
├────────────────────────────────────────────────────────┤
│              CarAudioManager                            │
│        (Audio Zones, Focus, Volume)                     │
└────────────────────────────────────────────────────────┘
```

### 1.2 Media Screens

| Screen | Description |
|--------|-------------|
| **Source Selector** | Grid of available media sources (Spotify, USB, BT, Radio) |
| **Media Browser** | Browse media source content (albums, playlists, folders) |
| **Now Playing** | Full-screen playback with album art, controls, progress |
| **Queue** | Current playback queue with reorder/remove |
| **Radio** | AM/FM/DAB tuner with presets and seek |
| **Mini Player** | Collapsed player bar visible on other screens |

---

## 2. Domain Models

```kotlin
// features/media/src/main/kotlin/com/ivi/media/domain/model/MediaModels.kt
package com.ivi.media.domain.model

data class MediaSource(
    val id: String,
    val name: String,
    val packageName: String,
    val icon: Drawable?,
    val isConnected: Boolean = true,
    val type: MediaSourceType,
)

enum class MediaSourceType {
    APP,           // Streaming apps (Spotify, YouTube Music)
    BLUETOOTH,     // Bluetooth A2DP audio
    USB,           // USB media device
    RADIO_AM,      // AM Radio
    RADIO_FM,      // FM Radio
    RADIO_DAB,     // DAB/DAB+ Radio
    AUX,           // Auxiliary input
}

data class MediaItem(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val artworkUri: Uri? = null,
    val isPlayable: Boolean = false,
    val isBrowsable: Boolean = false,
    val mediaUri: Uri? = null,
    val duration: Long = 0L,     // milliseconds
    val extras: Bundle? = null,
)

data class PlaybackState(
    val state: PlayState = PlayState.NONE,
    val position: Long = 0L,      // Current position in ms
    val duration: Long = 0L,      // Total duration in ms
    val speed: Float = 1.0f,
    val shuffleMode: ShuffleMode = ShuffleMode.OFF,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val activeQueueItemId: Long = -1,
)

enum class PlayState {
    NONE, PLAYING, PAUSED, BUFFERING, STOPPED, ERROR
}

enum class ShuffleMode { OFF, ALL }
enum class RepeatMode { OFF, ONE, ALL }

data class NowPlayingInfo(
    val title: String,
    val artist: String,
    val album: String,
    val artworkUri: Uri?,
    val playbackState: PlaybackState,
    val source: MediaSource?,
)
```

---

## 3. Media Browser Implementation

### 3.1 Data Source — MediaBrowser Client

```kotlin
// features/media/src/main/kotlin/com/ivi/media/data/source/CarMediaDataSource.kt
package com.ivi.media.data.source

import android.content.ComponentName
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CarMediaDataSource @Inject constructor(
    private val context: Context,
) {
    private var mediaBrowser: MediaBrowserCompat? = null
    private var mediaController: MediaControllerCompat? = null

    private val _connectionState = MutableStateFlow(false)
    val connectionState: StateFlow<Boolean> = _connectionState

    private val _nowPlaying = MutableStateFlow<NowPlayingInfo?>(null)
    val nowPlaying: StateFlow<NowPlayingInfo?> = _nowPlaying

    private val _playbackState = MutableStateFlow(PlaybackState())
    val playbackState: StateFlow<PlaybackState> = _playbackState

    /**
     * Connect to a media source's MediaBrowserService.
     */
    fun connectToSource(componentName: ComponentName) {
        disconnect()

        mediaBrowser = MediaBrowserCompat(
            context,
            componentName,
            object : MediaBrowserCompat.ConnectionCallback() {
                override fun onConnected() {
                    _connectionState.value = true
                    val token = mediaBrowser?.sessionToken ?: return
                    mediaController = MediaControllerCompat(context, token)
                    registerMediaControllerCallbacks()
                }

                override fun onConnectionFailed() {
                    _connectionState.value = false
                }

                override fun onConnectionSuspended() {
                    _connectionState.value = false
                }
            },
            null
        )
        mediaBrowser?.connect()
    }

    /**
     * Browse children of a media item (root, album, playlist, etc.)
     */
    fun browse(parentId: String): Flow<List<MediaItem>> = callbackFlow {
        val browser = mediaBrowser ?: return@callbackFlow

        val callback = object : MediaBrowserCompat.SubscriptionCallback() {
            override fun onChildrenLoaded(
                parentId: String,
                children: MutableList<MediaBrowserCompat.MediaItem>
            ) {
                val items = children.map { it.toMediaItem() }
                trySend(items)
            }

            override fun onError(parentId: String) {
                trySend(emptyList())
            }
        }

        browser.subscribe(parentId, callback)

        awaitClose {
            browser.unsubscribe(parentId)
        }
    }

    /**
     * Transport controls
     */
    fun play() = mediaController?.transportControls?.play()
    fun pause() = mediaController?.transportControls?.pause()
    fun skipNext() = mediaController?.transportControls?.skipToNext()
    fun skipPrevious() = mediaController?.transportControls?.skipToPrevious()
    fun seekTo(positionMs: Long) = mediaController?.transportControls?.seekTo(positionMs)
    fun setShuffleMode(mode: Int) = mediaController?.transportControls?.setShuffleMode(mode)
    fun setRepeatMode(mode: Int) = mediaController?.transportControls?.setRepeatMode(mode)

    fun playFromMediaId(mediaId: String) {
        mediaController?.transportControls?.playFromMediaId(mediaId, null)
    }

    private fun registerMediaControllerCallbacks() {
        mediaController?.registerCallback(object : MediaControllerCompat.Callback() {
            override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
                metadata?.let {
                    _nowPlaying.value = NowPlayingInfo(
                        title = it.getString(MediaMetadataCompat.METADATA_KEY_TITLE) ?: "Unknown",
                        artist = it.getString(MediaMetadataCompat.METADATA_KEY_ARTIST) ?: "Unknown",
                        album = it.getString(MediaMetadataCompat.METADATA_KEY_ALBUM) ?: "",
                        artworkUri = it.getString(MediaMetadataCompat.METADATA_KEY_ART_URI)
                            ?.let(Uri::parse),
                        playbackState = _playbackState.value,
                        source = null,
                    )
                }
            }

            override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
                state?.let {
                    _playbackState.value = PlaybackState(
                        state = mapPlayState(it.state),
                        position = it.position,
                        speed = it.playbackSpeed,
                    )
                }
            }
        })
    }

    fun disconnect() {
        mediaBrowser?.disconnect()
        mediaBrowser = null
        mediaController = null
    }

    private fun mapPlayState(state: Int): PlayState = when (state) {
        PlaybackStateCompat.STATE_PLAYING -> PlayState.PLAYING
        PlaybackStateCompat.STATE_PAUSED -> PlayState.PAUSED
        PlaybackStateCompat.STATE_BUFFERING -> PlayState.BUFFERING
        PlaybackStateCompat.STATE_STOPPED -> PlayState.STOPPED
        PlaybackStateCompat.STATE_ERROR -> PlayState.ERROR
        else -> PlayState.NONE
    }
}
```

---

## 4. Now Playing Screen

### 4.1 Now Playing UI

```kotlin
// features/media/src/main/kotlin/com/ivi/media/ui/player/NowPlayingScreen.kt
package com.ivi.media.ui.player

@Composable
fun NowPlayingScreen(
    viewModel: NowPlayingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        IVIColors.BackgroundGradientStart,
                        IVIColors.BackgroundGradientEnd,
                    )
                )
            )
            .padding(IVISpacing.XL),
        horizontalArrangement = Arrangement.spacedBy(IVISpacing.XL),
    ) {
        // Left: Album Art
        AlbumArtwork(
            artworkUri = uiState.artworkUri,
            modifier = Modifier
                .size(IVIDimensions.AlbumArtSize)
                .clip(RoundedCornerShape(IVIDimensions.AlbumArtCornerRadius)),
        )

        // Right: Track Info + Controls
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
        ) {
            // Track Title
            Text(
                text = uiState.title,
                style = IVITypography.HeadlineLarge,
                color = IVIColors.OnSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(Modifier.height(IVISpacing.XS))

            // Artist
            Text(
                text = uiState.artist,
                style = IVITypography.TitleMedium,
                color = IVIColors.OnSurfaceVariant,
            )

            // Album
            Text(
                text = uiState.album,
                style = IVITypography.BodyMedium,
                color = IVIColors.OnSurfaceDim,
            )

            Spacer(Modifier.height(IVISpacing.XL))

            // Progress Bar
            ProgressSection(
                position = uiState.positionMs,
                duration = uiState.durationMs,
                onSeek = { viewModel.onAction(PlayerAction.SeekTo(it)) },
            )

            Spacer(Modifier.height(IVISpacing.LG))

            // Transport Controls
            TransportControls(
                isPlaying = uiState.isPlaying,
                shuffleMode = uiState.shuffleMode,
                repeatMode = uiState.repeatMode,
                onPlayPause = { viewModel.onAction(PlayerAction.TogglePlayPause) },
                onNext = { viewModel.onAction(PlayerAction.Next) },
                onPrevious = { viewModel.onAction(PlayerAction.Previous) },
                onShuffle = { viewModel.onAction(PlayerAction.ToggleShuffle) },
                onRepeat = { viewModel.onAction(PlayerAction.CycleRepeat) },
            )
        }
    }
}

@Composable
private fun TransportControls(
    isPlaying: Boolean,
    shuffleMode: ShuffleMode,
    repeatMode: RepeatMode,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onShuffle: () -> Unit,
    onRepeat: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Shuffle
        IVIIconButton(
            icon = Icons.Default.Shuffle,
            contentDescription = "Shuffle",
            onClick = onShuffle,
            selected = shuffleMode == ShuffleMode.ALL,
        )

        Spacer(Modifier.width(IVISpacing.LG))

        // Previous
        IVIIconButton(
            icon = Icons.Default.SkipPrevious,
            contentDescription = "Previous",
            onClick = onPrevious,
        )

        Spacer(Modifier.width(IVISpacing.MD))

        // Play/Pause (large button)
        FloatingActionButton(
            onClick = onPlayPause,
            modifier = Modifier.size(IVIDimensions.TouchTargetLarge),
            containerColor = IVIColors.Primary,
            contentColor = IVIColors.OnPrimary,
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "Pause" else "Play",
                modifier = Modifier.size(IVIDimensions.IconLarge),
            )
        }

        Spacer(Modifier.width(IVISpacing.MD))

        // Next
        IVIIconButton(
            icon = Icons.Default.SkipNext,
            contentDescription = "Next",
            onClick = onNext,
        )

        Spacer(Modifier.width(IVISpacing.LG))

        // Repeat
        IVIIconButton(
            icon = when (repeatMode) {
                RepeatMode.ONE -> Icons.Default.RepeatOne
                else -> Icons.Default.Repeat
            },
            contentDescription = "Repeat",
            onClick = onRepeat,
            selected = repeatMode != RepeatMode.OFF,
        )
    }
}

@Composable
private fun ProgressSection(
    position: Long,
    duration: Long,
    onSeek: (Long) -> Unit,
) {
    Column {
        Slider(
            value = if (duration > 0) position.toFloat() / duration else 0f,
            onValueChange = { fraction ->
                onSeek((fraction * duration).toLong())
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(IVIDimensions.TouchTargetMin),
            colors = SliderDefaults.colors(
                thumbColor = IVIColors.Primary,
                activeTrackColor = IVIColors.Primary,
                inactiveTrackColor = IVIColors.SurfaceBright,
            )
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = formatDuration(position),
                style = IVITypography.LabelMedium,
                color = IVIColors.OnSurfaceVariant,
            )
            Text(
                text = formatDuration(duration),
                style = IVITypography.LabelMedium,
                color = IVIColors.OnSurfaceVariant,
            )
        }
    }
}

private fun formatDuration(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}
```

---

## 5. Media Source Selector

```kotlin
// features/media/src/main/kotlin/com/ivi/media/ui/browser/MediaSourceSelector.kt
package com.ivi.media.ui.browser

@Composable
fun MediaSourceSelector(
    sources: List<MediaSource>,
    activeSource: MediaSource?,
    onSourceSelected: (MediaSource) -> Unit,
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = IVISpacing.MD),
        horizontalArrangement = Arrangement.spacedBy(IVISpacing.MD),
        contentPadding = PaddingValues(horizontal = IVISpacing.LG),
    ) {
        items(sources) { source ->
            val isActive = source.id == activeSource?.id

            IVICard(
                onClick = { onSourceSelected(source) },
                modifier = Modifier.width(160.dp),
            ) {
                Column(
                    modifier = Modifier.padding(IVISpacing.MD),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Source icon
                    AsyncImage(
                        model = source.icon,
                        contentDescription = source.name,
                        modifier = Modifier.size(IVIDimensions.IconLarge),
                    )
                    Spacer(Modifier.height(IVISpacing.XS))
                    Text(
                        text = source.name,
                        style = IVITypography.LabelLarge,
                        color = if (isActive) IVIColors.Primary else IVIColors.OnSurface,
                    )
                }
            }
        }
    }
}
```

---

## 6. Audio Zone Management

```kotlin
// libraries/carservice/src/main/kotlin/com/ivi/carservice/AudioClient.kt
package com.ivi.carservice

import android.car.media.CarAudioManager
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages audio zones for multi-zone playback.
 * Example: Driver listens to music, passenger listens to podcast,
 *          rear seat watches video.
 */
@Singleton
class AudioClient @Inject constructor(
    private val carServiceManager: CarServiceManager,
) {
    private val audioManager: CarAudioManager?
        get() = carServiceManager.audioManager

    /**
     * Get available audio zones.
     */
    fun getAudioZones(): List<Int> {
        return audioManager?.audioZoneIds?.toList() ?: emptyList()
    }

    /**
     * Set volume for a specific audio zone.
     */
    fun setVolumeForZone(zoneId: Int, groupId: Int, volume: Int) {
        audioManager?.setGroupVolume(zoneId, groupId, volume, 0)
    }

    /**
     * Get current volume for a zone.
     */
    fun getVolumeForZone(zoneId: Int, groupId: Int): Int {
        return audioManager?.getGroupVolume(zoneId, groupId) ?: 0
    }

    /**
     * Get max volume for a zone.
     */
    fun getMaxVolumeForZone(zoneId: Int, groupId: Int): Int {
        return audioManager?.getGroupMaxVolume(zoneId, groupId) ?: 15
    }
}
```

---

## 7. Radio Tuner

### 7.1 Radio Screen

```kotlin
// features/media/src/main/kotlin/com/ivi/media/ui/radio/RadioScreen.kt
package com.ivi.media.ui.radio

@Composable
fun RadioScreen(
    viewModel: RadioViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(IVISpacing.LG),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Band selector (AM / FM / DAB)
        RadioBandSelector(
            selectedBand = uiState.band,
            onBandSelected = { viewModel.onAction(RadioAction.SelectBand(it)) },
        )

        Spacer(Modifier.height(IVISpacing.XL))

        // Frequency display
        Text(
            text = uiState.frequencyDisplay,
            style = IVITypography.DisplayLarge,
            color = IVIColors.Primary,
        )

        Text(
            text = uiState.stationName ?: "",
            style = IVITypography.TitleMedium,
            color = IVIColors.OnSurfaceVariant,
        )

        Spacer(Modifier.height(IVISpacing.XL))

        // Frequency slider / seek bar
        Slider(
            value = uiState.frequency,
            onValueChange = { viewModel.onAction(RadioAction.TuneToFrequency(it)) },
            valueRange = uiState.frequencyRange,
            modifier = Modifier
                .fillMaxWidth()
                .height(IVIDimensions.TouchTargetMin),
        )

        // Seek buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            IVIButton(
                text = "◀◀ Seek",
                onClick = { viewModel.onAction(RadioAction.SeekDown) },
            )
            Spacer(Modifier.width(IVISpacing.LG))
            IVIButton(
                text = "Seek ▶▶",
                onClick = { viewModel.onAction(RadioAction.SeekUp) },
            )
        }

        Spacer(Modifier.height(IVISpacing.XL))

        // Presets grid
        Text(
            text = "Presets",
            style = IVITypography.HeadlineSmall,
            color = IVIColors.OnSurface,
        )
        Spacer(Modifier.height(IVISpacing.MD))

        LazyVerticalGrid(
            columns = GridCells.Fixed(6),
            verticalArrangement = Arrangement.spacedBy(IVISpacing.SM),
            horizontalArrangement = Arrangement.spacedBy(IVISpacing.SM),
        ) {
            items(uiState.presets) { preset ->
                PresetButton(
                    preset = preset,
                    isActive = preset.frequency == uiState.frequency,
                    onClick = { viewModel.onAction(RadioAction.SelectPreset(preset)) },
                    onLongClick = { viewModel.onAction(RadioAction.SavePreset(preset.slot)) },
                )
            }
        }
    }
}
```

---

## 8. Verification Checklist

- [ ] Media source selector shows all available MediaBrowserServices
- [ ] Can browse media source content (albums, playlists, folders)
- [ ] Now Playing screen shows album art, track info, progress
- [ ] Transport controls (play/pause/next/prev/seek) work
- [ ] Shuffle and repeat modes toggle correctly
- [ ] Progress bar updates in real-time during playback
- [ ] Mini player appears on non-media screens
- [ ] Radio tuner seeks up/down and tunes to frequency
- [ ] Radio presets save and recall correctly
- [ ] Audio zone volume control works for multi-zone
- [ ] Bluetooth audio source connects and plays
- [ ] USB media source detects and browses files
- [ ] Media notification integrates with system notifications
- [ ] Audio focus handled correctly (pauses for navigation voice)

---

*Next: [06_Navigation.md](file:///c:/Users/abhis/AOSP/Projects/IVI%20Project/AI%20Plans/06_Navigation.md) — Map rendering, routing, turn-by-turn, and cluster display integration*
