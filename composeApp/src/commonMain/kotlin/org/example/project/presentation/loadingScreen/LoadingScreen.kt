package org.example.project.presentation.loadingScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.example.project.presentation.common.GitHubTheme

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize().background(GitHubTheme.colors.backgroundPrimary),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = GitHubTheme.colors.primary
        )
    }
}