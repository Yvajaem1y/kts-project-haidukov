package org.example.project.presentation.loginScreen

sealed class LoginUiEvent {
    object LoginSuccessEvent : LoginUiEvent()
}
