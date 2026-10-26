# 🎨 Phase 0B: Design System

> **Parent Plan:** [00_Master_Implementation_Plan.md](file:///c:/Users/abhis/AOSP/Projects/IVI%20Project/AI%20Plans/00_Master_Implementation_Plan.md)  
> **Module:** `libraries/designsystem`  
> **Goal:** Build a complete automotive-grade design system with Jetpack Compose

---

## 1. Design Principles for Automotive UI

### 1.1 Core Principles

| Principle | Guideline |
|-----------|-----------|
| **Glanceability** | Information must be understood in < 2 seconds |
| **Touch Target Size** | Minimum 76dp × 76dp (NHTSA guideline) |
| **Contrast Ratio** | ≥ 4.5:1 for text, ≥ 3:1 for large text |
| **Font Size** | Minimum 24sp for body text, 32sp+ for primary info |
| **Color Safety** | No saturated red (preserves night vision) |
| **Animation Duration** | Max 300ms for transitions, no distracting animations |
| **Interaction Depth** | Max 2 taps to reach any primary function |

### 1.2 Layout Grid

```
┌─────────────────────────────────────────────────────────────┐
│ Status Bar (48dp)                                           │
├─────────┬───────────────────────────────────────────────────┤
│         │                                                   │
│  Nav    │              Content Area                         │
│  Rail   │           (Scrollable Region)                     │
│ (96dp)  │                                                   │
│         │                                                   │
│  🏠     │    ┌─────────┐  ┌─────────┐  ┌─────────┐        │
│  🎵     │    │  Card   │  │  Card   │  │  Card   │        │
│  🗺️     │    │  200dp  │  │  200dp  │  │  200dp  │        │
│  ❄️     │    └─────────┘  └─────────┘  └─────────┘        │
│  📞     │                                                   │
│  ⚙️     │                                                   │
│         │                                                   │
├─────────┴───────────────────────────────────────────────────┤
│ Quick Controls Bar (80dp)  [Climate] [Volume] [Brightness]  │
└─────────────────────────────────────────────────────────────┘
```

---

## 2. Design Tokens

### 2.1 Color System

```kotlin
// libraries/designsystem/src/main/kotlin/com/ivi/designsystem/theme/Color.kt
package com.ivi.designsystem.theme

import androidx.compose.ui.graphics.Color

/**
 * IVI Color Palette
 * Designed for automotive displays with high contrast and low distraction.
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
    val SurfaceVariant = Color(0xFF1E293B)   // Slightly lighter
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
```

### 2.2 Typography

```kotlin
// libraries/designsystem/src/main/kotlin/com/ivi/designsystem/theme/Typography.kt
package com.ivi.designsystem.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Using Google Sans or Inter for automotive readability
val IVIFontFamily = FontFamily.Default // Replace with custom font

/**
 * Automotive-optimized typography scale.
 * All sizes exceed NHTSA minimum (24sp body).
 */
object IVITypography {

    // === Display (Speedometer, large values) ===
    val DisplayLarge = TextStyle(
        fontFamily = IVIFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 72.sp,
        lineHeight = 80.sp,
        letterSpacing = (-1).sp
    )
    val DisplayMedium = TextStyle(
        fontFamily = IVIFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 56.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.5).sp
    )
    val DisplaySmall = TextStyle(
        fontFamily = IVIFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 44.sp,
        lineHeight = 52.sp,
    )

    // === Headlines (Screen titles, section headers) ===
    val HeadlineLarge = TextStyle(
        fontFamily = IVIFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
    )
    val HeadlineMedium = TextStyle(
        fontFamily = IVIFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
    )
    val HeadlineSmall = TextStyle(
        fontFamily = IVIFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 28.sp,
        lineHeight = 36.sp,
    )

    // === Title (Card titles, list item primary text) ===
    val TitleLarge = TextStyle(
        fontFamily = IVIFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 26.sp,
        lineHeight = 34.sp,
    )
    val TitleMedium = TextStyle(
        fontFamily = IVIFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.15.sp
    )

    // === Body (Primary content text) ===
    val BodyLarge = TextStyle(
        fontFamily = IVIFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.5.sp
    )
    val BodyMedium = TextStyle(
        fontFamily = IVIFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.25.sp
    )

    // === Label (Buttons, chips, small UI elements) ===
    val LabelLarge = TextStyle(
        fontFamily = IVIFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.1.sp
    )
    val LabelMedium = TextStyle(
        fontFamily = IVIFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    val LabelSmall = TextStyle(
        fontFamily = IVIFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    )
}
```

### 2.3 Spacing & Dimensions

```kotlin
// libraries/designsystem/src/main/kotlin/com/ivi/designsystem/theme/Spacing.kt
package com.ivi.designsystem.theme

import androidx.compose.ui.unit.dp

/**
 * Spacing scale for consistent layout.
 * Automotive UIs use larger spacing for touch safety.
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
 * Standard component dimensions.
 */
object IVIDimensions {
    // Touch targets (NHTSA minimum: 76dp)
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
```

### 2.4 Shape System

```kotlin
// libraries/designsystem/src/main/kotlin/com/ivi/designsystem/theme/Shape.kt
package com.ivi.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

object IVIShapes {
    val None = RoundedCornerShape(0.dp)
    val ExtraSmall = RoundedCornerShape(4.dp)
    val Small = RoundedCornerShape(8.dp)
    val Medium = RoundedCornerShape(12.dp)
    val Large = RoundedCornerShape(16.dp)
    val ExtraLarge = RoundedCornerShape(24.dp)
    val Full = RoundedCornerShape(50)  // Fully rounded (pill shape)
}
```

---

## 3. Theme Implementation

### 3.1 IVITheme Composable

```kotlin
// libraries/designsystem/src/main/kotlin/com/ivi/designsystem/theme/IVITheme.kt
package com.ivi.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
 * Custom extended color properties not available in Material3.
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
    darkTheme: Boolean = true, // Automotive defaults to dark
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
```

---

## 4. Core Components

### 4.1 IVIButton

```kotlin
// libraries/designsystem/src/main/kotlin/com/ivi/designsystem/components/IVIButton.kt
package com.ivi.designsystem.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.ivi.designsystem.theme.IVIDimensions
import com.ivi.designsystem.theme.IVIShapes
import com.ivi.designsystem.theme.IVISpacing

enum class IVIButtonSize {
    Medium,  // 80dp height
    Large    // 96dp height
}

@Composable
fun IVIButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: IVIButtonSize = IVIButtonSize.Medium,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    val haptic = LocalHapticFeedback.current
    val height = when (size) {
        IVIButtonSize.Medium -> IVIDimensions.TouchTargetDefault
        IVIButtonSize.Large -> IVIDimensions.TouchTargetLarge
    }

    Button(
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            onClick()
        },
        modifier = modifier.height(height),
        enabled = enabled,
        shape = IVIShapes.Large,
        contentPadding = PaddingValues(
            horizontal = IVISpacing.LG,
            vertical = IVISpacing.MD
        )
    ) {
        if (leadingIcon != null) {
            leadingIcon()
            Spacer(Modifier.width(IVISpacing.SM))
        }
        Text(text)
    }
}
```

### 4.2 IVICard

```kotlin
// libraries/designsystem/src/main/kotlin/com/ivi/designsystem/components/IVICard.kt
package com.ivi.designsystem.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.ivi.designsystem.theme.*

@Composable
fun IVICard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = IVIShapes.Large
    val colors = CardDefaults.cardColors(
        containerColor = IVIColors.SurfaceVariant,
        contentColor = IVIColors.OnSurface,
    )

    if (onClick != null) {
        ElevatedCard(
            onClick = onClick,
            modifier = modifier
                .defaultMinSize(minHeight = IVIDimensions.CardMinHeight)
                .clip(shape),
            shape = shape,
            colors = colors,
            content = content,
        )
    } else {
        ElevatedCard(
            modifier = modifier
                .defaultMinSize(minHeight = IVIDimensions.CardMinHeight)
                .clip(shape),
            shape = shape,
            colors = colors,
            content = content,
        )
    }
}
```

### 4.3 IVISlider (for Climate/Volume)

```kotlin
// libraries/designsystem/src/main/kotlin/com/ivi/designsystem/components/IVISlider.kt
package com.ivi.designsystem.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.ivi.designsystem.theme.*

@Composable
fun IVISlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    label: String? = null,
    valueLabel: String? = null,
    enabled: Boolean = true,
) {
    val haptic = LocalHapticFeedback.current

    Column(modifier = modifier) {
        if (label != null || valueLabel != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (label != null) {
                    Text(
                        text = label,
                        style = IVITypography.LabelLarge,
                        color = IVIColors.OnSurface,
                    )
                }
                if (valueLabel != null) {
                    Text(
                        text = valueLabel,
                        style = IVITypography.TitleMedium,
                        color = IVIColors.Primary,
                    )
                }
            }
            Spacer(Modifier.height(IVISpacing.SM))
        }

        Slider(
            value = value,
            onValueChange = { newValue ->
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onValueChange(newValue)
            },
            valueRange = valueRange,
            steps = steps,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(IVIDimensions.TouchTargetMin),
            colors = SliderDefaults.colors(
                thumbColor = IVIColors.Primary,
                activeTrackColor = IVIColors.Primary,
                inactiveTrackColor = IVIColors.SurfaceBright,
            )
        )
    }
}
```

### 4.4 IVIIconButton (Navigation Rail)

```kotlin
// libraries/designsystem/src/main/kotlin/com/ivi/designsystem/components/IVIIconButton.kt
package com.ivi.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import com.ivi.designsystem.theme.*

@Composable
fun IVIIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    badge: String? = null,
    enabled: Boolean = true,
) {
    val backgroundColor = if (selected) {
        IVIColors.PrimaryContainer
    } else {
        IVIColors.Surface.copy(alpha = 0f)
    }
    val iconColor = if (selected) {
        IVIColors.Primary
    } else {
        IVIColors.OnSurfaceVariant
    }

    Box(
        modifier = modifier
            .size(IVIDimensions.TouchTargetDefault)
            .clip(IVIShapes.Large)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onClick,
            enabled = enabled,
        ) {
            if (badge != null) {
                BadgedBox(
                    badge = {
                        Badge { Text(badge) }
                    }
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = contentDescription,
                        tint = iconColor,
                        modifier = Modifier.size(IVIDimensions.IconMedium)
                    )
                }
            } else {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = iconColor,
                    modifier = Modifier.size(IVIDimensions.IconMedium)
                )
            }
        }
    }
}
```

---

## 5. Animation Specifications

```kotlin
// libraries/designsystem/src/main/kotlin/com/ivi/designsystem/animation/IVIAnimations.kt
package com.ivi.designsystem.animation

import androidx.compose.animation.*
import androidx.compose.animation.core.*

object IVIAnimations {
    // Standard transition duration
    const val DURATION_SHORT = 150
    const val DURATION_MEDIUM = 300
    const val DURATION_LONG = 500

    // Easing curves
    val EaseOut = CubicBezierEasing(0.0f, 0.0f, 0.2f, 1.0f)
    val EaseIn = CubicBezierEasing(0.4f, 0.0f, 1.0f, 1.0f)
    val EaseInOut = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)

    // Screen transitions
    val screenEnterTransition: EnterTransition =
        fadeIn(tween(DURATION_MEDIUM, easing = EaseOut)) +
        slideInHorizontally(tween(DURATION_MEDIUM, easing = EaseOut)) { it / 4 }

    val screenExitTransition: ExitTransition =
        fadeOut(tween(DURATION_SHORT, easing = EaseIn)) +
        slideOutHorizontally(tween(DURATION_SHORT, easing = EaseIn)) { -it / 4 }

    // Card press animation spec
    val cardPressScale = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )

    // Temperature value animation
    val temperatureAnimSpec = tween<Float>(
        durationMillis = DURATION_LONG,
        easing = EaseInOut
    )
}
```

---

## 6. UX Restriction Awareness

```kotlin
// libraries/designsystem/src/main/kotlin/com/ivi/designsystem/ux/UxRestrictionAware.kt
package com.ivi.designsystem.ux

import androidx.compose.runtime.*
import android.car.drivingstate.CarUxRestrictions

/**
 * Provides UX restriction state to composables.
 * When driving, certain UI elements must be simplified.
 */
val LocalUxRestrictions = compositionLocalOf {
    UxRestrictionState()
}

data class UxRestrictionState(
    val isFullyRestricted: Boolean = false,
    val noTextEntry: Boolean = false,
    val noFilteredList: Boolean = false,
    val limitStringLength: Boolean = false,
    val maxStringLength: Int = 120,
    val maxCumulativeContentItems: Int = 12,
)

/**
 * Modifier extension to hide content when driving restrictions are active.
 */
@Composable
fun RestrictedContent(
    restrictedContent: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    val restrictions = LocalUxRestrictions.current
    if (restrictions.isFullyRestricted) {
        restrictedContent()
    } else {
        content()
    }
}
```

---

## 7. Verification Checklist

- [ ] All design tokens defined (colors, typography, spacing, shapes)
- [ ] Dark and Light color schemes implemented
- [ ] IVITheme composable wraps Material3 with extensions
- [ ] All core components meet 76dp minimum touch target
- [ ] Typography scale uses ≥ 24sp for body text
- [ ] Animation specs defined and under 300ms
- [ ] UX restriction awareness integrated
- [ ] Component preview functions created for all components
- [ ] Accessibility: content descriptions on all interactive elements
- [ ] Theme preview gallery screen for visual QA

---

*Next: [03_Launcher_and_System_Shell.md](file:///c:/Users/abhis/AOSP/Projects/IVI%20Project/AI%20Plans/03_Launcher_and_System_Shell.md) — Home screen, status bar, navigation rail, and app drawer*
