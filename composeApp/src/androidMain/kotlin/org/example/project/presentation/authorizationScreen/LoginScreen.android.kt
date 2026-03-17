package org.example.project.presentation.authorizationScreen

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import org.koin.compose.viewmodel.koinViewModel

@Composable
actual fun LoginScreen(
    navigateToMain: () -> Unit
) {
    val viewModel: AndroidAuthorizationViewModel = koinViewModel()
    val curUiState by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(curUiState.errorText) {
        val errorText = curUiState.errorText
        if (errorText != null) {
            snackbarHostState.showSnackbar(curUiState.errorText!!)
        }

    }

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { value -> if (value is LoginUiEvent.LoginSuccessEvent) navigateToMain() }
    }

    val authLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        result.data?.let { intent ->
            handleAuthResponse(intent, viewModel)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.openAuthPageFlow.collect { intent ->
            authLauncher.launch(intent)
        }
    }

    AuthScreen(
        isLoading = curUiState.isLoading,
        onLoginClick = { viewModel.openLoginPage() },
        snackbarHostState = snackbarHostState
    )
}

private fun handleAuthResponse(
    intent: Intent,
    viewModel: AndroidAuthorizationViewModel
) {
    val exception = AuthorizationException.fromIntent(intent)
    val tokenExchangeRequest = AuthorizationResponse.fromIntent(intent)
        ?.createTokenExchangeRequest()

    when {
        exception != null -> viewModel.onAuthCodeFailed(exception)
        tokenExchangeRequest != null -> viewModel.onAuthCodeReceived(tokenExchangeRequest)
    }
}