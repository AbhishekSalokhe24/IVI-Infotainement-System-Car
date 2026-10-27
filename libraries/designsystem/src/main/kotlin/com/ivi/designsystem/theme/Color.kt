package com.ivi.designsystem.theme

import androidx.compose.ui.graphics.Color

/**
 * IVI Color Palette
 * Designed for automotive displays with high contrast, night-vision preservation, and low distraction.
 */
object IVIColors {

    // === Primary Brand Colors ===
    val Primary = Color(0xFF3B82F6)          // Vibrant Blue
    val PrimaryVariant = Color(0xFF2563EB)   // Deeper Blue
    val PrimaryLight = Color(0xFF60A5FA)     // Light Blue
    val PrimaryContainer = Color(0xFF1E3A5F) // Muted container

    // === Secondary / Accent ===
    val Secondary = Color(0xFF06B6D4)        // Cyan accent
    val SecondaryVariant = Color(0xFF0891B2)
    val SecondaryContainer = Color(0xFF164E63)

    // === Tertiary ===
    val Tertiary = Color(0xFF8B5CF6)         // Purple accent
    val TertiaryContainer = Color(0xFF3B1F6E)

    // === Surfaces (Dark Theme - Primary for Automotive) ===
    val Surface = Color(0xFF0F172A)          // Deep navy background
    val SurfaceVariant = Color(0xFF1E293B)   // Slightly lighter surface
    val SurfaceElevated = Color(0xFF334155)  // Cards, elevated surfaces
    val SurfaceBright = Color(0xFF475569)    // Interactive surfaces

    // === Background ===
    val Background = Color(0xFF020617)       // Near-black background
    val BackgroundGradientStart = Color(0xFF0F172A)
    val BackgroundGradientEnd = Color(0xFF020617)

    // === On Colors (Text/Icons on surfaces) ===
    val OnPrimary = Color(0xFFFFFFFF)
    val OnSecondary = Color(0xFFFFFFFF)
    val OnSurface = Color(0xFFF1F5F9)        // Primary text
    val OnSurfaceVariant = Color(0xFF94A3B8)  // Secondary text
    val OnSurfaceDim = Color(0xFF64748B)      // Tertiary/disabled text
    val OnBackground = Color(0xFFF8FAFC)

    // === Semantic Colors ===
    val Success = Color(0xFF22C55E)           // Green - OK, connected
    val Warning = Color(0xFFF59E0B)           // Amber - caution
    val Error = Color(0xFFEF4444)             // Red - error, danger
    val Info = Color(0xFF3B82F6)              // Blue - informational

    val SuccessContainer = Color(0xFF14532D)
    val WarningContainer = Color(0xFF713F12)
    val ErrorContainer = Color(0xFF7F1D1D)

    // === Climate-Specific Colors ===
    val ClimateCool = Color(0xFF38BDF8)       // Cooling blue
    val ClimateWarm = Color(0xFFFB923C)       // Heating orange
    val ClimateOff = Color(0xFF475569)        // Inactive gray

    // === Overlay / Scrim ===
    val Scrim = Color(0xCC000000)             // 80% black overlay
    val ScrimLight = Color(0x66000000)        // 40% black overlay

    // === Day Mode Overrides ===
    object Day {
        val Surface = Color(0xFFF8FAFC)
        val SurfaceVariant = Color(0xFFE2E8F0)
        val SurfaceElevated = Color(0xFFFFFFFF)
        val Background = Color(0xFFEFF6FF)
        val OnSurface = Color(0xFF0F172A)
        val OnSurfaceVariant = Color(0xFF475569)
    }
}
