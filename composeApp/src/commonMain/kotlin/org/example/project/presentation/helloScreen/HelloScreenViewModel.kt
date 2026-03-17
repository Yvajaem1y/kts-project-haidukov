package org.example.project.presentation.helloScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import org.example.project.data.local_database.dataStore.DataStoreRepository

class HelloScreenViewModel(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    fun onboardingIsShow() {
        viewModelScope.launch {
            dataStoreRepository.onBoardingIsShown()
        }
    }
}