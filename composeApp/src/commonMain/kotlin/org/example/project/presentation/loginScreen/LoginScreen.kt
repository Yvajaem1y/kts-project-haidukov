package org.example.project.presentation.loginScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kts_project_haidukov.composeapp.generated.resources.Res
import kts_project_haidukov.composeapp.generated.resources.button_error
import kts_project_haidukov.composeapp.generated.resources.hide_password
import kts_project_haidukov.composeapp.generated.resources.ic_hide_password
import kts_project_haidukov.composeapp.generated.resources.ic_show_password
import kts_project_haidukov.composeapp.generated.resources.login_button
import kts_project_haidukov.composeapp.generated.resources.login_placeholder
import kts_project_haidukov.composeapp.generated.resources.password_placeholder
import kts_project_haidukov.composeapp.generated.resources.show_password
import kts_project_haidukov.composeapp.generated.resources.success_button
import kts_project_haidukov.composeapp.generated.resources.unknown_error
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun LoginScreen(
    navigateToMain: () -> Unit
) {
    val viewModel: LoginViewModel = viewModel()

    val uiState by viewModel.state.collectAsState()

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { value -> if (value is LoginUiEvent.LoginSuccessEvent) navigateToMain() }
    }

    val loginFormContent = remember {
        movableContentOf {
            LoginFieldsAndButton(
                loginText = uiState.login,
                onLoginChange = { viewModel.onLoginChange(it) },
                passwordText = uiState.password,
                onPasswordChange = { viewModel.onPasswordChange(it) },
                passwordVisible = uiState.isPasswordVisible,
                onPasswordVisibilityChange = { viewModel.onPasswordVisibilityChange() },
                loginButtonState = uiState.loginButtonState,
                onLoginButtonClick = { viewModel.onLoginButtonClick() },
                error = uiState.error
            )
        }
    }

    loginFormContent()

}

@Composable
private fun LoginFieldsAndButton(
    loginText: String,
    onLoginChange: (String) -> Unit,
    passwordText: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityChange: () -> Unit,
    loginButtonState: ButtonState,
    onLoginButtonClick: () -> Unit,
    error: Throwable?,
) {
    Box(
        modifier = Modifier.statusBarsPadding().fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.7f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextField(
                value = loginText,
                onValueChange = onLoginChange,
                placeholder = { Text(stringResource(Res.string.login_placeholder)) })

            Spacer(modifier = Modifier.height(2.dp))

            TextField(
                value = passwordText,
                onValueChange = onPasswordChange,
                placeholder = { Text(stringResource(Res.string.password_placeholder)) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image =
                        if (passwordVisible) Res.drawable.ic_hide_password else Res.drawable.ic_show_password
                    val description = if (passwordVisible) stringResource(Res.string.hide_password)
                    else stringResource(Res.string.show_password)

                    IconButton(onClick = { onPasswordVisibilityChange() }) {
                        Icon(painterResource(image), description)
                    }
                })

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = when (loginButtonState) {
                        ButtonState.IsActive -> Color.Blue
                        ButtonState.IsError -> Color.Red
                        ButtonState.IsInProgress -> Color.Gray
                        ButtonState.IsNotActive -> Color.Gray
                        ButtonState.IsSuccess -> Color.Green
                    }
                ),
                onClick = {
                    onLoginButtonClick()
                }) {
                when (loginButtonState) {
                    is ButtonState.IsInProgress -> CircularProgressIndicator()
                    is ButtonState.IsSuccess -> Text(stringResource(Res.string.success_button))
                    is ButtonState.IsError -> Text(stringResource(Res.string.button_error))
                    else -> Text(stringResource(Res.string.login_button))
                }
            }


            if (error != null) {
                Text(text = error.message ?: stringResource(Res.string.unknown_error))
            }
        }
    }
}