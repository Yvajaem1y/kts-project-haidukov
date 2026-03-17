package org.example.project.presentation.appNavigate

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.example.project.data.local_database.dataStore.DataStoreRepository
import org.example.project.presentation.common.BaseViewModel

class AppNavigateViewModel(
    private val dataStoreRepository: DataStoreRepository
) : BaseViewModel<AppNavigateUiState>(AppNavigateUiState()) {

    init {
        viewModelScope.launch {
            dataStoreRepository.getIsFirstTimeInApp()
                .collect { value -> updateState { copy(isFirstTimeInApp = value) } }
            dataStoreRepository.getIsLoggedIn()
                .collect { value -> updateState { copy(isLoggedIn = value) } }
        }
    }
}