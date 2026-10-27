package com.ivi.launcher.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ivi.climate.ui.ClimateControlScreen
import com.ivi.designsystem.animation.IVIAnimations
import com.ivi.launcher.ui.home.HomeScreen
import com.ivi.media.ui.MediaCenterScreen
import com.ivi.navigation.ui.NavigationScreen
import com.ivi.phone.ui.PhoneScreen
import com.ivi.settings.ui.SettingsScreen
import com.ivi.vehicle.ui.VehicleStatusScreen

@Composable
fun IVINavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = IVIDestination.HOME.route,
        enterTransition = { IVIAnimations.screenEnterTransition },
        exitTransition = { IVIAnimations.screenExitTransition },
        modifier = modifier
    ) {
        composable(IVIDestination.HOME.route) {
            HomeScreen(
                onNavigateToMedia = { navController.navigate(IVIDestination.MEDIA.route) },
                onNavigateToNavigation = { navController.navigate(IVIDestination.NAVIGATION.route) },
                onNavigateToClimate = { navController.navigate(IVIDestination.CLIMATE.route) },
                onNavigateToVehicle = { navController.navigate(IVIDestination.VEHICLE.route) },
                onNavigateToPhone = { navController.navigate(IVIDestination.PHONE.route) },
            )
        }

        composable(IVIDestination.MEDIA.route) {
            MediaCenterScreen()
        }

        composable(IVIDestination.NAVIGATION.route) {
            NavigationScreen()
        }

        composable(IVIDestination.CLIMATE.route) {
            ClimateControlScreen()
        }

        composable(IVIDestination.VEHICLE.route) {
            VehicleStatusScreen()
        }

        composable(IVIDestination.PHONE.route) {
            PhoneScreen()
        }

        composable(IVIDestination.SETTINGS.route) {
            SettingsScreen()
        }
    }
}
