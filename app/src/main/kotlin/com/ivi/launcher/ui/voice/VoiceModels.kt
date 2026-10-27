package com.ivi.launcher.ui.voice

sealed interface VoiceState {
    data object Idle : VoiceState
    data object Listening : VoiceState
    data class Processing(val transcript: String) : VoiceState
    data class Responding(val speechText: String, val executedAction: String) : VoiceState
    data class Error(val message: String) : VoiceState
}
