package com.ivi.phone.ui.dialer

import androidx.lifecycle.viewModelScope
import com.ivi.core.base.BaseViewModel
import com.ivi.core.base.UiState
import com.ivi.phone.domain.model.ActiveCall
import com.ivi.phone.domain.model.CallLogEntry
import com.ivi.phone.domain.model.Contact
import com.ivi.phone.domain.model.PhoneTab
import com.ivi.phone.domain.repository.PhoneRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PhoneUiState(
    val selectedTab: PhoneTab = PhoneTab.KEYPAD,
    val dialedNumber: String = "",
    val contacts: List<Contact> = emptyList(),
    val favorites: List<Contact> = emptyList(),
    val callLog: List<CallLogEntry> = emptyList(),
    val activeCall: ActiveCall? = null,
    val contactSearchQuery: String = "",
    val error: String? = null
) : UiState

sealed interface PhoneAction {
    data class SelectTab(val tab: PhoneTab) : PhoneAction
    data class DialDigit(val digit: Char) : PhoneAction
    data object Backspace : PhoneAction
    data object ClearNumber : PhoneAction
    data object CallDialed : PhoneAction
    data class CallContact(val contact: Contact) : PhoneAction
    data class CallNumber(val number: String) : PhoneAction
    data object EndCall : PhoneAction
    data object ToggleMute : PhoneAction
    data class SearchContacts(val query: String) : PhoneAction
}

sealed interface PhoneEvent

@HiltViewModel
class DialerViewModel @Inject constructor(
    private val repository: PhoneRepository
) : BaseViewModel<PhoneUiState, PhoneEvent, PhoneAction>(PhoneUiState()) {

    init {
        observeData()
    }

    private fun observeData() {
        viewModelScope.launch {
            repository.observeContacts()
                .catch { e -> updateState { copy(error = e.message) } }
                .collect { list ->
                    updateState {
                        copy(
                            contacts = list,
                            favorites = list.filter { it.isFavorite }
                        )
                    }
                }
        }

        viewModelScope.launch {
            repository.observeCallLog().collect { log ->
                updateState { copy(callLog = log) }
            }
        }

        viewModelScope.launch {
            repository.observeActiveCall().collect { call ->
                updateState { copy(activeCall = call) }
            }
        }
    }

    override fun onAction(action: PhoneAction) {
        viewModelScope.launch {
            when (action) {
                is PhoneAction.SelectTab -> updateState { copy(selectedTab = action.tab) }
                is PhoneAction.DialDigit -> updateState { copy(dialedNumber = dialedNumber + action.digit) }
                is PhoneAction.Backspace -> updateState {
                    if (dialedNumber.isNotEmpty()) copy(dialedNumber = dialedNumber.dropLast(1))
                    else this
                }
                is PhoneAction.ClearNumber -> updateState { copy(dialedNumber = "") }
                is PhoneAction.CallDialed -> {
                    if (currentState.dialedNumber.isNotBlank()) {
                        repository.dialNumber(currentState.dialedNumber)
                    }
                }
                is PhoneAction.CallContact -> repository.dialNumber(action.contact.phoneNumber)
                is PhoneAction.CallNumber -> repository.dialNumber(action.number)
                is PhoneAction.EndCall -> repository.endCall()
                is PhoneAction.ToggleMute -> repository.toggleMute()
                is PhoneAction.SearchContacts -> updateState { copy(contactSearchQuery = action.query) }
            }
        }
    }
}
