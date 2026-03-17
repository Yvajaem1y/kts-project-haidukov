package org.example.project.presentation.authorizationScreen

sealed class LoginUiEvent {
    object LoginSuccessEvent : LoginUiEvent()
}
