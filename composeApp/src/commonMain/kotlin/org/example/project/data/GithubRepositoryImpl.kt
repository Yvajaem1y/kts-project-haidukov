package org.example.project.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import org.example.project.data.local_database.AppDatabase
import org.example.project.data.local_database.roomDAO.RoomDAO
import org.example.project.data.remote_database.GithubApi
import org.example.project.data.remote_database.models.RequestResponse
import org.example.project.data.utils.MergeStrategy
import org.example.project.data.utils.RequestResponseMergeStrategy
import org.example.project.data.utils.currentTimeMillis
import org.example.project.data.utils.toDBO
import org.example.project.data.utils.toData
import org.example.project.domain.models.RepositoryPreview
import org.example.project.domain.repository.GithubRepository
import kotlin.collections.listOf
import kotlin.math.ceil

class GithubRepositoryImpl(
    private val database: AppDatabase
) : GithubRepository {
    private val githubApi = GithubApi()

    private val roomDAO: RoomDAO by lazy {
        database.getDao()
    }

    private val mergeRequestResponseStrategy: MergeStrategy<RequestResponse<List<RepositoryPreview>>> =
        RequestResponseMergeStrategy()

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getInitialRepositoriesDataByQuery(
        query: String,
        perPage: Int,
    ): Flow<RequestResponse<List<RepositoryPreview>>> {
        val localData = getInitialLocalRepositoriesPreview(query = query)

        val remoteData =
            getInitialRemoteRepositoriesPreview(query = query, perPage = perPage)

        return localData.combine(remoteData, mergeRequestResponseStrategy::merge)
            .flatMapLatest { result ->
                when (result) {
                    is RequestResponse.InProgress<*> -> {
                        roomDAO.searchRepositoriesByName(query)
                            .map { dBOS -> dBOS.map { previewDBO -> previewDBO.toData() } }
                            .map { RequestResponse.OnSuccess(it) }
                    }

                    is RequestResponse.OnError<*> -> {
                        flowOf(result)
                    }

                    is RequestResponse.OnSuccess<*> -> {
                        roomDAO.searchRepositoriesByName(query)
                            .map { dBOS -> dBOS.map { previewDBO -> previewDBO.toData() } }
                            .map { RequestResponse.OnSuccess(it) }
                    }
                }
            }
    }

    override suspend fun searchNewRepositoriesByQuery(
        query: String,
        perPage: Int,
        page: Int
    ): Flow<RequestResponse<List<RepositoryPreview>>> {
        val localData = funGetLocalRepositoryPreview(query = query)

        val remoteData =
            getRemoteRepositoriesPreviewByQuery(query = query, page = page, perPage = perPage)

        return localData.combine(remoteData, mergeRequestResponseStrategy::merge)
            .flatMapLatest { result ->
                if (result is RequestResponse.OnSuccess) {
                    roomDAO.searchRepositoriesByName(query)
                        .map { dBOS -> dBOS.map { previewDBO -> previewDBO.toData() } }
                        .map { RequestResponse.OnSuccess(it) }
                } else {
                    flowOf(result)
                }
            }
    }

    private fun funGetLocalRepositoryPreview(query: String): Flow<RequestResponse<List<RepositoryPreview>>> =
        flow {
            try {
                val res = roomDAO.searchRepositoriesByNameOneTime(query)
                emit(RequestResponse.OnSuccess(res.map { previewDBO -> previewDBO.toData() }))
            } catch (e: Exception) {
                emit(RequestResponse.OnError(error = e, data = listOf()))
            }
        }

    private fun getInitialLocalRepositoriesPreview(
        query: String
    ): Flow<RequestResponse<List<RepositoryPreview>>> {
        val initialData: Flow<RequestResponse<List<RepositoryPreview>>> =
            flow { emit(RequestResponse.InProgress(data = listOf())) }

        val dataFromLocal: Flow<RequestResponse<List<RepositoryPreview>>> = flow {
            try {
                val res = roomDAO.searchRepositoriesByNameOneTime(query)
                emit(RequestResponse.OnSuccess(res.map { previewDBO -> previewDBO.toData() }))
            } catch (e: Exception) {
                emit(RequestResponse.OnError(error = e, data = listOf()))
            }
        }

        return merge(initialData, dataFromLocal)
    }

    private fun getRemoteRepositoriesPreviewByQuery(
        query: String,
        perPage: Int,
        page: Int
    ): Flow<RequestResponse<List<RepositoryPreview>>> {
        val initialData: Flow<RequestResponse<List<RepositoryPreview>>> =
            flow { emit(RequestResponse.InProgress(data = listOf())) }

        val remoteData: Flow<RequestResponse<List<RepositoryPreview>>> = flow {
            try {
                val currentTimesMillis = currentTimeMillis()

                val res =
                    githubApi.searchRepository(query = query, perPage = perPage, page = page).items
                        ?: listOf()

                roomDAO.invokeRepositoryPreview(res.map { previewDTO ->
                    previewDTO.toDBO(
                        currentTimesMillis
                    )
                })

                emit(RequestResponse.OnSuccess(data = res.map { previewDTO ->
                    previewDTO.toData(
                        currentTimesMillis
                    )
                }))

            } catch (e: Exception) {
                emit(RequestResponse.OnError(error = e, data = listOf()))
            }
        }

        return merge(initialData, remoteData)
    }

    private fun getInitialRemoteRepositoriesPreview(
        query: String,
        perPage: Int,
    ): Flow<RequestResponse<List<RepositoryPreview>>> {
        val initialData: Flow<RequestResponse<List<RepositoryPreview>>> =
            flow { emit(RequestResponse.InProgress(data = listOf())) }

        val remoteData: Flow<RequestResponse<List<RepositoryPreview>>> = flow {
            try {
                val currentTimesMillis = currentTimeMillis()
                val currentCountPagesInLocalDatabase =
                    ceil(roomDAO.searchRepositoriesByNameOneTime(query).size.toDouble() / perPage).toInt()

                for (curPage in 1..currentCountPagesInLocalDatabase) {
                    val res =
                        githubApi.searchRepository(
                            query = query,
                            perPage = perPage,
                            page = curPage
                        ).items
                            ?: listOf()

                    roomDAO.invokeRepositoryPreview(res.map { previewDTO ->
                        previewDTO.toDBO(
                            currentTimesMillis
                        )
                    })
                }

                emit(RequestResponse.OnSuccess(data = listOf()))

            } catch (e: Exception) {
                emit(RequestResponse.OnError(error = e, data = listOf()))
            }
        }

        return merge(initialData, remoteData)
    }
}