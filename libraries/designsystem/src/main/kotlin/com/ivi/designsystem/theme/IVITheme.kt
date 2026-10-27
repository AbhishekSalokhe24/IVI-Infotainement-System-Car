package com.ivi.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = IVIColors.Primary,
    onPrimary = IVIColors.OnPrimary,
    primaryContainer = IVIColors.PrimaryContainer,
    secondary = IVIColors.Secondary,
    onSecondary = IVIColors.OnSecondary,
    secondaryContainer = IVIColors.SecondaryContainer,
    tertiary = IVIColors.Tertiary,
    tertiaryContainer = IVIColors.TertiaryContainer,
    surface = IVIColors.Surface,
    onSurface = IVIColors.OnSurface,
    surfaceVariant = IVIColors.SurfaceVariant,
    onSurfaceVariant = IVIColors.OnSurfaceVariant,
    background = IVIColors.Background,
    onBackground = IVIColors.OnBackground,
    error = IVIColors.Error,
    errorContainer = IVIColors.ErrorContainer,
    scrim = IVIColors.Scrim,
)

private val LightColorScheme = lightColorScheme(
    primary = IVIColors.Primary,
    onPrimary = IVIColors.OnPrimary,
    primaryContainer = IVIColors.PrimaryLight,
    secondary = IVIColors.Secondary,
    surface = IVIColors.Day.Surface,
    onSurface = IVIColors.Day.OnSurface,
    surfaceVariant = IVIColors.Day.SurfaceVariant,
    onSurfaceVariant = IVIColors.Day.OnSurfaceVariant,
    background = IVIColors.Day.Background,
    onBackground = IVIColors.Day.OnSurface,
    error = IVIColors.Error,
)

/**
 * Custom extended automotive color properties.
 */
@Stable
data class IVIExtendedColors(
    val success: Color,
    val successContainer: Color,
    val warning: Color,
    val warningContainer: Color,
    val climateCool: Color,
    val climateWarm: Color,
    val climateOff: Color,
    val surfaceElevated: Color,
    val surfaceBright: Color,
    val onSurfaceDim: Color,
)

val LocalIVIExtendedColors = staticCompositionLocalOf {
    IVIExtendedColors(
        success = IVIColors.Success,
        successContainer = IVIColors.SuccessContainer,
        warning = IVIColors.Warning,
        warningContainer = IVIColors.WarningContainer,
        climateCool = IVIColors.ClimateCool,
        climateWarm = IVIColors.ClimateWarm,
        climateOff = IVIColors.ClimateOff,
        surfaceElevated = IVIColors.SurfaceElevated,
        surfaceBright = IVIColors.SurfaceBright,
        onSurfaceDim = IVIColors.OnSurfaceDim,
    )
}

@Composable
fun IVITheme(
    darkTheme: Boolean = true, // Automotive defaults to dark mode
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val extendedColors = IVIExtendedColors(
        success = IVIColors.Success,
        successContainer = IVIColors.SuccessContainer,
        warning = IVIColors.Warning,
        warningContainer = IVIColors.WarningContainer,
        climateCool = IVIColors.ClimateCool,
        climateWarm = IVIColors.ClimateWarm,
        climateOff = IVIColors.ClimateOff,
        surfaceElevated = IVIColors.SurfaceElevated,
        surfaceBright = IVIColors.SurfaceBright,
        onSurfaceDim = IVIColors.OnSurfaceDim,
    )

    CompositionLocalProvider(
        LocalIVIExtendedColors provides extendedColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}

/**
 * Access extended colors from any Composable.
 */
object IVITheme {
    val extendedColors: IVIExtendedColors
        @Composable
        @ReadOnlyComposable
        get() = LocalIVIExtendedColors.current
}
