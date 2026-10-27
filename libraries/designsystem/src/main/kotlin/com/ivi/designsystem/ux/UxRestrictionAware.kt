package com.ivi.designsystem.ux

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

/**
 * Provides UX restriction state to composables.
 * When driving, certain UI elements must be simplified or hidden to prevent driver distraction.
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
 * Composable container that conditionally shows restricted fallback content when driving restrictions are active.
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
