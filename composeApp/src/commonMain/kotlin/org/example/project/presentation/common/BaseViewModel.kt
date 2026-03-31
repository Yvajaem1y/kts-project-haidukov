package org.example.project.presentation.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<State>(initialState: State) : ViewModel() {

    private val mutableState = MutableStateFlow(initialState)

    val state: StateFlow<State>
        get() = mutableState.asStateFlow()

    fun updateState(block: State.() -> State) {
        mutableState.update { it.block() }
    }
}