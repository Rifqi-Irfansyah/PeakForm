package com.example.peakform.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBar(navController: NavController) { // Ambil NavController dari luar
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val screensWithBottomBar = listOf(
        Screens.Home.route,
        Screens.Profile.route
    )

    if (currentDestination?.route in screensWithBottomBar) { // Hanya tampil di layar tertentu
        NavigationBar {
            BottomNavItem().BottomNavItem().forEach { navigationItem ->
                NavigationBarItem(
                    selected = navigationItem.route == currentDestination?.route,
                    label = { Text(navigationItem.label) },
                    icon = {
                        Icon(
                            navigationItem.icon,
                            contentDescription = navigationItem.label
                        )
                    },
                    onClick = {
                        navController.navigate(navigationItem.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}
