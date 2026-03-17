package org.example.project.presentation.appNavigate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import org.example.project.presentation.helloScreen.HelloScreen
import org.example.project.presentation.authorizationScreen.AuthorizationScreen
import org.example.project.presentation.loadingScreen.LoadingScreen
import org.example.project.presentation.mainScreenWithNavigation.MainScreenWithNavigation
import org.koin.compose.viewmodel.koinViewModel

@Serializable
private sealed class Routes(val route: String) {
    object NavigateLoadingScreen : Routes("loading")
    object NavigateHelloScreen : Routes("home")
    object NavigateLoginScreen : Routes("login")
    object NavigateMainScreen : Routes("main")
}

@Composable
internal fun AppNavigate(
    navController: NavHostController = rememberNavController()
) {
    val viewModel: AppNavigateViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    val isNavigationPerformed = remember { mutableStateOf(false) }

    LaunchedEffect(state.isLoggedIn, state.isFirstTimeInApp) {
        if (!isNavigationPerformed.value) {
            delay(100)

            when {
                state.isLoggedIn -> {
                    navController.navigate(Routes.NavigateMainScreen.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }

                state.isFirstTimeInApp -> {
                    navController.navigate(Routes.NavigateHelloScreen.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }

                else -> {
                    navController.navigate(Routes.NavigateLoginScreen.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }

            isNavigationPerformed.value = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.NavigateLoadingScreen.route
    ) {
        composable(Routes.NavigateLoadingScreen.route) {
            LoadingScreen()
            LaunchedEffect(Unit) {
                Napier.d("Screen loading is open", tag = "Navigate")
            }
        }

        composable(Routes.NavigateHelloScreen.route) {
            HelloScreen {
                navController.navigate(Routes.NavigateLoginScreen.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
            LaunchedEffect(Unit) {
                Napier.d("Screen hello is open", tag = "Navigate")
            }
        }

        composable(Routes.NavigateLoginScreen.route) {
            AuthorizationScreen {
                navController.navigate(Routes.NavigateMainScreen.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
            LaunchedEffect(Unit) {
                Napier.d("Screen Authorization is open", tag = "Navigate")
            }
        }

        composable(Routes.NavigateMainScreen.route) {
            MainScreenWithNavigation()
            LaunchedEffect(Unit) {
                Napier.d("Screen main is open", tag = "Navigate")
            }
        }
    }
}

