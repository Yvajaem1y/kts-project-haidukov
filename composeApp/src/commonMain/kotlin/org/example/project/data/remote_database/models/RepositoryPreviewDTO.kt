package org.example.project.data.remote_database.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchRepositoriesResponseDTO(
    @SerialName("items") val items: List<RepositoryPreviewDTO>?
)

@Serializable
data class RepositoryPreviewDTO(
    @SerialName("id") val id : Long,
    @SerialName("name") val repositoryName: String,
    @SerialName("description") val description: String? = null ,
    @SerialName("language") val language: String? = null,
    @SerialName("stargazers_count") val starsCount: Int,
    @SerialName("owner") val owner: OwnerDTO,

    )

@Serializable
data class OwnerDTO(
    @SerialName("login") val login: String,
    @SerialName("avatar_url") val avatarUrl: String? = null,
)