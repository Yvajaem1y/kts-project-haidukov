package org.example.project.presentation.mainScreenWithNavigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.example.project.presentation.favouritesScreen.FavouritesScreen
import org.example.project.presentation.mainScreenWithNavigation.bottomNavigationUtils.Constants
import org.example.project.presentation.repositoriesScreen.RepositoriesScreen
import org.example.project.presentation.profileScreen.ProfileScreen
import org.jetbrains.compose.resources.painterResource

@Composable
fun MainScreenWithNavigation() {
    val navController = rememberNavController()
    Surface(color = Color.White) {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(navController = navController)
            }, content = { padding ->
                NavHostContainer(navController = navController, padding = padding)
            }
        )
    }
}


@Composable
fun NavHostContainer(
    navController: NavHostController,
    padding: PaddingValues
) {

    NavHost(
        navController = navController,
        startDestination = "repositories",
        modifier = Modifier.padding(paddingValues = padding),

        builder = {
            composable("repositories") {
                RepositoriesScreen()
            }
            composable("favourites") {
                FavouritesScreen()
            }
            composable("profile") {
                ProfileScreen()
            }
        })
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {

    NavigationBar(
        containerColor = Color.Black
    ) {

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Constants.BottomNavItems.forEach { navItem ->

            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    navController.navigate(navItem.route)
                },
                icon = {
                    Icon(
                        painter = painterResource(navItem.icon),
                        contentDescription = navItem.label
                    )
                },
                label = {
                    Text(text = navItem.label)
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
