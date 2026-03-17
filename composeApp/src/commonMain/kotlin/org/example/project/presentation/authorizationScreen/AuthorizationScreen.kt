package org.example.project.presentation.authorizationScreen

import androidx.compose.runtime.Composable



@Composable
expect fun AuthorizationScreen(
    navigateToMain: () -> Unit
)