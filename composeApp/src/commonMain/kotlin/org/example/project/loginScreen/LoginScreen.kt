package org.example.project.loginScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import kts_project_haidukov.composeapp.generated.resources.Res
import kts_project_haidukov.composeapp.generated.resources.hide_password
import kts_project_haidukov.composeapp.generated.resources.ic_hide_password
import kts_project_haidukov.composeapp.generated.resources.ic_show_password
import kts_project_haidukov.composeapp.generated.resources.login_button
import kts_project_haidukov.composeapp.generated.resources.login_placeholder
import kts_project_haidukov.composeapp.generated.resources.password_placeholder
import kts_project_haidukov.composeapp.generated.resources.show_password
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun LoginScreen() {
    var loginText by rememberSaveable { mutableStateOf("") }
    var passwordText by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val loginFormContent = remember {
        movableContentOf {
            LoginFieldsAndButton(
                loginText = loginText,
                onLoginChange = { loginText = it },
                passwordText = passwordText,
                onPasswordChange = { passwordText = it },
                passwordVisible = passwordVisible,
                onPasswordVisibilityChange = { passwordVisible = !passwordVisible }
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
    onPasswordVisibilityChange: () -> Unit
) {
    Column(
        modifier = Modifier.statusBarsPadding().fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = loginText,
            onValueChange = onLoginChange,
            placeholder = { Text(stringResource(Res.string.login_placeholder)) }
        )

        TextField(
            value = passwordText,
            onValueChange = onPasswordChange,
            placeholder = { Text(stringResource(Res.string.password_placeholder)) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image =
                    if (passwordVisible) Res.drawable.ic_hide_password else Res.drawable.ic_show_password
                val description = if (passwordVisible)
                    stringResource(Res.string.hide_password)
                else
                    stringResource(Res.string.show_password)

                IconButton(onClick = { onPasswordVisibilityChange() }) {
                    Icon(painterResource(image), description)
                }
            }
        )

        Button(
            onClick = {
                onLoginChange("")
                onPasswordChange("")
            }
        ) {
            Text(stringResource(Res.string.login_button))
        }
    }
}