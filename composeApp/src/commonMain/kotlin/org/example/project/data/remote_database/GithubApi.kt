package org.example.project.data.remote_database

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.example.project.data.remote_database.models.SearchRepositoriesResponseDTO

class GithubApi {
    suspend fun searchRepository(query: String, perPage : Int, page : Int): SearchRepositoriesResponseDTO {
        return Networking.httpClient.get("search/repositories") {
            parameter("q", query)
            parameter("per_page",perPage)
            parameter("page",page)
        }.body<SearchRepositoriesResponseDTO>()
    }
}