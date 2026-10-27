package com.ivi.phone.data.repository

import com.ivi.phone.domain.model.ActiveCall
import com.ivi.phone.domain.model.CallLogEntry
import com.ivi.phone.domain.model.CallType
import com.ivi.phone.domain.model.Contact
import com.ivi.phone.domain.repository.PhoneRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhoneRepositoryImpl @Inject constructor() : PhoneRepository {

    private val contacts = MutableStateFlow(
        listOf(
            Contact("1", "Sarah Connor", "+1 (555) 019-2834", isFavorite = true),
            Contact("2", "John Anderson", "+1 (555) 438-9201", isFavorite = true),
            Contact("3", "Emergency Roadside Assist", "+1 (800) 555-0199", isFavorite = true),
            Contact("4", "Alexander Vance", "+1 (555) 872-3190", isFavorite = false),
            Contact("5", "Dr. Emily Watson", "+1 (555) 651-8932", isFavorite = false),
        )
    )

    private val callLog = MutableStateFlow(
        listOf(
            CallLogEntry("c1", "Sarah Connor", "+1 (555) 019-2834", "10:14 AM", CallType.INCOMING),
            CallLogEntry("c2", "Alexander Vance", "+1 (555) 872-3190", "Yesterday", CallType.MISSED),
            CallLogEntry("c3", "John Anderson", "+1 (555) 438-9201", "Oct 24", CallType.OUTGOING),
        )
    )

    private val activeCall = MutableStateFlow<ActiveCall?>(null)

    override fun observeContacts(): Flow<List<Contact>> = contacts.asStateFlow()

    override fun observeCallLog(): Flow<List<CallLogEntry>> = callLog.asStateFlow()

    override fun observeActiveCall(): Flow<ActiveCall?> = activeCall.asStateFlow()

    override suspend fun dialNumber(number: String) {
        val contactName = contacts.value.find { it.phoneNumber == number }?.name ?: number
        activeCall.value = ActiveCall(contactName, number)
    }

    override suspend fun endCall() {
        activeCall.value = null
    }

    override suspend fun toggleMute() {
        activeCall.value?.let {
            activeCall.value = it.copy(isMuted = !it.isMuted)
        }
    }
}
