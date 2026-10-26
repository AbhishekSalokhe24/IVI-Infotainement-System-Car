# ❄️ Phase 2: Climate Control & Vehicle Status

> **Parent Plan:** [00_Master_Implementation_Plan.md](file:///c:/Users/abhis/AOSP/Projects/IVI%20Project/AI%20Plans/00_Master_Implementation_Plan.md)  
> **Modules:** `features/climate`, `features/vehicle`  
> **Duration:** Weeks 6–8  
> **Goal:** Build HVAC controls and vehicle status dashboard with real-time Car API data

---

## 1. Climate Control Module

### 1.1 Feature Overview

| Feature | Description |
|---------|-------------|
| Temperature Control | Driver/Passenger zone temperature (16°C–30°C) |
| Fan Speed | 7-speed fan control with auto mode |
| Air Distribution | Face / Feet / Defrost / Auto modes |
| AC Toggle | Air conditioning on/off |
| Recirculation | Internal/External air toggle |
| Seat Heating/Cooling | 3-level seat temperature (driver/passenger) |
| Steering Wheel Heat | On/off toggle |
| Rear Defrost | Rear window defrost toggle |
| Sync Mode | Sync passenger zone to driver settings |

### 1.2 Climate UI Layout

```
┌──────────────────────────────────────────────────────────────┐
│                      CLIMATE CONTROL                         │
├──────────────────┬──────────────────┬────────────────────────┤
│                  │                  │                        │
│   DRIVER ZONE    │   CAR VISUAL     │   PASSENGER ZONE      │
│                  │                  │                        │
│  ┌──────────┐    │   ┌──────────┐   │   ┌──────────┐        │
│  │  22.0°C  │    │   │          │   │   │  22.0°C  │        │
│  │   ▲  ▼   │    │   │  [Car]   │   │   │   ▲  ▼   │        │
│  └──────────┘    │   │  [Top    │   │   └──────────┘        │
│                  │   │   View]  │   │                        │
│  Seat: ███░░     │   │          │   │   Seat: ██░░░          │
│                  │   └──────────┘   │                        │
├──────────────────┴──────────────────┴────────────────────────┤
│                                                              │
│  Fan: ○ ○ ● ○ ○ ○ ○  AUTO    │  AC  │ RECIRC │ SYNC       │
│                                                              │
│  Air: [😮 Face] [🦶 Feet] [🔲 Defrost] [🔄 Auto]           │
│                                                              │
└──────────────────────────────────────────────────────────────┘
```

### 1.3 Domain Models

```kotlin
// features/climate/src/main/kotlin/com/ivi/climate/domain/model/ClimateState.kt
package com.ivi.climate.domain.model

data class ClimateState(
    val driverTemperature: Float = 22.0f,    // °C
    val passengerTemperature: Float = 22.0f, // °C
    val fanSpeed: Int = 3,                   // 0-7 (0 = off)
    val isAutoMode: Boolean = false,
    val isACOn: Boolean = true,
    val isRecirculation: Boolean = false,
    val isSyncMode: Boolean = true,
    val airDistribution: AirDistribution = AirDistribution.AUTO,
    val driverSeatHeat: SeatHeatLevel = SeatHeatLevel.OFF,
    val passengerSeatHeat: SeatHeatLevel = SeatHeatLevel.OFF,
    val isRearDefrost: Boolean = false,
    val isSteeringWheelHeat: Boolean = false,
    val outsideTemperature: Float = 25.0f,   // °C
)

enum class AirDistribution {
    FACE, FEET, FACE_AND_FEET, DEFROST, AUTO
}

enum class SeatHeatLevel {
    OFF, LOW, MEDIUM, HIGH
}
```

### 1.4 Car API Integration (Data Source)

```kotlin
// features/climate/src/main/kotlin/com/ivi/climate/data/source/CarClimateDataSource.kt
package com.ivi.climate.data.source

import android.car.VehicleAreaSeat
import android.car.VehiclePropertyIds
import android.car.hardware.CarPropertyValue
import android.car.hardware.property.CarPropertyManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class CarClimateDataSource @Inject constructor(
    private val carPropertyManager: CarPropertyManager,
) {
    /**
     * Observe HVAC temperature for a specific zone (driver/passenger).
     */
    fun observeTemperature(zone: Int): Flow<Float> = callbackFlow {
        val callback = object : CarPropertyManager.CarPropertyEventCallback {
            override fun onChangeEvent(value: CarPropertyValue<*>) {
                trySend(value.value as Float)
            }
            override fun onErrorEvent(propertyId: Int, zone: Int) {
                // Handle error
            }
        }

        carPropertyManager.registerCallback(
            callback,
            VehiclePropertyIds.HVAC_TEMPERATURE_SET,
            CarPropertyManager.SENSOR_RATE_ONCHANGE
        )

        awaitClose {
            carPropertyManager.unregisterCallback(callback)
        }
    }

    fun setTemperature(zone: Int, temperature: Float) {
        carPropertyManager.setFloatProperty(
            VehiclePropertyIds.HVAC_TEMPERATURE_SET,
            zone,
            temperature
        )
    }

    fun observeFanSpeed(): Flow<Int> = callbackFlow {
        val callback = object : CarPropertyManager.CarPropertyEventCallback {
            override fun onChangeEvent(value: CarPropertyValue<*>) {
                trySend(value.value as Int)
            }
            override fun onErrorEvent(propertyId: Int, zone: Int) {}
        }

        carPropertyManager.registerCallback(
            callback,
            VehiclePropertyIds.HVAC_FAN_SPEED,
            CarPropertyManager.SENSOR_RATE_ONCHANGE
        )

        awaitClose {
            carPropertyManager.unregisterCallback(callback)
        }
    }

    fun setFanSpeed(speed: Int) {
        carPropertyManager.setIntProperty(
            VehiclePropertyIds.HVAC_FAN_SPEED,
            VehicleAreaSeat.SEAT_ROW_1_LEFT,
            speed
        )
    }

    fun toggleAC(enabled: Boolean) {
        carPropertyManager.setBooleanProperty(
            VehiclePropertyIds.HVAC_AC_ON,
            VehicleAreaSeat.SEAT_ROW_1_LEFT,
            enabled
        )
    }

    fun observeOutsideTemperature(): Flow<Float> = callbackFlow {
        val callback = object : CarPropertyManager.CarPropertyEventCallback {
            override fun onChangeEvent(value: CarPropertyValue<*>) {
                trySend(value.value as Float)
            }
            override fun onErrorEvent(propertyId: Int, zone: Int) {}
        }

        carPropertyManager.registerCallback(
            callback,
            VehiclePropertyIds.ENV_OUTSIDE_TEMPERATURE,
            CarPropertyManager.SENSOR_RATE_NORMAL
        )

        awaitClose {
            carPropertyManager.unregisterCallback(callback)
        }
    }
}
```

### 1.5 Climate Repository

```kotlin
// features/climate/src/main/kotlin/com/ivi/climate/data/repository/ClimateRepositoryImpl.kt
package com.ivi.climate.data.repository

class ClimateRepositoryImpl @Inject constructor(
    private val carClimateDataSource: CarClimateDataSource,
) : ClimateRepository {

    override fun observeClimateState(): Flow<ClimateState> = combine(
        carClimateDataSource.observeTemperature(DRIVER_ZONE),
        carClimateDataSource.observeTemperature(PASSENGER_ZONE),
        carClimateDataSource.observeFanSpeed(),
        carClimateDataSource.observeOutsideTemperature(),
    ) { driverTemp, passengerTemp, fanSpeed, outsideTemp ->
        ClimateState(
            driverTemperature = driverTemp,
            passengerTemperature = passengerTemp,
            fanSpeed = fanSpeed,
            outsideTemperature = outsideTemp,
        )
    }

    override suspend fun setDriverTemperature(temp: Float) {
        carClimateDataSource.setTemperature(DRIVER_ZONE, temp.coerceIn(16f, 30f))
    }

    override suspend fun setPassengerTemperature(temp: Float) {
        carClimateDataSource.setTemperature(PASSENGER_ZONE, temp.coerceIn(16f, 30f))
    }

    override suspend fun setFanSpeed(speed: Int) {
        carClimateDataSource.setFanSpeed(speed.coerceIn(0, 7))
    }

    override suspend fun toggleAC(enabled: Boolean) {
        carClimateDataSource.toggleAC(enabled)
    }

    companion object {
        const val DRIVER_ZONE = VehicleAreaSeat.SEAT_ROW_1_LEFT
        const val PASSENGER_ZONE = VehicleAreaSeat.SEAT_ROW_1_RIGHT
    }
}
```

### 1.6 Climate ViewModel

```kotlin
// features/climate/src/main/kotlin/com/ivi/climate/ui/ClimateViewModel.kt
package com.ivi.climate.ui

@HiltViewModel
class ClimateViewModel @Inject constructor(
    private val getClimateState: GetClimateStateUseCase,
    private val setTemperature: SetTemperatureUseCase,
    private val setFanSpeed: SetFanSpeedUseCase,
    private val toggleAC: ToggleACUseCase,
) : BaseViewModel<ClimateUiState, ClimateEvent, ClimateAction>(ClimateUiState()) {

    init {
        observeClimate()
    }

    override fun onAction(action: ClimateAction) {
        viewModelScope.launch {
            when (action) {
                is ClimateAction.SetDriverTemp -> {
                    updateState { copy(driverTemperature = action.temp) }
                    setTemperature(Zone.DRIVER, action.temp)
                }
                is ClimateAction.SetPassengerTemp -> {
                    updateState { copy(passengerTemperature = action.temp) }
                    setTemperature(Zone.PASSENGER, action.temp)
                }
                is ClimateAction.SetFanSpeed -> {
                    updateState { copy(fanSpeed = action.speed) }
                    setFanSpeed(action.speed)
                }
                is ClimateAction.ToggleAC -> {
                    val newState = !currentState.isACOn
                    updateState { copy(isACOn = newState) }
                    toggleAC(newState)
                }
                is ClimateAction.SetAirDistribution -> {
                    updateState { copy(airDistribution = action.mode) }
                }
                is ClimateAction.ToggleSync -> {
                    updateState { copy(isSyncMode = !currentState.isSyncMode) }
                }
                is ClimateAction.SetSeatHeat -> {
                    val nextLevel = action.currentLevel.next()
                    if (action.zone == Zone.DRIVER) {
                        updateState { copy(driverSeatHeat = nextLevel) }
                    } else {
                        updateState { copy(passengerSeatHeat = nextLevel) }
                    }
                }
            }
        }
    }

    private fun observeClimate() {
        viewModelScope.launch {
            getClimateState().collect { state ->
                updateState {
                    copy(
                        driverTemperature = state.driverTemperature,
                        passengerTemperature = state.passengerTemperature,
                        fanSpeed = state.fanSpeed,
                        outsideTemperature = state.outsideTemperature,
                    )
                }
            }
        }
    }
}

data class ClimateUiState(
    val driverTemperature: Float = 22.0f,
    val passengerTemperature: Float = 22.0f,
    val fanSpeed: Int = 3,
    val isAutoMode: Boolean = false,
    val isACOn: Boolean = true,
    val isRecirculation: Boolean = false,
    val isSyncMode: Boolean = true,
    val airDistribution: AirDistribution = AirDistribution.AUTO,
    val driverSeatHeat: SeatHeatLevel = SeatHeatLevel.OFF,
    val passengerSeatHeat: SeatHeatLevel = SeatHeatLevel.OFF,
    val outsideTemperature: Float = 25.0f,
) : UiState

sealed interface ClimateAction {
    data class SetDriverTemp(val temp: Float) : ClimateAction
    data class SetPassengerTemp(val temp: Float) : ClimateAction
    data class SetFanSpeed(val speed: Int) : ClimateAction
    data object ToggleAC : ClimateAction
    data class SetAirDistribution(val mode: AirDistribution) : ClimateAction
    data object ToggleSync : ClimateAction
    data class SetSeatHeat(val zone: Zone, val currentLevel: SeatHeatLevel) : ClimateAction
}
```

---

## 2. Vehicle Status Module

### 2.1 Feature Overview

| Feature | Vehicle Property ID | Data Type |
|---------|-------------------|-----------|
| Speed | `PERF_VEHICLE_SPEED` | Float (m/s) |
| RPM | `ENGINE_RPM` | Float |
| Fuel Level | `FUEL_LEVEL` | Float (mL) |
| Fuel Range | `RANGE_REMAINING` | Float (m) |
| Odometer | `PERF_ODOMETER` | Float (km) |
| Gear | `GEAR_SELECTION` | Int (enum) |
| Tire Pressure | `TIRE_PRESSURE` | Float (kPa) per wheel |
| Door Status | `DOOR_OPEN` | Boolean per door |
| Turn Signals | `TURN_SIGNAL_STATE` | Int (enum) |
| Parking Brake | `PARKING_BRAKE_ON` | Boolean |
| EV Battery (if EV) | `EV_BATTERY_LEVEL` | Float (%) |
| EV Charge Status | `EV_CHARGE_STATE` | Int (enum) |

### 2.2 Vehicle Dashboard Layout

```
┌──────────────────────────────────────────────────────────────┐
│                     VEHICLE STATUS                           │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌────────────┐   ┌─────────────────────┐   ┌────────────┐  │
│  │  FUEL/EV   │   │                     │   │   RANGE    │  │
│  │            │   │     ┌───────┐       │   │            │  │
│  │  ████░░░   │   │    ╱│       │╲      │   │  420 km    │  │
│  │   67%      │   │   │ │ [CAR] │ │     │   │  remaining │  │
│  │            │   │   │ │       │ │     │   │            │  │
│  └────────────┘   │    ╲│       │╱      │   └────────────┘  │
│                   │     └───────┘       │                    │
│  ┌────────────┐   │                     │   ┌────────────┐  │
│  │ ODOMETER   │   │  FL:32 ●  ● FR:32  │   │   DOORS    │  │
│  │ 45,230 km  │   │  RL:32 ●  ● RR:31  │   │  All Closed│  │
│  └────────────┘   │   (Tire Pressure)   │   └────────────┘  │
│                   └─────────────────────┘                    │
│                                                              │
│  ┌──────────────────────────────────────────────────────────┐│
│  │  ADAS STATUS:  Lane Assist ✅  AEB ✅  Cruise ⚠️        ││
│  └──────────────────────────────────────────────────────────┘│
└──────────────────────────────────────────────────────────────┘
```

### 2.3 Vehicle Property Client

```kotlin
// libraries/carservice/src/main/kotlin/com/ivi/carservice/VehiclePropertyClient.kt
package com.ivi.carservice

import android.car.VehiclePropertyIds
import android.car.hardware.CarPropertyValue
import android.car.hardware.property.CarPropertyManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VehiclePropertyClient @Inject constructor(
    private val carServiceManager: CarServiceManager,
) {
    private val propertyManager: CarPropertyManager?
        get() = carServiceManager.propertyManager

    /**
     * Observe a vehicle property as a Flow.
     * Automatically registers/unregisters callbacks.
     */
    inline fun <reified T> observeProperty(propertyId: Int): Flow<T> = callbackFlow {
        val pm = propertyManager ?: return@callbackFlow

        val callback = object : CarPropertyManager.CarPropertyEventCallback {
            override fun onChangeEvent(value: CarPropertyValue<*>) {
                val typedValue = value.value as? T ?: return
                trySend(typedValue)
            }
            override fun onErrorEvent(propertyId: Int, zone: Int) {
                // Log error
            }
        }

        pm.registerCallback(
            callback,
            propertyId,
            CarPropertyManager.SENSOR_RATE_ONCHANGE
        )

        // Emit initial value
        try {
            val initial = pm.getProperty<T>(propertyId, 0)
            if (initial?.value != null) {
                trySend(initial.value as T)
            }
        } catch (e: Exception) { /* Property may not be available */ }

        awaitClose {
            pm.unregisterCallback(callback, propertyId)
        }
    }

    fun observeSpeed(): Flow<Float> = observeProperty(VehiclePropertyIds.PERF_VEHICLE_SPEED)
    fun observeFuelLevel(): Flow<Float> = observeProperty(VehiclePropertyIds.FUEL_LEVEL)
    fun observeRpm(): Flow<Float> = observeProperty(VehiclePropertyIds.ENGINE_RPM)
    fun observeGear(): Flow<Int> = observeProperty(VehiclePropertyIds.GEAR_SELECTION)
    fun observeOdometer(): Flow<Float> = observeProperty(VehiclePropertyIds.PERF_ODOMETER)
    fun observeParkingBrake(): Flow<Boolean> = observeProperty(VehiclePropertyIds.PARKING_BRAKE_ON)
}
```

### 2.4 Vehicle Dashboard ViewModel

```kotlin
// features/vehicle/src/main/kotlin/com/ivi/vehicle/ui/dashboard/DashboardViewModel.kt
package com.ivi.vehicle.ui.dashboard

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val vehiclePropertyClient: VehiclePropertyClient,
) : BaseViewModel<DashboardUiState, Nothing, DashboardAction>(DashboardUiState()) {

    init {
        observeAllProperties()
    }

    override fun onAction(action: DashboardAction) {
        // Dashboard is mostly read-only
    }

    private fun observeAllProperties() {
        viewModelScope.launch {
            vehiclePropertyClient.observeSpeed().collect { speed ->
                // Convert m/s to km/h
                updateState { copy(speedKmh = (speed * 3.6f).roundToInt()) }
            }
        }
        viewModelScope.launch {
            vehiclePropertyClient.observeFuelLevel().collect { fuel ->
                updateState { copy(fuelLevelPercent = fuel) }
            }
        }
        viewModelScope.launch {
            vehiclePropertyClient.observeRpm().collect { rpm ->
                updateState { copy(rpm = rpm.roundToInt()) }
            }
        }
        viewModelScope.launch {
            vehiclePropertyClient.observeGear().collect { gear ->
                updateState { copy(gearDisplay = gearToString(gear)) }
            }
        }
        viewModelScope.launch {
            vehiclePropertyClient.observeOdometer().collect { odometer ->
                updateState { copy(odometerKm = odometer.roundToInt()) }
            }
        }
    }

    private fun gearToString(gear: Int): String = when (gear) {
        0 -> "P"
        1 -> "R"
        2 -> "N"
        3 -> "D"
        else -> "D${gear - 2}"
    }
}

data class DashboardUiState(
    val speedKmh: Int = 0,
    val rpm: Int = 0,
    val fuelLevelPercent: Float = 0f,
    val rangeKm: Int = 0,
    val odometerKm: Int = 0,
    val gearDisplay: String = "P",
    val tirePressures: TirePressures = TirePressures(),
    val doorStates: DoorStates = DoorStates(),
    val parkingBrakeOn: Boolean = true,
    val isEV: Boolean = false,
    val evBatteryPercent: Float = 0f,
    val evChargeState: ChargeState = ChargeState.NOT_CHARGING,
) : UiState

data class TirePressures(
    val frontLeft: Float = 32f,
    val frontRight: Float = 32f,
    val rearLeft: Float = 32f,
    val rearRight: Float = 32f,
) {
    fun isAnyLow(threshold: Float = 28f): Boolean =
        frontLeft < threshold || frontRight < threshold ||
        rearLeft < threshold || rearRight < threshold
}

data class DoorStates(
    val frontLeft: Boolean = false,  // true = open
    val frontRight: Boolean = false,
    val rearLeft: Boolean = false,
    val rearRight: Boolean = false,
    val trunk: Boolean = false,
    val hood: Boolean = false,
) {
    val allClosed: Boolean get() = !frontLeft && !frontRight &&
        !rearLeft && !rearRight && !trunk && !hood
}
```

---

## 3. Mock Data for Development

```kotlin
// libraries/testing/src/main/kotlin/com/ivi/testing/FakeCarService.kt
package com.ivi.testing

/**
 * Fake implementation of VehiclePropertyClient for development
 * when real Car API is not available (e.g., standard Android emulator).
 */
class FakeVehiclePropertyClient : VehiclePropertyClient {

    private val _speed = MutableStateFlow(0f)
    private val _fuel = MutableStateFlow(67f)
    private val _rpm = MutableStateFlow(0f)

    override fun observeSpeed(): Flow<Float> = _speed

    override fun observeFuelLevel(): Flow<Float> = _fuel

    // Simulate driving for development
    fun simulateDriving() {
        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                _speed.value = (40..120).random().toFloat() / 3.6f  // km/h to m/s
                _rpm.value = (1000..5000).random().toFloat()
                _fuel.value = (_fuel.value - 0.01f).coerceAtLeast(0f)
                delay(1000)
            }
        }
    }
}
```

---

## 4. Verification Checklist

- [ ] Climate control screen renders with driver/passenger zones
- [ ] Temperature adjustment sends CarPropertyManager updates
- [ ] Fan speed control works with 7 levels + auto
- [ ] Air distribution buttons toggle correctly
- [ ] AC, recirculation, sync toggles work
- [ ] Seat heat cycles through OFF → LOW → MED → HIGH
- [ ] Vehicle dashboard shows real-time speed, RPM, fuel
- [ ] Tire pressure visualization highlights low-pressure tires
- [ ] Door status shows open doors with visual indicator
- [ ] Gear indicator updates in real-time
- [ ] Mock data works when Car API is unavailable
- [ ] Temperature animation is smooth (no jumps)
- [ ] All touch targets ≥ 76dp

---

*Next: [05_Media_Center.md](file:///c:/Users/abhis/AOSP/Projects/IVI%20Project/AI%20Plans/05_Media_Center.md) — Media browsing, playback controls, radio, and audio zone management*
