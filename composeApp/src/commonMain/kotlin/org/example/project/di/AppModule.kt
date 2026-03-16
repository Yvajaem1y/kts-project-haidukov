package org.example.project.di


import org.example.project.data.GithubRepositoryImpl
import org.example.project.domain.repository.GithubRepository
import org.example.project.domain.useCases.GetInitialRepositoriesByQueryUseCase
import org.example.project.domain.useCases.SearchNewRepositoryUseCase
import org.example.project.presentation.repositoriesScreen.RepositoriesScreenViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

expect val platformModule: Module

val commonModule = module {
    single<GithubRepository> { GithubRepositoryImpl(get()) }

    single<GetInitialRepositoriesByQueryUseCase> {GetInitialRepositoriesByQueryUseCase(get())}
    single<SearchNewRepositoryUseCase> { SearchNewRepositoryUseCase(get())}

    viewModel<RepositoriesScreenViewModel>{ RepositoriesScreenViewModel(getInitialRepositoriesByQueryUseCase = get(), searchNewRepositoryUseCase = get()) }
}

val appModules = listOf(
    commonModule,
    platformModule
)