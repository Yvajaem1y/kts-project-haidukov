package org.example.project.data.remote_database.models

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.example.project.domain.models.RepositoryPreview

sealed class RequestResponse<out T> (open val data : T)  {
    data class InProgress<T>(override val data: T) : RequestResponse<T>(data )
    data class OnSuccess<T>(override val data: T) : RequestResponse<T>(data)
    data class OnError<T>(val error: Throwable, override val data: T) : RequestResponse<T>(data)
}

fun <T> RequestResponse<List<T>>.mapToImmutableList() : RequestResponse<ImmutableList<T>>{
    return when(this){
        is RequestResponse.InProgress -> RequestResponse.InProgress(data = data.toImmutableList())
        is RequestResponse.OnError -> RequestResponse.OnError(error = error, data = data.toImmutableList())
        is RequestResponse.OnSuccess -> RequestResponse.OnSuccess(data = data.toImmutableList())
    }
}

fun RequestResponse<List<RepositoryPreview>>.sortList() : RequestResponse<List<RepositoryPreview>>{
    return when(this){
        is RequestResponse.InProgress -> RequestResponse.InProgress(data = (data).sortedBy { it.lastUpdated })
        is RequestResponse.OnError -> RequestResponse.OnError(error = error, data = (data).sortedBy { it.lastUpdated })
        is RequestResponse.OnSuccess -> RequestResponse.OnSuccess(data = (data).sortedBy { it.lastUpdated })
    }
}