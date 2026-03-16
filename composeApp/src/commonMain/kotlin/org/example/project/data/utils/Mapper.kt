package org.example.project.data.utils

import org.example.project.data.local_database.models.OwnerDBO
import org.example.project.data.local_database.models.RepositoryPreviewDBO
import org.example.project.data.remote_database.models.OwnerDTO
import org.example.project.data.remote_database.models.RepositoryPreviewDTO
import org.example.project.domain.models.Owner
import org.example.project.domain.models.RepositoryPreview

internal fun RepositoryPreviewDTO.toData(lastUpdated: Long) : RepositoryPreview{
    return RepositoryPreview(
        repositoryName = repositoryName,
        description = description ?: "",
        language = language ?: "",
        starsCount = starsCount,
        owner = owner.toOwner(),
        lastUpdated = lastUpdated,
        id = id
    )
}

private fun OwnerDTO.toOwner() : Owner{
    return Owner(
        login = login,
        avatarUrl = avatarUrl ?: ""
    )
}

internal fun RepositoryPreviewDBO.toData() : RepositoryPreview{
    return RepositoryPreview(
        repositoryName = repositoryName,
        description = description ?: "",
        language = language ?: "",
        starsCount = starsCount,
        owner = owner.toOwner(),
        lastUpdated = lastUpdated,
        id = id
    )
}

private fun OwnerDBO.toOwner() : Owner{
    return Owner(
        login = login,
        avatarUrl = avatarUrl ?: ""
    )
}


internal fun RepositoryPreviewDTO.toDBO(lastUpdated: Long) : RepositoryPreviewDBO {
    return RepositoryPreviewDBO(
        id = id,
        repositoryName = repositoryName,
        description = description ?: "",
        language = language ?: "",
        starsCount = starsCount,
        owner = owner.toDBO(),
        lastUpdated = lastUpdated,
    )
}

private fun OwnerDTO.toDBO() : OwnerDBO{
    return OwnerDBO(
        login = login,
        avatarUrl = avatarUrl ?: ""
    )
}