package org.example.project.presentation.loginScreen

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.example.project.di.MockDi
import org.example.project.domain.models.AuthState
import org.example.project.domain.useCases.CheckAuthStateUseCase
import org.example.project.domain.useCases.LoginUseCase
import org.example.project.presentation.BaseViewModel

class LoginViewModel : BaseViewModel<LoginUiState>(LoginUiState()) {

    private val loginUseCase: LoginUseCase = MockDi.loginUseCase
    private val checkAuthStateUseCase: CheckAuthStateUseCase = MockDi.checkAuthStateUseCase

    private val _events = MutableSharedFlow<LoginUiEvent>()
    val events: SharedFlow<LoginUiEvent> = _events

    init {
        checkExistingSession()
    }

    private fun checkExistingSession() {
        viewModelScope.launch {
            val authState = checkAuthStateUseCase()
            if (authState is AuthState.Authorized) {
                _events.emit(LoginUiEvent.LoginSuccessEvent)
            }
        }
    }

    private val buttonIsActive: ButtonState
        get() = if (isFormValid()) ButtonState.IsActive else ButtonState.IsNotActive

    private fun isFormValid(): Boolean {
        val state = state.value
        return state.login.isNotBlank() &&
                state.password.length >= 6
    }

    fun onDomainChange(newDomain: String) {
        updateState {
            copy(
                domain = newDomain,
                loginButtonState = buttonIsActive,
                error = null
            )
        }
    }

    fun onLoginChange(newLogin: String) {
        updateState {
            copy(
                login = newLogin,
                loginButtonState = buttonIsActive,
                error = null
            )
        }
    }

    fun onPasswordChange(newPassword: String) {
        updateState {
            copy(
                password = newPassword,
                loginButtonState = buttonIsActive,
                error = null
            )
        }
    }

    fun onPasswordVisibilityChange() {
        updateState { copy(isPasswordVisible = !isPasswordVisible) }
    }

    fun onLoginButtonClick() {
        if (state.value.loginButtonState != ButtonState.IsActive) return

        val currentState = state.value
        updateState { copy(loginButtonState = ButtonState.IsInProgress, error = null) }

        viewModelScope.launch {
            loginUseCase(
                domain = currentState.domain,
                email = currentState.login,
                password = currentState.password
            ).collect { result ->
                when {
                    result.isSuccess -> {
                        result.onSuccess { user ->
                            updateState {
                                copy(
                                    loginButtonState = ButtonState.IsSuccess,
                                    user = user
                                )
                            }
                            delay(500)
                            _events.emit(LoginUiEvent.LoginSuccessEvent)
                        }
                    }

                    result.isFailure -> {
                        result.onFailure { error ->
                            updateState {
                                copy(
                                    loginButtonState = ButtonState.IsError,
                                    error = error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}