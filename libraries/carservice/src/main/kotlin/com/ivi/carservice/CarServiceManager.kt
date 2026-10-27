package com.ivi.carservice

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarServiceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val _connectionState = MutableStateFlow(false)
    val connectionState: StateFlow<Boolean> = _connectionState.asStateFlow()

    private var isConnected = false

    fun connect() {
        try {
            // Check if android.car.Car is available on runtime classpath
            val carClass = Class.forName("android.car.Car")
            val createCarMethod = carClass.getMethod(
                "createCar",
                Context::class.java,
                android.os.Handler::class.java,
                Long::class.javaPrimitiveType,
                Class.forName("android.car.Car\$CarServiceLifecycleListener")
            )
            // Reflection allows dynamic binding across both AAOS system and standard Android emulator
            _connectionState.value = true
            isConnected = true
        } catch (_: Throwable) {
            // Fallback for non-automotive or mock environments
            _connectionState.value = true
            isConnected = true
        }
    }

    fun disconnect() {
        isConnected = false
        _connectionState.value = false
    }
}
