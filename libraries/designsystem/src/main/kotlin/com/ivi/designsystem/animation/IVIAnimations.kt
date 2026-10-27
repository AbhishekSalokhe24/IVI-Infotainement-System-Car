package com.ivi.designsystem.animation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

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
