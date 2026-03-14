package org.example.project.presentation.loginScreen

import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest
import org.example.project.oAuth.models.AuthRepository
import org.example.project.presentation.common.BaseViewModel

class AndroidLoginViewModel(
    private val authService: AuthorizationService
) : BaseViewModel<LoginUiState>(LoginUiState()) {

    private val authRepository = AuthRepository()

    private val _openAuthPageFlow = MutableSharedFlow<Intent>()
    val openAuthPageFlow: SharedFlow<Intent> = _openAuthPageFlow.asSharedFlow()

    private val _events = MutableSharedFlow<LoginUiEvent>()
    val events: SharedFlow<LoginUiEvent>
        get() = _events

    fun onAuthCodeFailed(exception: AuthorizationException) {
        updateState { copy(errorText = exception.message) }
    }

    fun onAuthCodeReceived(tokenRequest: TokenRequest) {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            runCatching {
                authRepository.performTokenRequest(
                    authService = authService,
                    tokenRequest = tokenRequest
                )
            }.onSuccess {
                _events.emit(LoginUiEvent.LoginSuccessEvent)
            }.onFailure { exception ->
                updateState { copy(errorText = "Token exchange failed: ${exception.message}") }
            }
            updateState { copy(isLoading = false) }
        }
    }

    fun openLoginPage() {
        viewModelScope.launch {
            try {
                val authRequest = authRepository.getAuthRequest()

                val openAuthPageIntent = authService.getAuthorizationRequestIntent(
                    authRequest,
                    CustomTabsIntent.Builder().build()
                )

                _openAuthPageFlow.emit(openAuthPageIntent)

            } catch (e: Exception) {
                updateState {
                    copy(errorText = "Failed to open login page: ${e.message}")
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        authService.dispose()
    }
}