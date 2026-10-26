# 🗺️ Phase 4: Navigation

> **Parent Plan:** [00_Master_Implementation_Plan.md](file:///c:/Users/abhis/AOSP/Projects/IVI%20Project/AI%20Plans/00_Master_Implementation_Plan.md)  
> **Module:** `features/navigation`  
> **Duration:** Weeks 13–16  
> **Goal:** Map rendering, POI search, route planning, turn-by-turn directions, and cluster display integration

---

## 1. Navigation Architecture

### 1.1 Map Engine Options

| Option | Pros | Cons | License |
|--------|------|------|---------|
| **MapLibre GL Native** | Open source, offline, customizable | Requires tile server | BSD |
| **Google Maps for Automotive** | Full-featured, traffic, Street View | Requires GMS, licensing | Proprietary |
| **HERE SDK for Android** | Automotive-focused, offline, fleet APIs | Commercial license | Proprietary |
| **Mapbox** | Beautiful maps, customizable, GL-based | Paid above free tier | Proprietary |

> **Recommended**: MapLibre GL for AOSP builds (no GMS dependency), Google Maps for GAS (Google Automotive Services) builds.

### 1.2 Navigation Components

```
┌────────────────────────────────────────────────────────────┐
│                    Navigation Module                        │
│  ┌────────────┐  ┌────────────┐  ┌────────────────────┐   │
│  │  Map View   │  │  Search    │  │  Turn-by-Turn      │   │
│  │  (MapLibre) │  │  + POI     │  │  Guidance Engine    │   │
│  └──────┬─────┘  └──────┬─────┘  └──────────┬─────────┘   │
│         │               │                    │              │
│  ┌──────┴───────────────┴────────────────────┴──────────┐  │
│  │              Navigation Service (Background)          │  │
│  │  - Route calculation                                  │  │
│  │  - Position tracking (GPS + Dead Reckoning)           │  │
│  │  - Maneuver detection                                 │  │
│  │  - ETA calculation                                    │  │
│  └──────┬────────────────────────────────────────────────┘  │
│         │                                                    │
│  ┌──────┴────────────────────────────────────────────────┐  │
│  │    CarNavigationStatusManager (Cluster Updates)        │  │
│  └───────────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────────┘
```

---

## 2. Map Screen Implementation

### 2.1 Map Screen Layout

```
┌──────────────────────────────────────────────────────────────┐
│  🔍 Search Bar                              [📍] [🧭] [📐]  │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│                                                              │
│                     MAP VIEW                                 │
│                   (Full Screen)                              │
│                                                              │
│                      📍                                      │
│                   (Current Location)                         │
│                                                              │
│                                                              │
├──────────────────────────────────────────────────────────────┤
│  Upcoming Maneuver Card (when navigating):                   │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  ↱ Turn right on Main Street    │  500 m  │  3 min  │   │
│  └──────────────────────────────────────────────────────┘   │
└──────────────────────────────────────────────────────────────┘
```

### 2.2 Map Screen Composable

```kotlin
// features/navigation/src/main/kotlin/com/ivi/navigation/ui/map/MapScreen.kt
package com.ivi.navigation.ui.map

@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        // Map View (AndroidView wrapping MapLibre/Google Maps)
        MapView(
            modifier = Modifier.fillMaxSize(),
            cameraPosition = uiState.cameraPosition,
            currentLocation = uiState.currentLocation,
            route = uiState.activeRoute,
            pois = uiState.visiblePOIs,
            onMapClick = { latLng -> viewModel.onAction(MapAction.MapTapped(latLng)) },
            onCameraMoved = { position -> viewModel.onAction(MapAction.CameraMoved(position)) },
        )

        // Floating Search Bar (top)
        SearchBar(
            query = uiState.searchQuery,
            onQueryChange = { viewModel.onAction(MapAction.SearchQueryChanged(it)) },
            onSearch = { viewModel.onAction(MapAction.PerformSearch) },
            suggestions = uiState.searchSuggestions,
            onSuggestionClick = { viewModel.onAction(MapAction.SelectSuggestion(it)) },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(IVISpacing.MD)
                .fillMaxWidth(0.7f),
        )

        // Map controls (right side)
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(IVISpacing.MD),
            verticalArrangement = Arrangement.spacedBy(IVISpacing.SM),
        ) {
            // Current Location
            IVIIconButton(
                icon = Icons.Default.MyLocation,
                contentDescription = "My Location",
                onClick = { viewModel.onAction(MapAction.CenterOnLocation) },
            )
            // Zoom In
            IVIIconButton(
                icon = Icons.Default.Add,
                contentDescription = "Zoom In",
                onClick = { viewModel.onAction(MapAction.ZoomIn) },
            )
            // Zoom Out
            IVIIconButton(
                icon = Icons.Default.Remove,
                contentDescription = "Zoom Out",
                onClick = { viewModel.onAction(MapAction.ZoomOut) },
            )
            // Toggle 2D/3D
            IVIIconButton(
                icon = Icons.Default.Terrain,
                contentDescription = "Toggle View",
                onClick = { viewModel.onAction(MapAction.TogglePerspective) },
            )
        }

        // Navigation Guidance Card (bottom)
        if (uiState.isNavigating) {
            NavigationGuidanceCard(
                nextManeuver = uiState.nextManeuver,
                distanceToManeuver = uiState.distanceToManeuver,
                eta = uiState.eta,
                remainingDistance = uiState.remainingDistance,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(IVISpacing.MD),
            )
        }

        // Route Overview (when route is calculated but not started)
        if (uiState.routePreview != null && !uiState.isNavigating) {
            RoutePreviewCard(
                route = uiState.routePreview!!,
                onStartNavigation = { viewModel.onAction(MapAction.StartNavigation) },
                onDismiss = { viewModel.onAction(MapAction.DismissRoute) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(IVISpacing.MD),
            )
        }
    }
}
```

### 2.3 MapView Integration (AndroidView)

```kotlin
// features/navigation/src/main/kotlin/com/ivi/navigation/ui/map/MapView.kt
package com.ivi.navigation.ui.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Wraps MapLibre GL MapView in a Compose-compatible component.
 * For Google Maps, replace with Google Maps Compose library.
 */
@Composable
fun MapView(
    modifier: Modifier = Modifier,
    cameraPosition: CameraPosition,
    currentLocation: LatLng?,
    route: RouteGeometry?,
    pois: List<POI>,
    onMapClick: (LatLng) -> Unit,
    onCameraMoved: (CameraPosition) -> Unit,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            // Initialize MapLibre MapView
            org.maplibre.android.maps.MapView(context).apply {
                getMapAsync { map ->
                    // Set up map style (dark automotive theme)
                    map.setStyle("asset://automotive_dark_style.json") { style ->
                        // Enable location component
                        enableLocationComponent(map, style)

                        // Set up click listeners
                        map.addOnMapClickListener { point ->
                            onMapClick(LatLng(point.latitude, point.longitude))
                            true
                        }
                    }
                }
            }
        },
        update = { mapView ->
            mapView.getMapAsync { map ->
                // Update camera position
                map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        org.maplibre.android.geometry.LatLng(
                            cameraPosition.latitude,
                            cameraPosition.longitude
                        ),
                        cameraPosition.zoom
                    ),
                    300 // animation duration ms
                )

                // Draw route polyline
                route?.let { drawRoute(map, it) }

                // Update POI markers
                updatePOIMarkers(map, pois)
            }
        }
    )
}
```

---

## 3. Navigation Service (Background)

```kotlin
// features/navigation/src/main/kotlin/com/ivi/navigation/service/NavigationService.kt
package com.ivi.navigation.service

import android.app.Service
import android.car.navigation.CarNavigationStatusManager
import android.content.Intent
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Background service that:
 * 1. Tracks GPS position
 * 2. Matches position to route
 * 3. Generates turn-by-turn maneuvers
 * 4. Updates cluster display via CarNavigationStatusManager
 * 5. Provides voice guidance via TTS
 */
@AndroidEntryPoint
class NavigationService : Service() {

    @Inject lateinit var routeEngine: RouteEngine
    @Inject lateinit var carServiceManager: CarServiceManager

    private var navStatusManager: CarNavigationStatusManager? = null

    override fun onCreate() {
        super.onCreate()
        navStatusManager = carServiceManager.car
            ?.getCarManager(Car.CAR_NAVIGATION_SERVICE) as? CarNavigationStatusManager
    }

    /**
     * Send navigation state to cluster display.
     */
    private fun updateClusterDisplay(maneuver: Maneuver) {
        val bundle = Bundle().apply {
            putInt("maneuver_type", maneuver.type.ordinal)
            putString("road_name", maneuver.roadName)
            putFloat("distance_meters", maneuver.distanceMeters)
            putString("eta", maneuver.eta)
        }

        navStatusManager?.sendNavigationStateChange(bundle)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
```

---

## 4. Domain Models

```kotlin
// features/navigation/src/main/kotlin/com/ivi/navigation/domain/model/NavigationModels.kt
package com.ivi.navigation.domain.model

data class LatLng(val latitude: Double, val longitude: Double)

data class CameraPosition(
    val latitude: Double,
    val longitude: Double,
    val zoom: Float = 15f,
    val tilt: Float = 0f,
    val bearing: Float = 0f,
)

data class RouteGeometry(
    val points: List<LatLng>,
    val distanceMeters: Float,
    val durationSeconds: Int,
    val trafficDurationSeconds: Int? = null,
)

data class RoutePreview(
    val routes: List<RouteGeometry>,  // Up to 3 alternatives
    val destination: Place,
    val selectedIndex: Int = 0,
) {
    val selectedRoute: RouteGeometry get() = routes[selectedIndex]
    val etaFormatted: String get() {
        val mins = selectedRoute.durationSeconds / 60
        return if (mins >= 60) "${mins / 60}h ${mins % 60}min" else "${mins} min"
    }
    val distanceFormatted: String get() {
        val km = selectedRoute.distanceMeters / 1000
        return if (km >= 1) "%.1f km".format(km) else "${selectedRoute.distanceMeters.toInt()} m"
    }
}

data class Maneuver(
    val type: ManeuverType,
    val roadName: String,
    val distanceMeters: Float,
    val eta: String,
    val instruction: String,
)

enum class ManeuverType {
    STRAIGHT,
    TURN_LEFT, TURN_RIGHT,
    SLIGHT_LEFT, SLIGHT_RIGHT,
    SHARP_LEFT, SHARP_RIGHT,
    U_TURN_LEFT, U_TURN_RIGHT,
    ROUNDABOUT,
    MERGE_LEFT, MERGE_RIGHT,
    EXIT_LEFT, EXIT_RIGHT,
    ARRIVE,
}

data class Place(
    val name: String,
    val address: String,
    val location: LatLng,
    val category: PlaceCategory? = null,
)

enum class PlaceCategory {
    GAS_STATION, EV_CHARGING, RESTAURANT, PARKING,
    HOSPITAL, HOTEL, ATM, SHOPPING, AIRPORT,
}

data class POI(
    val id: String,
    val place: Place,
    val icon: Int, // drawable resource
)
```

---

## 5. Search & Geocoding

```kotlin
// features/navigation/src/main/kotlin/com/ivi/navigation/data/source/GeocodingDataSource.kt
package com.ivi.navigation.data.source

/**
 * Geocoding and POI search using:
 * - Nominatim (OpenStreetMap) for AOSP builds
 * - Google Places API for GAS builds
 */
interface GeocodingDataSource {
    suspend fun search(query: String, near: LatLng? = null): List<Place>
    suspend fun reverseGeocode(location: LatLng): Place?
    suspend fun getSuggestions(query: String, near: LatLng? = null): List<String>
    suspend fun searchPOI(category: PlaceCategory, near: LatLng, radiusMeters: Int = 5000): List<POI>
}

// Nominatim implementation for AOSP
class NominatimGeocodingDataSource @Inject constructor(
    private val httpClient: OkHttpClient,
) : GeocodingDataSource {

    override suspend fun search(query: String, near: LatLng?): List<Place> {
        val url = buildString {
            append("https://nominatim.openstreetmap.org/search?")
            append("q=${URLEncoder.encode(query, "UTF-8")}")
            append("&format=json&addressdetails=1&limit=10")
            near?.let {
                append("&viewbox=${it.longitude-0.1},${it.latitude+0.1},${it.longitude+0.1},${it.latitude-0.1}")
                append("&bounded=1")
            }
        }

        // Execute HTTP request and parse JSON response
        // ... implementation
        return emptyList()
    }

    override suspend fun reverseGeocode(location: LatLng): Place? {
        // Reverse geocode using Nominatim API
        return null
    }

    override suspend fun getSuggestions(query: String, near: LatLng?): List<String> {
        return search(query, near).map { "${it.name}, ${it.address}" }
    }

    override suspend fun searchPOI(
        category: PlaceCategory, near: LatLng, radiusMeters: Int
    ): List<POI> {
        // Use Overpass API for category search
        return emptyList()
    }
}
```

---

## 6. Route Engine

```kotlin
// features/navigation/src/main/kotlin/com/ivi/navigation/data/source/RouteEngine.kt
package com.ivi.navigation.data.source

/**
 * Route calculation using:
 * - OSRM (Open Source Routing Machine) for AOSP builds
 * - Google Directions API for GAS builds
 */
interface RouteEngine {
    suspend fun calculateRoute(
        origin: LatLng,
        destination: LatLng,
        waypoints: List<LatLng> = emptyList(),
        avoidTolls: Boolean = false,
        avoidHighways: Boolean = false,
        alternatives: Boolean = true,
    ): RouteResult

    suspend fun calculateETA(route: RouteGeometry, currentPosition: LatLng): ETAResult
}

data class RouteResult(
    val routes: List<RouteGeometry>,
    val error: String? = null,
)

data class ETAResult(
    val etaSeconds: Int,
    val remainingDistanceMeters: Float,
    val remainingManeuvers: List<Maneuver>,
)
```

---

## 7. Cluster Display Integration

```kotlin
// features/navigation/src/main/kotlin/com/ivi/navigation/cluster/ClusterNavigationRenderer.kt
package com.ivi.navigation.cluster

/**
 * Renders navigation information on the instrument cluster.
 * Uses CarNavigationStatusManager to send navigation events
 * that the cluster display app can render.
 */
class ClusterNavigationRenderer @Inject constructor(
    private val carServiceManager: CarServiceManager,
) {
    /**
     * Cluster displays typically show:
     * 1. Next maneuver icon (turn arrow)
     * 2. Distance to maneuver
     * 3. Road name
     * 4. ETA and remaining distance
     * 5. Simplified mini-map (optional)
     */
    fun sendNavigationState(
        isNavigating: Boolean,
        nextManeuver: Maneuver?,
        eta: String?,
        remainingDistance: String?,
    ) {
        val bundle = Bundle().apply {
            putBoolean("is_navigating", isNavigating)
            nextManeuver?.let {
                putInt("maneuver_type", it.type.ordinal)
                putString("maneuver_road", it.roadName)
                putFloat("maneuver_distance", it.distanceMeters)
                putString("maneuver_instruction", it.instruction)
            }
            eta?.let { putString("eta", it) }
            remainingDistance?.let { putString("remaining_distance", it) }
        }

        carServiceManager.car
            ?.getCarManager(Car.CAR_NAVIGATION_SERVICE)
            ?.let { it as CarNavigationStatusManager }
            ?.sendNavigationStateChange(bundle)
    }
}
```

---

## 8. Verification Checklist

- [ ] Map renders with automotive dark theme
- [ ] Current location marker shows and updates
- [ ] Map panning, zooming, and rotation work smoothly
- [ ] Search bar autocompletes with suggestions
- [ ] POI search by category works (gas, food, parking, etc.)
- [ ] Route calculation returns with alternatives
- [ ] Route polyline draws on map
- [ ] Route preview card shows ETA, distance, and Start button
- [ ] Turn-by-turn guidance displays correct maneuvers
- [ ] Maneuver distance counts down in real-time
- [ ] Voice guidance speaks turn instructions (TTS)
- [ ] Cluster display receives navigation state updates
- [ ] Navigation continues when IVI screen switches to other apps
- [ ] Offline map tiles work without internet
- [ ] Map style switches between day and night modes

---

*Next: [07_Phone_and_Communications.md](file:///c:/Users/abhis/AOSP/Projects/IVI%20Project/AI%20Plans/07_Phone_and_Communications.md) — Dialer, contacts, Bluetooth HFP, call log, SMS*
