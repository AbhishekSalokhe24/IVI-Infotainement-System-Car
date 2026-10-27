package com.ivi.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Base ViewModel implementing Unidirectional Data Flow (UDF) pattern.
 *
 * @param S UI State type implementing [UiState]
 * @param E One-time Event type (navigation, alerts, snackbars)
 * @param A User Action / Intent type
 */
abstract class BaseViewModel<S : UiState, E, A>(
    initialState: S
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    private val _events = Channel<E>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    protected val currentState: S get() = _uiState.value

    protected fun updateState(reducer: S.() -> S) {
        _uiState.update { it.reducer() }
    }

    protected fun sendEvent(event: E) {
        viewModelScope.launch {
            _events.send(event)
        }
    }

    abstract fun onAction(action: A)
}
