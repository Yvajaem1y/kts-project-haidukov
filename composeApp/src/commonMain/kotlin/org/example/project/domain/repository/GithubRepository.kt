package org.example.project.domain.repository

import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import org.example.project.data.remote_database.models.RepositoryPreviewDTO
import org.example.project.data.remote_database.models.RequestResponse
import org.example.project.data.remote_database.models.ServerItemsWrapper
import org.example.project.domain.models.RepositoryPreview

interface GithubRepository {
    suspend fun getInitialRepositoriesDataByQuery(query: String, perPage : Int) : Flow<RequestResponse<List<RepositoryPreview>>>
    suspend fun searchNewRepositoriesByQuery(query: String, perPage : Int, page : Int) : Flow<RequestResponse<List<RepositoryPreview>>>
}
