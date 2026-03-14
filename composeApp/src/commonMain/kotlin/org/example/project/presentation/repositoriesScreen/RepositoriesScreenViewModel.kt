package org.example.project.presentation.repositoriesScreen

import androidx.lifecycle.viewModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.project.data.remote_database.models.RequestResponse
import org.example.project.data.remote_database.models.mapToImmutableList
import org.example.project.domain.repository.GithubRepository
import org.example.project.presentation.common.BaseViewModel
import org.example.project.presentation.common.RequestResponseUiState
import org.example.project.presentation.common.toUiState

class RepositoriesScreenViewModel(
    private val githubRepository: GithubRepository
) : BaseViewModel<RepositoriesScreenUiState>(
    RepositoriesScreenUiState()
) {
    companion object {
        private const val PER_PAGE_SIZE = 50
    }

    private var currentGetRepositoriesJob: Job? = null
    private var searchDebounceJob: Job? = null

    fun updateQueryText(query: String) {
        updateState { copy(query = query) }

        searchDebounceJob?.cancel()
        searchDebounceJob = viewModelScope.launch {
            delay(300)
            updateState { copy(page = 1) }
            if (query.isEmpty()) {
                updateState { copy(requestResultWithRepositories = RequestResponseUiState.None) }
                currentGetRepositoriesJob?.cancel()
            }
            else getRepositories()
        }
    }

    fun getRepositories() {
        currentGetRepositoriesJob?.cancel()
        currentGetRepositoriesJob = viewModelScope.launch {
            Napier.d("Starting search for: ${state.value.query}", tag = "VIEWMODEL")

            githubRepository.searchRepository(
                query = state.value.query,
                perPage = PER_PAGE_SIZE,
                page = state.value.page
            )
                .collect { requestResponse ->
                    Napier.d(
                        "Flow emitted: ${requestResponse::class.simpleName}",
                        tag = "VIEWMODEL"
                    )
                    if (requestResponse is RequestResponse.OnError) Napier.d(
                        "Error: ${requestResponse.error}",
                        tag = "VIEWMODEL"
                    )
                    if (requestResponse is RequestResponse.OnSuccess) updateState { copy(page = page + 1) }
                    updateState {
                        copy(
                            requestResultWithRepositories = requestResponse.mapToImmutableList()
                                .toUiState()
                        )
                    }
                }
        }
    }
}