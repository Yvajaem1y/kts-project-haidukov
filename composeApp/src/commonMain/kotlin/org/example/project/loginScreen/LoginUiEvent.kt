package org.example.project.loginScreen

sealed class LoginUiEvent {
    object LoginSuccessEvent : LoginUiEvent()
}