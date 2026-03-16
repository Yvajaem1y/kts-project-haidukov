package org.example.project.domain.useCases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.example.project.data.remote_database.models.RequestResponse
import org.example.project.data.remote_database.models.sortList
import org.example.project.domain.models.RepositoryPreview
import org.example.project.domain.repository.GithubRepository

class GetInitialRepositoriesByQueryUseCase(
    private val githubRepository: GithubRepository
) {
    suspend fun invoke(
        query: String,
        perPage : Int,
    ): Flow<RequestResponse<List<RepositoryPreview>>> = withContext(Dispatchers.Default) {
            githubRepository.getInitialRepositoriesDataByQuery(
                query = query,
                perPage = perPage,
            ).map { requestResponse -> requestResponse.sortList() }
        }
}