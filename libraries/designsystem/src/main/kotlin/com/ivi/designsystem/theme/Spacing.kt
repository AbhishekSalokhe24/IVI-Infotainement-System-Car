package com.ivi.designsystem.theme

import androidx.compose.ui.unit.dp

/**
 * Spacing scale for consistent automotive layout.
 * Automotive UIs use larger spacing for glanceability and touch safety.
 */
object IVISpacing {
    val XXS = 4.dp
    val XS = 8.dp
    val SM = 12.dp
    val MD = 16.dp
    val LG = 24.dp
    val XL = 32.dp
    val XXL = 48.dp
    val XXXL = 64.dp
}

/**
 * Standard component dimensions compliant with automotive UX standards.
 */
object IVIDimensions {
    // Touch targets (NHTSA guideline minimum: 76dp)
    val TouchTargetMin = 76.dp
    val TouchTargetDefault = 80.dp
    val TouchTargetLarge = 96.dp

    // Status bar
    val StatusBarHeight = 48.dp

    // Navigation rail
    val NavRailWidth = 96.dp
    val NavRailIconSize = 32.dp

    // Quick controls bar
    val QuickControlsHeight = 80.dp

    // Cards
    val CardMinHeight = 120.dp
    val CardCornerRadius = 16.dp
    val CardElevation = 4.dp

    // Media
    val AlbumArtSize = 280.dp
    val AlbumArtCornerRadius = 12.dp

    // Sliders
    val SliderTrackHeight = 8.dp
    val SliderThumbSize = 32.dp

    // Icon sizes
    val IconSmall = 24.dp
    val IconMedium = 32.dp
    val IconLarge = 48.dp
    val IconXLarge = 64.dp
}
