package org.example.project.data.utils

import org.example.project.data.remote_database.models.RequestResponse
import org.example.project.data.remote_database.models.RequestResponse.InProgress
import org.example.project.data.remote_database.models.RequestResponse.OnSuccess
import org.example.project.data.remote_database.models.RequestResponse.OnError

public interface MergeStrategy<E> {

    public fun merge(
        right: E,
        left: E
    ): E
}

internal class RequestResponseMergeStrategy<T : Any> : MergeStrategy<RequestResponse<T>> {
    @Suppress("CyclomaticComplexMethod")
    override fun merge(
        right: RequestResponse<T>,
        left: RequestResponse<T>,
    ): RequestResponse<T> {
        return when {
            right is InProgress && left is InProgress -> merge(right, left)
            right is OnSuccess && left is InProgress -> merge(right, left)
            right is InProgress && left is OnSuccess -> merge(right, left)
            right is OnSuccess && left is OnSuccess -> merge(right, left)
            right is OnSuccess && left is Error -> merge(right, left)
            right is InProgress && left is Error -> merge(right, left)
            right is Error && left is InProgress -> merge(right, left)
            right is Error && left is OnSuccess -> merge(right, left)

            else -> error("Unimplemented branch right=$right & left=$left")
        }
    }

    private fun merge(
        cache: InProgress<T>,
        server: InProgress<T>
    ): RequestResponse<T> {
        return InProgress(cache.data)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        cache: OnSuccess<T>,
        server: InProgress<T>
    ): RequestResponse<T> {
        return InProgress(cache.data)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        cache: InProgress<T>,
        server: OnSuccess<T>
    ): RequestResponse<T> {
        return InProgress(server.data)
    }

    private fun merge(
        cache: OnSuccess<T>,
        server: OnError<T>
    ): RequestResponse<T> {
        return OnError(data = cache.data, error = server.error)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        cache: OnSuccess<T>,
        server: OnSuccess<T>
    ): RequestResponse<T> {
        return OnSuccess(data = server.data)
    }

    private fun merge(
        cache: InProgress<T>,
        server: OnError<T>
    ): RequestResponse<T> {
        return OnError(data = server.data ?: cache.data, error = server.error)
    }

    private fun merge(
        cache: OnError<T>,
        server: InProgress<T>
    ): RequestResponse<T> {
        return server
    }

    private fun merge(
        cache: OnError<T>,
        server: OnSuccess<T>
    ): RequestResponse<T> {
        return server
    }
}