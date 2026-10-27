package com.ivi.navigation.ui.map

import androidx.lifecycle.viewModelScope
import com.ivi.core.base.BaseViewModel
import com.ivi.core.base.UiState
import com.ivi.navigation.domain.model.LatLng
import com.ivi.navigation.domain.model.POI
import com.ivi.navigation.domain.model.Route
import com.ivi.navigation.domain.repository.NavigationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MapUiState(
    val currentLocation: LatLng = LatLng(37.7749, -122.4194),
    val activeRoute: Route? = null,
    val isNavigating: Boolean = false,
    val searchQuery: String = "",
    val searchResults: List<POI> = emptyList(),
    val zoomLevel: Float = 14f,
    val is3DMode: Boolean = true,
    val error: String? = null
) : UiState

sealed interface MapAction {
    data class SearchQueryChanged(val query: String) : MapAction
    data class SelectPOI(val poi: POI) : MapAction
    data object StopNavigation : MapAction
    data object ZoomIn : MapAction
    data object ZoomOut : MapAction
    data object Toggle3D : MapAction
    data object RecenterLocation : MapAction
}

sealed interface MapEvent

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: NavigationRepository
) : BaseViewModel<MapUiState, MapEvent, MapAction>(MapUiState()) {

    init {
        observeNavigation()
    }

    private fun observeNavigation() {
        viewModelScope.launch {
            repository.observeCurrentLocation().collect { location ->
                updateState { copy(currentLocation = location) }
            }
        }

        viewModelScope.launch {
            repository.observeActiveRoute().collect { route ->
                updateState {
                    copy(
                        activeRoute = route,
                        isNavigating = route != null && route.isNavigating
                    )
                }
            }
        }

        search("")
    }

    private fun search(query: String) {
        viewModelScope.launch {
            repository.searchPOIs(query)
                .catch { e -> updateState { copy(error = e.message) } }
                .collect { results ->
                    updateState { copy(searchResults = results) }
                }
        }
    }

    override fun onAction(action: MapAction) {
        viewModelScope.launch {
            when (action) {
                is MapAction.SearchQueryChanged -> {
                    updateState { copy(searchQuery = action.query) }
                    search(action.query)
                }
                is MapAction.SelectPOI -> {
                    repository.startNavigation(action.poi)
                }
                is MapAction.StopNavigation -> {
                    repository.stopNavigation()
                }
                is MapAction.ZoomIn -> {
                    updateState { copy(zoomLevel = (zoomLevel + 1f).coerceAtMost(20f)) }
                }
                is MapAction.ZoomOut -> {
                    updateState { copy(zoomLevel = (zoomLevel - 1f).coerceAtLeast(3f)) }
                }
                is MapAction.Toggle3D -> {
                    updateState { copy(is3DMode = !is3DMode) }
                }
                is MapAction.RecenterLocation -> {
                    // Recenter camera
                }
            }
        }
    }
}
