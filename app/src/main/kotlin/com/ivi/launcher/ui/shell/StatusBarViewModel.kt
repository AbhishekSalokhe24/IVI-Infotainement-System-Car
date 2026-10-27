package com.ivi.launcher.ui.shell

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

enum class BluetoothState {
    CONNECTED, DISCONNECTED
}

data class StatusBarState(
    val time: String = "10:30 AM",
    val bluetoothState: BluetoothState = BluetoothState.CONNECTED,
    val signalStrength: Int = 4, // 0 - 4
    val batteryLevel: Int = 92,  // 0 - 100
    val isNightMode: Boolean = true,
)

@HiltViewModel
class StatusBarViewModel @Inject constructor() : ViewModel() {

    private val _statusState = MutableStateFlow(StatusBarState())
    val statusState: StateFlow<StatusBarState> = _statusState.asStateFlow()

    init {
        observeTime()
    }

    private fun observeTime() {
        viewModelScope.launch {
            while (true) {
                val formatted = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
                _statusState.update { it.copy(time = formatted) }
                delay(30_000L)
            }
        }
    }
}
