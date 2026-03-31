package org.example.project.presentation.repositoriesScreen

import kotlinx.collections.immutable.ImmutableList
import org.example.project.domain.models.RepositoryPreview
import org.example.project.presentation.common.RequestResponseUiState

data class RepositoriesScreenUiState (
    val requestResultWithRepositories : RequestResponseUiState<ImmutableList<RepositoryPreview>> = RequestResponseUiState.None,
    val query : String = "",
    val page : Int = 1,
)