package org.example.project.loginScreen

data class LoginUiState(
    val login: String = "",
    val password: String = "",
    val loginButtonState: ButtonState = ButtonState.IsNotActive,
    val isPasswordVisible: Boolean = false,
    val error: Throwable? = null
)

sealed class ButtonState {
    object IsNotActive : ButtonState()
    object IsActive : ButtonState()
    object IsInProgress : ButtonState()
    object IsError : ButtonState()
    object IsSuccess : ButtonState()
}

