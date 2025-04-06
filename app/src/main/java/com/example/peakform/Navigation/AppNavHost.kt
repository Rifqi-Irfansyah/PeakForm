package com.example.peakform.Navigation

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
import com.example.peakform.Screens.auth.Login
import com.example.peakform.Screens.Home
import com.example.peakform.Screens.MakeSchedule
import com.example.peakform.Screens.Profile
import com.example.peakform.Screens.auth.register.Register
import com.example.peakform.Screens.auth.register.VerifyRegister
import com.google.accompanist.navigation.animation.AnimatedNavHost

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
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
            Login(navController)
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
        composable(Screens.Home.route) {
            Home(navController)
        }
        composable(Screens.Profile.route) {
            Profile(navController)
        }
        composable(Screens.MakeSchedule.route) {
            MakeSchedule(navController)
        }
    }
}
