package org.example.project.presentation.mainScreenWithNavigation.bottomNavigationUtils

import kts_project_haidukov.composeapp.generated.resources.Res
import kts_project_haidukov.composeapp.generated.resources.ic_show_password

object Constants {
    val BottomNavItems = listOf(
        BottomNavItem(
            label = "Repositories",
            icon = Res.drawable.ic_show_password,
            route = "repositories"
        ),
        BottomNavItem(
            label = "Favourites",
            icon = Res.drawable.ic_show_password,
            route = "favourites"
        ),
        BottomNavItem(
            label = "Profile",
            icon = Res.drawable.ic_show_password,
            route = "profile"
        )
    )
}