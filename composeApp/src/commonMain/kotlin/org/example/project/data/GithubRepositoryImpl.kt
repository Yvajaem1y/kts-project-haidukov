package org.example.project.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.example.project.data.remote_database.GithubApi
import org.example.project.data.remote_database.models.RequestResponse
import org.example.project.data.utils.toData
import org.example.project.domain.models.RepositoryPreview
import org.example.project.domain.repository.GithubRepository

class GithubRepositoryImpl : GithubRepository {
    private val githubApi = GithubApi()

    override suspend fun searchRepository(
        query: String,
        perPage: Int,
        page: Int
    ): Flow<RequestResponse<List<RepositoryPreview>>> {
        return flow {
            emit(RequestResponse.InProgress(listOf()))
            try {
                val response =
                    githubApi.searchRepository(query = query, perPage = perPage, page = page)
                val repositories: List<RepositoryPreview> =
                    (if (response.items == null) listOf<RepositoryPreview>() else response.items.map { previewDTO -> previewDTO.toData() })
                emit(RequestResponse.OnSuccess(repositories))
            } catch (e: Exception) {
                emit(RequestResponse.OnError(error = e, item = listOf()))
            }
        }
    }
}