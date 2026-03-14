package org.example.project.data.utils

import org.example.project.data.remote_database.models.OwnerDTO
import org.example.project.data.remote_database.models.RepositoryPreviewDTO
import org.example.project.domain.models.Owner
import org.example.project.domain.models.RepositoryPreview

internal fun RepositoryPreviewDTO.toData() : RepositoryPreview{
    return RepositoryPreview(
        repositoryName = repositoryName,
        description = description ?: "",
        language = language ?: "",
        starsCount = starsCount,
        owner = owner.toOwner()
    )
}

private fun OwnerDTO.toOwner() : Owner{
    return Owner(
        login = login,
        avatarUrl = avatarUrl ?: ""
    )
}