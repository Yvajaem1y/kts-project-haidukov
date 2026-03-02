package org.example.project.loginScreen

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.example.project.BaseViewModel

class LoginViewModel() : BaseViewModel<LoginUiState>(LoginUiState()) {

    private val _events = MutableSharedFlow<LoginUiEvent>()
    val events: SharedFlow<LoginUiEvent>
        get() = _events

    private val buttonIsActive: ButtonState
        get() = if (state.value.login.isNotEmpty() && state.value.password.length > 5) ButtonState.IsActive else ButtonState.IsNotActive

    fun onPasswordChange(newPassword: String) {
        updateState {
            copy(
                password = newPassword,
                loginButtonState = buttonIsActive
            )
        }
    }

    fun onLoginChange(newLogin: String) {
        updateState {
            copy(
                login = newLogin,
                loginButtonState = buttonIsActive
            )
        }
    }

    fun onPasswordVisibilityChange() {
        updateState { copy(isPasswordVisible = !isPasswordVisible) }
    }

    fun onLoginButtonClick() {
        if (state.value.loginButtonState is ButtonState.IsActive) {
            updateState { copy(loginButtonState = ButtonState.IsInProgress, error = null) }
            viewModelScope.launch {
                delay(1000L)

                if (state.value.login != "Pomidor") {
                    updateState { copy(loginButtonState = ButtonState.IsSuccess) }
                    _events.emit(LoginUiEvent.LoginSuccessEvent)
                } else {
                    updateState { copy(loginButtonState = ButtonState.IsError, error = IllegalStateException("Don't use the login \"Pomidor\"")) }
                }

            }
        }
    }
}