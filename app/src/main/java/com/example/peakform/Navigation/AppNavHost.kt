package com.example.peakform.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.peakform.Screens.Home
import com.example.peakform.Screens.MakeSchedule
import com.example.peakform.Screens.Profile

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screens.Home.route,
        modifier = modifier
    ) {
        composable(Screens.Home.route) {
            Home(navController)
        }
        composable(Screens.Profile.route) {
            Profile(navController)
        }
        composable(Screens.MakeSchedule.route) {
            MakeSchedule()
        }
    }
}
