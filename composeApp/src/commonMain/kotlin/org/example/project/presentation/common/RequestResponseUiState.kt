package org.example.project.presentation.common

import org.example.project.data.remote_database.models.RequestResponse

sealed class RequestResponseUiState<out T> {
    object None : RequestResponseUiState<Nothing>()
    class InProgress<T>(val item: T) : RequestResponseUiState<T>()
    data class OnSuccess<T>(val item: T) : RequestResponseUiState<T>()
    data class OnError<T>(val error: Throwable, val item: T) : RequestResponseUiState<T>()
}

internal fun <T> RequestResponse<T>.toUiState(): RequestResponseUiState<T> {
    return when(this){
        is RequestResponse.InProgress -> RequestResponseUiState.InProgress(item = item)
        is RequestResponse.OnError -> RequestResponseUiState.OnError(error = error, item = item)
        is RequestResponse.OnSuccess -> RequestResponseUiState.OnSuccess(item)
    }
}