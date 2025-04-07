package com.example.peakform.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.peakform.screens.Home
import com.example.peakform.screens.MakeSchedule
import com.example.peakform.screens.profile.Profile
import com.example.peakform.screens.Search
import com.example.peakform.screens.auth.Login
import com.example.peakform.screens.auth.register.Register
import com.example.peakform.screens.auth.register.VerifyRegister
import com.example.peakform.screens.profile.ChangePassword
import com.example.peakform.viewmodel.VMUser
import com.google.accompanist.navigation.animation.AnimatedNavHost

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier, VMUser: VMUser) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Screens.Auth.route,
        enterTransition = {
            slideInHorizontally(initialOffsetX = { it }) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -it }) + fadeOut(animationSpec = tween(300))
        }
    ) {
        composable(Screens.Auth.route) {
            Login(navController, userViewModel = VMUser)
        }
        composable(Screens.Register.route) {
            Register(navController)
        }
        composable(
            route = "verify_register_screen/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            VerifyRegister(navController, email = email)
        }
        composable(Screens.ChangePassword.route) {
            ChangePassword(navController, userViewModel = VMUser)
        }
        composable(Screens.Home.route) {
            Home(navController)
        }
        composable(Screens.Profile.route) {
            Profile(navController, userViewModel = VMUser)
        }
        composable(Screens.MakeSchedule.route) {
            MakeSchedule(navController)
        }
        composable(Screens.Search.route) {
            Search(navController)
        }
    }
}

