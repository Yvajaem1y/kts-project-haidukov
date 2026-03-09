package org.example.project.presentation.loginScreen

import org.example.project.domain.models.User

data class LoginUiState(
    val domain: String = "",
    val login: String = "",
    val password: String = "",
    val loginButtonState: ButtonState = ButtonState.IsNotActive,
    val isPasswordVisible: Boolean = false,
    val error: Throwable? = null,
    val user: User? = null
)

sealed class ButtonState {
    object IsNotActive : ButtonState()
    object IsActive : ButtonState()
    object IsInProgress : ButtonState()
    object IsError : ButtonState()
    object IsSuccess : ButtonState()
}

