package com.ivi.navigation.domain.model

data class LatLng(
    val latitude: Double,
    val longitude: Double
)

data class Maneuver(
    val instruction: String = "Turn left onto Grand Avenue",
    val distanceMeters: Int = 400,
    val maneuverType: ManeuverType = ManeuverType.TURN_LEFT
)

enum class ManeuverType {
    STRAIGHT,
    TURN_LEFT,
    TURN_RIGHT,
    ROUNDABOUT,
    DESTINATION
}

data class Route(
    val id: String,
    val destinationName: String,
    val distanceRemainingKm: Float,
    val etaMinutes: Int,
    val currentManeuver: Maneuver,
    val isNavigating: Boolean = true
)

data class POI(
    val id: String,
    val name: String,
    val address: String,
    val distanceKm: Float,
    val category: String,
    val coordinates: LatLng
)
