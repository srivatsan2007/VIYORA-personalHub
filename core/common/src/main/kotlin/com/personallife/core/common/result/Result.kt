package com.personallife.core.common.result

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * A sealed hierarchy representing the state of an asynchronous operation or data stream.
 */
sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable, val message: String = exception.localizedMessage ?: "An unexpected error occurred") : Result<Nothing>
    data object Loading : Result<Nothing>
}

/**
 * Maps a Flow of T to a Flow of Result<T> with Loading and Error handling.
 */
fun <T> Flow<T>.asResult(): Flow<Result<T>> = this
    .map<T, Result<T>> { Result.Success(it) }
    .onStart { emit(Result.Loading) }
    .catch { emit(Result.Error(it)) }
