package com.ivi.phone.domain.repository

import com.ivi.phone.domain.model.ActiveCall
import com.ivi.phone.domain.model.CallLogEntry
import com.ivi.phone.domain.model.Contact
import kotlinx.coroutines.flow.Flow

interface PhoneRepository {
    fun observeContacts(): Flow<List<Contact>>
    fun observeCallLog(): Flow<List<CallLogEntry>>
    fun observeActiveCall(): Flow<ActiveCall?>
    suspend fun dialNumber(number: String)
    suspend fun endCall()
    suspend fun toggleMute()
}
