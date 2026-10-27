package com.ivi.launcher.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class IVIDestination(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    HOME(
        route = "home",
        label = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    MEDIA(
        route = "media",
        label = "Media",
        selectedIcon = Icons.Filled.MusicNote,
        unselectedIcon = Icons.Outlined.MusicNote,
    ),
    NAVIGATION(
        route = "navigation",
        label = "Maps",
        selectedIcon = Icons.Filled.Map,
        unselectedIcon = Icons.Outlined.Map,
    ),
    CLIMATE(
        route = "climate",
        label = "Climate",
        selectedIcon = Icons.Filled.AcUnit,
        unselectedIcon = Icons.Outlined.AcUnit,
    ),
    VEHICLE(
        route = "vehicle",
        label = "Vehicle",
        selectedIcon = Icons.Filled.DirectionsCar,
        unselectedIcon = Icons.Outlined.DirectionsCar,
    ),
    PHONE(
        route = "phone",
        label = "Phone",
        selectedIcon = Icons.Filled.Phone,
        unselectedIcon = Icons.Outlined.Phone,
    ),
    SETTINGS(
        route = "settings",
        label = "Settings",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
    ),
}
