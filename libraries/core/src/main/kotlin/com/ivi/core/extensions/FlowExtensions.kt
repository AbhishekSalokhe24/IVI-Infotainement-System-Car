package com.ivi.core.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

/**
 * Convenience extension to safely catch flow errors with logging/fallback.
 */
inline fun <T> Flow<T>.catchWithMessage(
    crossinline onError: (String, Throwable) -> Unit
): Flow<T> = catch { throwable ->
    onError(throwable.localizedMessage ?: "Unknown error occurred", throwable)
}
