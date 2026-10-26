# 📦 Phase 0: Project Setup & Architecture

> **Parent Plan:** [00_Master_Implementation_Plan.md](file:///c:/Users/abhis/AOSP/Projects/IVI%20Project/AI%20Plans/00_Master_Implementation_Plan.md)  
> **Duration:** Weeks 1–2  
> **Goal:** Set up AOSP build environment, scaffold multi-module project, establish architecture patterns

---

## 1. AOSP Build Environment Setup

### 1.1 Prerequisites

| Requirement | Specification |
|-------------|--------------|
| **OS** | Ubuntu 22.04 LTS (or WSL2 on Windows) |
| **Disk Space** | ≥ 400 GB (AOSP source + build artifacts) |
| **RAM** | ≥ 32 GB (64 GB recommended) |
| **CPU** | ≥ 8 cores |
| **JDK** | OpenJDK 17 (bundled with AOSP) |
| **Python** | Python 3.9+ |

### 1.2 AOSP Source Download

```bash
# Install repo tool
mkdir -p ~/bin
curl https://storage.googleapis.com/git-repo-downloads/repo > ~/bin/repo
chmod a+x ~/bin/repo
export PATH=~/bin:$PATH

# Initialize AOSP source (Android 14 Automotive)
mkdir ~/aosp && cd ~/aosp
repo init -u https://android.googlesource.com/platform/manifest \
    -b android-14.0.0_r1 \
    --depth=1

# Sync source (this takes several hours)
repo sync -c -j$(nproc) --no-tags --no-clone-bundle
```

### 1.3 Automotive Build Target

```bash
# Set up build environment
source build/envsetup.sh

# Choose automotive target
lunch sdk_car_x86_64-userdebug
# Alternative for ARM: lunch sdk_car_arm64-userdebug

# Build the system image
m -j$(nproc)

# Launch the automotive emulator
emulator -no-snapshot -wipe-data
```

### 1.4 Android Studio Setup

```
Android Studio Hedgehog (or later) with:
├── Android Automotive OS emulator system images
├── Kotlin plugin 1.9+
├── Compose plugin
└── Car App Library templates
```

**Gradle JDK Configuration:**
```properties
# gradle.properties
org.gradle.jvmargs=-Xmx8g -XX:+UseParallelGC
org.gradle.parallel=true
org.gradle.caching=true
android.useAndroidX=true
kotlin.code.style=official
```

---

## 2. Project Scaffolding

### 2.1 Complete Project Structure

```
IVI-Project/
│
├── build.gradle.kts                    ← Root build configuration
├── settings.gradle.kts                 ← Module declarations
├── gradle.properties                   ← Build properties
├── gradle/
│   ├── wrapper/
│   └── libs.versions.toml             ← Version catalog
│
├── buildSrc/                           ← Convention plugins
│   ├── build.gradle.kts
│   └── src/main/kotlin/
│       ├── AndroidApplicationConventionPlugin.kt
│       ├── AndroidLibraryConventionPlugin.kt
│       ├── AndroidComposeConventionPlugin.kt
│       └── AndroidHiltConventionPlugin.kt
│
├── app/                                ← Main launcher/shell application
│   ├── build.gradle.kts
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml
│   │   │   ├── kotlin/com/ivi/launcher/
│   │   │   │   ├── IVIApplication.kt
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── di/
│   │   │   │   │   └── AppModule.kt
│   │   │   │   ├── ui/
│   │   │   │   │   ├── home/
│   │   │   │   │   │   ├── HomeScreen.kt
│   │   │   │   │   │   └── HomeViewModel.kt
│   │   │   │   │   ├── shell/
│   │   │   │   │   │   ├── SystemShell.kt
│   │   │   │   │   │   ├── StatusBar.kt
│   │   │   │   │   │   └── NavigationBar.kt
│   │   │   │   │   └── drawer/
│   │   │   │   │       ├── AppDrawer.kt
│   │   │   │   │       └── AppDrawerViewModel.kt
│   │   │   │   └── navigation/
│   │   │   │       └── IVINavGraph.kt
│   │   │   └── res/
│   │   ├── debug/
│   │   └── release/
│   └── Android.bp                      ← AOSP Soong build file
│
├── features/                           ← Feature modules
│   ├── media/
│   │   ├── build.gradle.kts
│   │   └── src/main/kotlin/com/ivi/media/
│   │       ├── di/MediaModule.kt
│   │       ├── data/
│   │       │   ├── repository/MediaRepositoryImpl.kt
│   │       │   └── source/
│   │       │       ├── CarMediaDataSource.kt
│   │       │       └── LocalMediaDataSource.kt
│   │       ├── domain/
│   │       │   ├── model/
│   │       │   │   ├── MediaItem.kt
│   │       │   │   ├── PlaybackState.kt
│   │       │   │   └── MediaSource.kt
│   │       │   ├── repository/MediaRepository.kt
│   │       │   └── usecase/
│   │       │       ├── GetMediaSourcesUseCase.kt
│   │       │       ├── PlayMediaUseCase.kt
│   │       │       └── BrowseMediaUseCase.kt
│   │       └── ui/
│   │           ├── browser/
│   │           │   ├── MediaBrowserScreen.kt
│   │           │   └── MediaBrowserViewModel.kt
│   │           ├── player/
│   │           │   ├── NowPlayingScreen.kt
│   │           │   └── NowPlayingViewModel.kt
│   │           └── radio/
│   │               ├── RadioScreen.kt
│   │               └── RadioViewModel.kt
│   │
│   ├── navigation/
│   │   ├── build.gradle.kts
│   │   └── src/main/kotlin/com/ivi/navigation/
│   │       ├── di/NavigationModule.kt
│   │       ├── data/
│   │       ├── domain/
│   │       └── ui/
│   │           ├── map/MapScreen.kt
│   │           ├── search/SearchScreen.kt
│   │           └── directions/DirectionsScreen.kt
│   │
│   ├── climate/
│   │   ├── build.gradle.kts
│   │   └── src/main/kotlin/com/ivi/climate/
│   │       ├── di/ClimateModule.kt
│   │       ├── data/
│   │       │   ├── repository/ClimateRepositoryImpl.kt
│   │       │   └── source/CarClimateDataSource.kt
│   │       ├── domain/
│   │       │   ├── model/ClimateState.kt
│   │       │   ├── repository/ClimateRepository.kt
│   │       │   └── usecase/
│   │       │       ├── GetClimateStateUseCase.kt
│   │       │       └── SetTemperatureUseCase.kt
│   │       └── ui/
│   │           ├── ClimateControlScreen.kt
│   │           └── ClimateViewModel.kt
│   │
│   ├── vehicle/
│   │   ├── build.gradle.kts
│   │   └── src/main/kotlin/com/ivi/vehicle/
│   │       ├── di/VehicleModule.kt
│   │       ├── data/
│   │       ├── domain/
│   │       └── ui/
│   │           ├── dashboard/DashboardScreen.kt
│   │           └── diagnostics/DiagnosticsScreen.kt
│   │
│   ├── phone/
│   │   ├── build.gradle.kts
│   │   └── src/main/kotlin/com/ivi/phone/
│   │       ├── di/PhoneModule.kt
│   │       ├── data/
│   │       ├── domain/
│   │       └── ui/
│   │           ├── dialer/DialerScreen.kt
│   │           ├── contacts/ContactsScreen.kt
│   │           └── calllog/CallLogScreen.kt
│   │
│   └── settings/
│       ├── build.gradle.kts
│       └── src/main/kotlin/com/ivi/settings/
│           ├── di/SettingsModule.kt
│           ├── data/
│           ├── domain/
│           └── ui/
│               ├── SettingsScreen.kt
│               └── sections/
│
├── libraries/                          ← Shared libraries
│   ├── designsystem/
│   │   ├── build.gradle.kts
│   │   └── src/main/kotlin/com/ivi/designsystem/
│   │       ├── theme/
│   │       │   ├── IVITheme.kt
│   │       │   ├── Color.kt
│   │       │   ├── Typography.kt
│   │       │   ├── Spacing.kt
│   │       │   └── Shape.kt
│   │       ├── components/
│   │       │   ├── IVIButton.kt
│   │       │   ├── IVICard.kt
│   │       │   ├── IVISlider.kt
│   │       │   ├── IVIToggle.kt
│   │       │   ├── IVIIconButton.kt
│   │       │   └── IVIBottomSheet.kt
│   │       ├── icons/
│   │       │   └── IVIIcons.kt
│   │       └── animation/
│   │           └── IVIAnimations.kt
│   │
│   ├── carservice/
│   │   ├── build.gradle.kts
│   │   └── src/main/kotlin/com/ivi/carservice/
│   │       ├── CarServiceManager.kt
│   │       ├── VehiclePropertyClient.kt
│   │       ├── ClimateClient.kt
│   │       ├── MediaClient.kt
│   │       ├── AudioClient.kt
│   │       └── UxRestrictionClient.kt
│   │
│   ├── core/
│   │   ├── build.gradle.kts
│   │   └── src/main/kotlin/com/ivi/core/
│   │       ├── base/
│   │       │   ├── BaseViewModel.kt
│   │       │   └── UiState.kt
│   │       ├── extensions/
│   │       │   ├── FlowExtensions.kt
│   │       │   └── ContextExtensions.kt
│   │       ├── di/
│   │       │   ├── CoroutineModule.kt
│   │       │   └── Qualifiers.kt
│   │       └── util/
│   │           ├── Resource.kt
│   │           └── Constants.kt
│   │
│   └── testing/
│       ├── build.gradle.kts
│       └── src/main/kotlin/com/ivi/testing/
│           ├── FakeCarService.kt
│           ├── TestCoroutineRule.kt
│           └── ComposeTestExtensions.kt
│
└── aosp/                               ← AOSP integration files
    ├── sepolicy/
    │   └── ivi_app.te
    ├── overlays/
    │   └── IVIOverlay/
    │       └── res/values/config.xml
    ├── permissions/
    │   └── com.ivi.xml
    └── init/
        └── ivi.rc
```

### 2.2 Version Catalog (`gradle/libs.versions.toml`)

```toml
[versions]
kotlin = "1.9.22"
agp = "8.2.2"
compose-bom = "2024.02.00"
compose-compiler = "1.5.10"
hilt = "2.50"
room = "2.6.1"
coroutines = "1.8.0"
navigation = "2.7.7"
lifecycle = "2.7.0"
car-app = "1.4.0"
car-ui-lib = "2.6.0"
coil = "2.5.0"
datastore = "1.0.0"
kotlinx-serialization = "1.6.3"

# Testing
junit5 = "5.10.2"
mockk = "1.13.9"
turbine = "1.0.0"
compose-ui-test = "1.6.3"

[libraries]
# Compose
compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "compose-bom" }
compose-ui = { group = "androidx.compose.ui", name = "ui" }
compose-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
compose-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
compose-material3 = { group = "androidx.compose.material3", name = "material3" }
compose-material-icons = { group = "androidx.compose.material", name = "material-icons-extended" }
compose-animation = { group = "androidx.compose.animation", name = "animation" }

# Architecture
lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycle" }
lifecycle-runtime-compose = { group = "androidx.lifecycle", name = "lifecycle-runtime-compose", version.ref = "lifecycle" }
navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigation" }

# DI
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-compiler = { group = "com.google.dagger", name = "hilt-compiler", version.ref = "hilt" }
hilt-navigation-compose = { group = "androidx.hilt", name = "hilt-navigation-compose", version = "1.2.0" }

# Car / Automotive
car-app-library = { group = "androidx.car.app", name = "app", version.ref = "car-app" }
car-ui-lib = { group = "com.android.car.ui", name = "car-ui-lib", version.ref = "car-ui-lib" }

# Data
room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }
datastore-preferences = { group = "androidx.datastore", name = "datastore-preferences", version.ref = "datastore" }
kotlinx-serialization-json = { group = "org.jetbrains.kotlinx", name = "kotlinx-serialization-json", version.ref = "kotlinx-serialization" }

# Coroutines
coroutines-core = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version.ref = "coroutines" }
coroutines-android = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-android", version.ref = "coroutines" }

# Image Loading
coil-compose = { group = "io.coil-kt", name = "coil-compose", version.ref = "coil" }

# Testing
junit5-api = { group = "org.junit.jupiter", name = "junit-jupiter-api", version.ref = "junit5" }
junit5-engine = { group = "org.junit.jupiter", name = "junit-jupiter-engine", version.ref = "junit5" }
mockk = { group = "io.mockk", name = "mockk", version.ref = "mockk" }
turbine = { group = "app.cash.turbine", name = "turbine", version.ref = "turbine" }
coroutines-test = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-test", version.ref = "coroutines" }
compose-ui-test-junit4 = { group = "androidx.compose.ui", name = "ui-test-junit4" }
compose-ui-test-manifest = { group = "androidx.compose.ui", name = "ui-test-manifest" }

[bundles]
compose = ["compose-ui", "compose-ui-graphics", "compose-ui-tooling-preview", "compose-material3", "compose-material-icons", "compose-animation"]
lifecycle = ["lifecycle-viewmodel-compose", "lifecycle-runtime-compose"]
room = ["room-runtime", "room-ktx"]
testing = ["junit5-api", "mockk", "turbine", "coroutines-test"]

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
android-library = { id = "com.android.library", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
hilt = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
ksp = { id = "com.google.devtools.ksp", version = "1.9.22-1.0.17" }
```

### 2.3 Root `build.gradle.kts`

```kotlin
// build.gradle.kts (root)
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}
```

### 2.4 `settings.gradle.kts`

```kotlin
// settings.gradle.kts
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "IVI-Infotainment"

// App
include(":app")

// Features
include(":features:media")
include(":features:navigation")
include(":features:climate")
include(":features:vehicle")
include(":features:phone")
include(":features:settings")

// Libraries
include(":libraries:designsystem")
include(":libraries:carservice")
include(":libraries:core")
include(":libraries:testing")
```

---

## 3. Architecture Patterns

### 3.1 Base ViewModel

```kotlin
// libraries/core/src/main/kotlin/com/ivi/core/base/BaseViewModel.kt
package com.ivi.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Base ViewModel implementing UDF (Unidirectional Data Flow) pattern.
 *
 * @param S UI State type
 * @param E One-time Event type (navigation, snackbar, etc.)
 * @param A User Action/Intent type
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
        _uiState.value = currentState.reducer()
    }

    protected fun sendEvent(event: E) {
        viewModelScope.launch {
            _events.send(event)
        }
    }

    abstract fun onAction(action: A)
}
```

### 3.2 UiState Sealed Interface

```kotlin
// libraries/core/src/main/kotlin/com/ivi/core/base/UiState.kt
package com.ivi.core.base

interface UiState

/**
 * Generic wrapper for async data loading states.
 */
sealed interface Resource<out T> {
    data object Loading : Resource<Nothing>
    data class Success<T>(val data: T) : Resource<T>
    data class Error(val message: String, val throwable: Throwable? = null) : Resource<Nothing>
}
```

### 3.3 Car Service Manager

```kotlin
// libraries/carservice/src/main/kotlin/com/ivi/carservice/CarServiceManager.kt
package com.ivi.carservice

import android.car.Car
import android.car.hardware.CarPropertyManager
import android.car.hardware.hvac.CarHvacManager
import android.car.media.CarAudioManager
import android.car.drivingstate.CarUxRestrictionsManager
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarServiceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var car: Car? = null

    private val _connectionState = MutableStateFlow(false)
    val connectionState: StateFlow<Boolean> = _connectionState

    val propertyManager: CarPropertyManager?
        get() = car?.getCarManager(Car.PROPERTY_SERVICE) as? CarPropertyManager

    val audioManager: CarAudioManager?
        get() = car?.getCarManager(Car.AUDIO_SERVICE) as? CarAudioManager

    val uxRestrictionsManager: CarUxRestrictionsManager?
        get() = car?.getCarManager(Car.CAR_UX_RESTRICTION_SERVICE) as? CarUxRestrictionsManager

    fun connect() {
        car = Car.createCar(context, null, Car.CAR_WAIT_TIMEOUT_WAIT_FOREVER) { car, ready ->
            _connectionState.value = ready
            if (!ready) {
                this.car = null
            }
        }
    }

    fun disconnect() {
        car?.disconnect()
        car = null
        _connectionState.value = false
    }
}
```

### 3.4 Coroutine Module (DI)

```kotlin
// libraries/core/src/main/kotlin/com/ivi/core/di/CoroutineModule.kt
package com.ivi.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher

@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {

    @IoDispatcher
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @DefaultDispatcher
    @Provides
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}
```

---

## 4. AOSP Integration Files

### 4.1 Android.bp (Soong Build)

```
// aosp/Android.bp
android_app {
    name: "IVILauncher",
    srcs: ["app/src/main/**/*.kt"],
    resource_dirs: ["app/src/main/res"],
    manifest: "app/src/main/AndroidManifest.xml",

    platform_apis: true,
    certificate: "platform",
    privileged: true,

    static_libs: [
        "androidx.compose.runtime_runtime",
        "androidx.compose.material3_material3",
        "androidx.car.app_app",
        "car-ui-lib",
        "hilt_android",
    ],

    required: [
        "privapp-permissions-ivi.xml",
    ],

    optimize: {
        enabled: true,
        proguard_flags_files: ["proguard-rules.pro"],
    },
}
```

### 4.2 SELinux Policy

```
# aosp/sepolicy/ivi_app.te
# SELinux policy for IVI application

type ivi_app, domain;
type ivi_app_data_file, file_type, data_file_type;

# Allow IVI app to access vehicle HAL
allow ivi_app vehicle_hal_service:service_manager find;
allow ivi_app carservice_service:service_manager find;

# Allow access to audio HAL
allow ivi_app audio_hal_service:service_manager find;

# Allow access to Bluetooth
allow ivi_app bluetooth_service:service_manager find;

# Allow network access (for navigation/maps)
net_domain(ivi_app)
```

### 4.3 Privileged Permissions

```xml
<!-- aosp/permissions/privapp-permissions-ivi.xml -->
<?xml version="1.0" encoding="utf-8"?>
<permissions>
    <privapp-permissions package="com.ivi.launcher">
        <permission name="android.permission.INTERACT_ACROSS_USERS"/>
        <permission name="android.permission.MANAGE_USERS"/>
        <permission name="android.car.permission.CAR_ENERGY"/>
        <permission name="android.car.permission.CAR_SPEED"/>
        <permission name="android.car.permission.CONTROL_CAR_CLIMATE"/>
        <permission name="android.car.permission.CAR_EXTERIOR_ENVIRONMENT"/>
        <permission name="android.car.permission.CAR_INFO"/>
        <permission name="android.car.permission.CAR_DYNAMICS_STATE"/>
        <permission name="android.car.permission.CAR_CONTROL_AUDIO_VOLUME"/>
    </privapp-permissions>
</permissions>
```

---

## 5. CI/CD Pipeline

### 5.1 GitHub Actions Workflow

```yaml
# .github/workflows/ci.yml
name: IVI CI Pipeline

on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main]

jobs:
  lint:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Run ktlint
        run: ./gradlew ktlintCheck
      - name: Run detekt
        run: ./gradlew detekt

  unit-test:
    runs-on: ubuntu-latest
    needs: lint
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Run unit tests
        run: ./gradlew testDebugUnitTest
      - name: Upload test results
        uses: actions/upload-artifact@v4
        with:
          name: test-results
          path: '**/build/reports/tests/'

  build:
    runs-on: ubuntu-latest
    needs: unit-test
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Build debug APKs
        run: ./gradlew assembleDebug
      - name: Upload APKs
        uses: actions/upload-artifact@v4
        with:
          name: debug-apks
          path: '**/build/outputs/apk/debug/*.apk'
```

---

## 6. Verification Checklist

- [ ] AOSP source tree synced and buildable
- [ ] Automotive emulator boots successfully
- [ ] Multi-module Gradle project builds without errors
- [ ] All module dependencies resolve correctly
- [ ] Version catalog entries are complete
- [ ] Convention plugins apply correctly to all modules
- [ ] Car Service Manager connects on emulator
- [ ] Base architecture classes compile and pass tests
- [ ] SELinux policies validated (for AOSP integration)
- [ ] CI pipeline runs green on initial commit

---

> [!TIP]
> For development, use the Gradle build with Android Studio. The AOSP/Soong build is only needed when building the complete system image for deployment on actual automotive hardware.

---

*Next: [02_Design_System.md](file:///c:/Users/abhis/AOSP/Projects/IVI%20Project/AI%20Plans/02_Design_System.md) — Complete design system with tokens, components, and theming*
