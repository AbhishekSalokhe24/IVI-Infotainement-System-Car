package com.ivi.launcher.ui.voice

import com.ivi.carservice.ClimateClient
import com.ivi.carservice.MediaClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoiceAssistantManager @Inject constructor(
    private val climateClient: ClimateClient,
    private val mediaClient: MediaClient,
) {
    private val _voiceState = MutableStateFlow<VoiceState>(VoiceState.Idle)
    val voiceState: StateFlow<VoiceState> = _voiceState.asStateFlow()

    fun startListening() {
        _voiceState.value = VoiceState.Listening
    }

    suspend fun processSpeech(spokenText: String) {
        _voiceState.value = VoiceState.Processing(spokenText)
        delay(600L) // Simulate quick automotive on-device NLU parsing

        val lower = spokenText.lowercase()
        when {
            lower.contains("play") -> {
                mediaClient.togglePlayPause()
                _voiceState.value = VoiceState.Responding(
                    speechText = "Resuming media playback.",
                    executedAction = "Media: Play"
                )
            }
            lower.contains("pause") -> {
                mediaClient.togglePlayPause()
                _voiceState.value = VoiceState.Responding(
                    speechText = "Media playback paused.",
                    executedAction = "Media: Pause"
                )
            }
            lower.contains("next") -> {
                mediaClient.skipNext()
                _voiceState.value = VoiceState.Responding(
                    speechText = "Skipping to next track.",
                    executedAction = "Media: Next Track"
                )
            }
            lower.contains("temp") || lower.contains("degree") || lower.contains("climate") -> {
                climateClient.setDriverTemperature(21.5f)
                _voiceState.value = VoiceState.Responding(
                    speechText = "Cabin temperature set to 21.5 degrees.",
                    executedAction = "Climate: 21.5°C"
                )
            }
            lower.contains("ac") || lower.contains("air condition") -> {
                climateClient.toggleAC()
                _voiceState.value = VoiceState.Responding(
                    speechText = "Air conditioning toggled.",
                    executedAction = "Climate: AC Toggled"
                )
            }
            lower.contains("navigate") || lower.contains("maps") -> {
                _voiceState.value = VoiceState.Responding(
                    speechText = "Routing to Grand Avenue Center. Calculating fastest route.",
                    executedAction = "Nav: Grand Avenue"
                )
            }
            else -> {
                _voiceState.value = VoiceState.Responding(
                    speechText = "Recognized: \"$spokenText\". All vehicle systems normal.",
                    executedAction = "Assistant Response"
                )
            }
        }

        delay(3_000L)
        _voiceState.value = VoiceState.Idle
    }

    fun cancelVoice() {
        _voiceState.value = VoiceState.Idle
    }
}
