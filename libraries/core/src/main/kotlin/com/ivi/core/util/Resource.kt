package com.ivi.core.util

/**
 * Generic wrapper for async data loading states following Kotlin Sealed Interface patterns.
 */
sealed interface Resource<out T> {
    data object Loading : Resource<Nothing>
    data class Success<T>(val data: T) : Resource<T>
    data class Error(val message: String, val throwable: Throwable? = null) : Resource<Nothing>
}

fun <T> Resource<T>.getOrNull(): T? = (this as? Resource.Success)?.data

inline fun <T, R> Resource<T>.map(transform: (T) -> R): Resource<R> = when (this) {
    is Resource.Success -> Resource.Success(transform(data))
    is Resource.Error -> this
    is Resource.Loading -> this
}
