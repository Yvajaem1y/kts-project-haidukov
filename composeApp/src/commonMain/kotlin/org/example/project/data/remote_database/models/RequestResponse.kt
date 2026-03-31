package org.example.project.data.remote_database.models

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.example.project.presentation.common.RequestResponseUiState

sealed class RequestResponse<out T> {
    data class InProgress<T>(val item: T) : RequestResponse<T>()
    data class OnSuccess<T>(val item: T) : RequestResponse<T>()
    data class OnError<T>(val error: Throwable, val item: T) : RequestResponse<T>()
}

fun <T> RequestResponse<List<T>>.mapToImmutableList() : RequestResponse<ImmutableList<T>>{
    return when(this){
        is RequestResponse.InProgress -> RequestResponse.InProgress(item = (item as List<T>).toImmutableList())
        is RequestResponse.OnError -> RequestResponse.OnError(error = error, item = (item as List<T>).toImmutableList())
        is RequestResponse.OnSuccess<*> -> RequestResponse.OnSuccess(item = (item as List<T>).toImmutableList())
    }
}