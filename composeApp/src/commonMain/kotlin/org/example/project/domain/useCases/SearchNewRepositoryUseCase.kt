package org.example.project.domain.useCases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.example.project.data.remote_database.models.RequestResponse
import org.example.project.data.remote_database.models.sortList
import org.example.project.domain.models.RepositoryPreview
import org.example.project.domain.repository.GithubRepository

class SearchNewRepositoryUseCase (
    private val githubRepository: GithubRepository
) {
    suspend fun invoke(
        query: String,
        perPage : Int,
        page : Int,
    ): Flow<RequestResponse<List<RepositoryPreview>>> = withContext(Dispatchers.Default) {
        githubRepository.searchNewRepositoriesByQuery(
            query = query,
            perPage = perPage,
            page = page
        ).map { requestResponse -> requestResponse.sortList() }
    }
}