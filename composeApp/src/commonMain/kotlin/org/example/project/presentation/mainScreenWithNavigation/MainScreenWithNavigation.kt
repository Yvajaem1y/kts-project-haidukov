package org.example.project.presentation.mainScreenWithNavigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.example.project.presentation.common.GitHubTheme
import org.example.project.presentation.favouritesScreen.FavouritesScreen
import org.example.project.presentation.mainScreenWithNavigation.bottomNavigationUtils.Constants
import org.example.project.presentation.repositoriesScreen.RepositoriesScreen
import org.example.project.presentation.profileScreen.ProfileScreen
import org.jetbrains.compose.resources.painterResource

@Composable
fun MainScreenWithNavigation() {
    val navController = rememberNavController()
    val colors = GitHubTheme.colors

    Box(
        modifier = Modifier
            .background(colors.backgroundPrimary)
    ) {
        Surface(
            color = colors.backgroundPrimary,
            modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
        ) {
            Scaffold(
                bottomBar = {
                    BottomNavigationBar(navController = navController)
                },
                containerColor = colors.backgroundPrimary,
                contentColor = colors.textPrimary,
                content = { paddingValues ->
                    NavHostContainer(
                        navController = navController,
                        padding = paddingValues
                    )
                }
            )
        }
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
        }
    )
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val colors = GitHubTheme.colors

    NavigationBar(
        containerColor = colors.backgroundSecondary,
        contentColor = colors.textSecondary,
        tonalElevation = 0.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Constants.BottomNavItems.forEach { navItem ->
            val isSelected = currentRoute == navItem.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.popBackStack(navItem.route, false)
                    navController.navigate(navItem.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(navItem.icon),
                        contentDescription = navItem.label,
                        tint = if (isSelected) colors.primary else colors.textTertiary
                    )
                },
                label = {
                    Text(
                        text = navItem.label,
                        color = if (isSelected) colors.primary else colors.textTertiary
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colors.primary,
                    unselectedIconColor = colors.textTertiary,
                    selectedTextColor = colors.primary,
                    unselectedTextColor = colors.textTertiary,
                    indicatorColor = colors.primary.copy(alpha = 0.1f)
                )
            )
        }
    }
}