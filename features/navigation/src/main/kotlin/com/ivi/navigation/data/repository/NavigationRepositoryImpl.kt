package com.ivi.navigation.data.repository

import com.ivi.navigation.domain.model.LatLng
import com.ivi.navigation.domain.model.Maneuver
import com.ivi.navigation.domain.model.ManeuverType
import com.ivi.navigation.domain.model.POI
import com.ivi.navigation.domain.model.Route
import com.ivi.navigation.domain.repository.NavigationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationRepositoryImpl @Inject constructor() : NavigationRepository {

    private val currentLocation = MutableStateFlow(LatLng(37.7749, -122.4194)) // San Francisco
    private val activeRoute = MutableStateFlow<Route?>(
        Route(
            id = "route_1",
            destinationName = "Grand Avenue Center",
            distanceRemainingKm = 14.2f,
            etaMinutes = 18,
            currentManeuver = Maneuver(
                instruction = "In 400 m, turn left onto Grand Avenue",
                distanceMeters = 400,
                maneuverType = ManeuverType.TURN_LEFT
            ),
            isNavigating = true
        )
    )

    private val samplePOIs = listOf(
        POI("poi_1", "Tesla Supercharger", "450 10th St", 2.1f, "Charging", LatLng(37.771, -122.412)),
        POI("poi_2", "Grand Avenue Center", "1200 Grand Ave", 14.2f, "Shopping", LatLng(37.801, -122.435)),
        POI("poi_3", "Shell EV Rapid Station", "789 Mission St", 3.8f, "Energy", LatLng(37.784, -122.404)),
        POI("poi_4", "Blue Bottle Coffee", "315 Linden St", 1.4f, "Cafe", LatLng(37.776, -122.423)),
    )

    override fun observeCurrentLocation(): Flow<LatLng> = currentLocation.asStateFlow()

    override fun observeActiveRoute(): Flow<Route?> = activeRoute.asStateFlow()

    override fun searchPOIs(query: String): Flow<List<POI>> {
        return MutableStateFlow(samplePOIs).map { pois ->
            if (query.isBlank()) pois
            else pois.filter { it.name.contains(query, ignoreCase = true) || it.category.contains(query, ignoreCase = true) }
        }
    }

    override suspend fun startNavigation(poi: POI) {
        activeRoute.value = Route(
            id = poi.id,
            destinationName = poi.name,
            distanceRemainingKm = poi.distanceKm,
            etaMinutes = (poi.distanceKm * 1.5).toInt().coerceAtLeast(3),
            currentManeuver = Maneuver("Proceed onto highway route", 500, ManeuverType.STRAIGHT),
            isNavigating = true
        )
    }

    override suspend fun stopNavigation() {
        activeRoute.value = null
    }
}
