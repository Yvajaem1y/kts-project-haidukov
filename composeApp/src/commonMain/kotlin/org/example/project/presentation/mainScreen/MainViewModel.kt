package org.example.project.presentation.mainScreen

import kotlinx.collections.immutable.toImmutableList
import org.example.project.presentation.BaseViewModel
import org.example.project.presentation.mockDatabase.MockDatabase

class MainViewModel(
    private val database: MockDatabase = MockDatabase
) : BaseViewModel<MainUiState>(MainUiState()) {

    init {
        updateState {
            copy(listUser = database.getList().toImmutableList())
        }
    }

}