package org.example.project.presentation.mainScreen

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.immutableListOf
import org.example.project.presentation.mockDatabase.User

data class MainUiState(
    val listUser: ImmutableList<User> = immutableListOf()
)