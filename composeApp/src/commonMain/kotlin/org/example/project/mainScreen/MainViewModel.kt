package org.example.project.mainScreen

import kotlinx.collections.immutable.toImmutableList
import org.example.project.BaseViewModel
import org.example.project.mockDatabase.MockDatabase

class MainViewModel(
    private val database: MockDatabase = MockDatabase
) : BaseViewModel<MainUiState>(MainUiState()) {

    init {
        updateState {
            copy(listUser = database.getList().toImmutableList())
        }
    }

}