package org.example.project.appNavigate

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import org.example.project.helloScreen.HelloScreen
import org.example.project.loginScreen.LoginScreen
import org.example.project.mainScreen.MainScreen

@Serializable
private sealed class Routes(val route: String) {
    object NavigateHelloScreen : Routes("home")
    object NavigateLoginScreen : Routes("login")
    object NavigateMainScreen : Routes("main")
}

@Composable
internal fun AppNavigate(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Routes.NavigateHelloScreen.route) {

        composable(Routes.NavigateHelloScreen.route) {
            HelloScreen { navController.navigate(Routes.NavigateLoginScreen.route) }
        }

        composable(Routes.NavigateLoginScreen.route) {
            LoginScreen {
                navController.navigate(Routes.NavigateMainScreen.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }

        composable(Routes.NavigateMainScreen.route) {
            MainScreen()
        }

    }
}

