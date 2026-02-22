package org.example.project.appNavigate

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.example.project.helloScreen.HelloScreen
import org.example.project.loginScreen.LoginScreen

private sealed class Routes(val route: String) {
    object NavigateHelloScreen : Routes("home")
    object NavigateLoginScreen : Routes("contact")
}

@Composable
internal fun AppNavigate(
    navController : NavHostController
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

