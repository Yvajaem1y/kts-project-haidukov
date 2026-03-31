package org.example.project.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class RepositoryPreview(
    val repositoryName: String,
    val description: String,
    val language: String,
    val starsCount: Int,
    val owner: Owner,
    )

data class Owner(
    val login: String,
    val avatarUrl: String,
)