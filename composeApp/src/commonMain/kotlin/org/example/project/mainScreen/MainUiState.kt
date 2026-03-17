package org.example.project.mainScreen

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.immutableListOf
import org.example.project.mockDatabase.User

data class MainUiState(
    val listUser: ImmutableList<User> = immutableListOf()
)