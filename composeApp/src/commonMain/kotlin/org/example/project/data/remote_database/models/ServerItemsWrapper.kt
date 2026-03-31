package org.example.project.data.remote_database.models

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable

@Serializable
data class ServerItemsWrapper<T>(
    val items: T
)

inline fun <T, R> ServerItemsWrapper<T>.map( mapper: (T) -> R ) : ServerItemsWrapper<R>{
    return ServerItemsWrapper(mapper(this.items))
}