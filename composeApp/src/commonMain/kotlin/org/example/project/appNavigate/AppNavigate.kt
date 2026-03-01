package org.example.project.appNavigate

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import org.example.project.helloScreen.HelloScreen
import org.example.project.loginScreen.LoginScreen

private sealed class Routes(val route: String) {
    @Serializable
    object NavigateHelloScreen : Routes("home")
    @Serializable
    object NavigateLoginScreen : Routes("login")
}

@Composable
internal fun AppNavigate(
    navController : NavHostController = rememberNavController()
){
    NavHost(navController = navController, startDestination = Routes.NavigateHelloScreen.route) {

        composable(Routes.NavigateHelloScreen.route) {
            HelloScreen({navController.navigate(Routes.NavigateLoginScreen.route)})
        }

        composable(Routes.NavigateLoginScreen.route) {
            LoginScreen()
        }

    }
}

