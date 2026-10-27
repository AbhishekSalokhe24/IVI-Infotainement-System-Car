package com.ivi.navigation.domain.repository

import com.ivi.navigation.domain.model.LatLng
import com.ivi.navigation.domain.model.POI
import com.ivi.navigation.domain.model.Route
import kotlinx.coroutines.flow.Flow

interface NavigationRepository {
    fun observeCurrentLocation(): Flow<LatLng>
    fun observeActiveRoute(): Flow<Route?>
    fun searchPOIs(query: String): Flow<List<POI>>
    suspend fun startNavigation(poi: POI)
    suspend fun stopNavigation()
}
