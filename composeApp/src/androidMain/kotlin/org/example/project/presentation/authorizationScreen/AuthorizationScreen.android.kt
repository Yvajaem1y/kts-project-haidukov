package org.example.project.presentation.authorizationScreen

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import io.github.aakira.napier.Napier
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import org.example.project.presentation.loadingScreen.LoadingScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
actual fun AuthorizationScreen(
    navigateToMain: () -> Unit
) {
    Napier.d("AuthorizationScreen: Composable started", tag = "Authorization")

    val viewModel: AndroidAuthorizationViewModel = koinViewModel()
    val curUiState by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val isLoginOpened = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!isLoginOpened.value) {
            Napier.d(
                "AuthorizationScreen: Calling viewModel.openLoginPage()",
                tag = "Authorization"
            )
            viewModel.openLoginPage()
            isLoginOpened.value = true
        }
    }

    LaunchedEffect(curUiState.errorText) {
        val errorText = curUiState.errorText
        if (errorText != null) {
            Napier.e("AuthorizationScreen: Error occurred - $errorText", tag = "Authorization")
            snackbarHostState.showSnackbar(errorText)
        }
    }

    val authLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Napier.d(
            "AuthorizationScreen: Auth activity result received - resultCode: ${result.resultCode}",
            tag = "Authorization"
        )
        result.data?.let { intent ->
            Napier.d("AuthorizationScreen: Processing auth response intent", tag = "Authorization")
            handleAuthResponse(intent, viewModel)
        } ?: run {
            Napier.w("AuthorizationScreen: Auth result data is null", tag = "Authorization")
        }
    }

    LaunchedEffect(Unit) {
        Napier.d("AuthorizationScreen: Collecting openAuthPageFlow", tag = "Authorization")
        viewModel.openAuthPageFlow.collect { intent ->
            Napier.d(
                "AuthorizationScreen: Received intent from flow: $intent",
                tag = "Authorization"
            )

            if (intent != null) {
                Napier.d(
                    "AuthorizationScreen: Launching auth activity with intent",
                    tag = "Authorization"
                )
                try {
                    authLauncher.launch(intent)
                    Napier.d(
                        "AuthorizationScreen: Auth activity launched successfully",
                        tag = "Authorization"
                    )
                } catch (e: Exception) {
                    Napier.e(
                        "AuthorizationScreen: Failed to launch auth activity",
                        tag = "Authorization",
                        throwable = e
                    )
                }
            } else {
                Napier.e(
                    "AuthorizationScreen: Intent is null, cannot launch auth activity",
                    tag = "Authorization"
                )
            }
        }
    }

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                is LoginUiEvent.LoginSuccessEvent -> {
                    Napier.d(
                        "AuthorizationScreen: Login success event received, navigating to main",
                        tag = "Authorization"
                    )
                    navigateToMain()
                }
            }
        }
    }

    LoadingScreen()
}

private fun handleAuthResponse(
    intent: Intent,
    viewModel: AndroidAuthorizationViewModel
) {
    Napier.d("handleAuthResponse: Processing auth response", tag = "Authorization")

    val exception = AuthorizationException.fromIntent(intent)
    val response = AuthorizationResponse.fromIntent(intent)
    val tokenExchangeRequest = response?.createTokenExchangeRequest()

    when {
        exception != null -> {
            viewModel.onAuthCodeFailed(exception)
        }

        tokenExchangeRequest != null -> {
            viewModel.onAuthCodeReceived(tokenExchangeRequest)
        }

        else -> {
            Napier.w(
                "handleAuthResponse: Neither exception nor token exchange request found in intent",
                tag = "Authorization"
            )
        }
    }
}