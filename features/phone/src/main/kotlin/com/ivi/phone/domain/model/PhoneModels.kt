package com.ivi.phone.domain.model

data class Contact(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val isFavorite: Boolean = false,
    val photoUri: String? = null
)

data class CallLogEntry(
    val id: String,
    val contactName: String?,
    val phoneNumber: String,
    val timestampFormatted: String,
    val type: CallType
)

enum class CallType {
    INCOMING, OUTGOING, MISSED
}

enum class PhoneTab {
    FAVORITES, RECENT, CONTACTS, KEYPAD
}

data class ActiveCall(
    val callerName: String,
    val phoneNumber: String,
    val durationSeconds: Int = 0,
    val isMuted: Boolean = false,
    val isSpeakerOn: Boolean = true
)
