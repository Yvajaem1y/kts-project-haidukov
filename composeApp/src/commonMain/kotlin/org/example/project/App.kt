package org.example.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import org.example.project.appNavigate.AppNavigate

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        AppNavigate(navController = navController)
    }
}