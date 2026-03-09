package org.example.project.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import org.example.project.presentation.appNavigate.AppNavigate

@Composable
@Preview
fun App() {
    MaterialTheme {
        AppNavigate()
    }
}