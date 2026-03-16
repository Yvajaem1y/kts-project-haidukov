package org.example.project.presentation.repositoriesScreen

import androidx.lifecycle.viewModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.project.data.remote_database.models.RequestResponse
import org.example.project.data.remote_database.models.mapToImmutableList
import org.example.project.domain.useCases.GetInitialRepositoriesByQueryUseCase
import org.example.project.domain.useCases.SearchNewRepositoryUseCase
import org.example.project.presentation.common.BaseViewModel
import org.example.project.presentation.common.RequestResponseUiState
import org.example.project.presentation.common.toUiState
import kotlin.math.ceil

class RepositoriesScreenViewModel(
    private val getInitialRepositoriesByQueryUseCase: GetInitialRepositoriesByQueryUseCase,
    private val searchNewRepositoryUseCase: SearchNewRepositoryUseCase
) : BaseViewModel<RepositoriesScreenUiState>(
    RepositoriesScreenUiState()
) {
    private var currentGetRepositoriesJob: Job? = null
    private var searchDebounceJob: Job? = null

    private companion object {
        private const val PER_PAGE_SIZE = 50
    }

    fun updateQueryText(query: String) {
        updateState { copy(query = query) }

        searchDebounceJob?.cancel()
        searchDebounceJob = viewModelScope.launch {
            delay(300)
            updateState { copy(page = 1) }
            if (query.isEmpty()) {
                updateState { copy(requestResultWithRepositories = RequestResponseUiState.None) }
                currentGetRepositoriesJob?.cancel()
            } else getInitialRepositories()
        }
    }

    fun searchNewRepositories() {
        currentGetRepositoriesJob?.cancel()
        currentGetRepositoriesJob = viewModelScope.launch {
            Napier.d("Starting search data for: ${state.value.query}", tag = "VIEWMODEL")

            searchNewRepositoryUseCase.invoke(
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

    fun getInitialRepositories() {
        currentGetRepositoriesJob?.cancel()
        currentGetRepositoriesJob = viewModelScope.launch {
            Napier.d("Starting search initial data for: ${state.value.query}", tag = "VIEWMODEL")

            getInitialRepositoriesByQueryUseCase.invoke(
                query = state.value.query,
                perPage = PER_PAGE_SIZE
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

                    if (requestResponse is RequestResponse.OnSuccess){
                        val countOfPage = ceil(requestResponse.data.size.toDouble() / PER_PAGE_SIZE).toInt()
                        if (countOfPage == 0 ) {
                            searchNewRepositories()
                            Napier.d(
                                "Start searching new repositories",
                                tag = "VIEWMODEL"
                            )
                        }
                        else {
                            updateState { copy(page = countOfPage) }
                        }
                    }

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