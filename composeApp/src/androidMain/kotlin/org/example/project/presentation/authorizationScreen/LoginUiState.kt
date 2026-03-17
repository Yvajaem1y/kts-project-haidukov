package org.example.project.presentation.authorizationScreen

data class LoginUiState(
    val errorText: String? = null,
    val isLoading: Boolean = false,
    val isLoginPageOpened: Boolean = false
)