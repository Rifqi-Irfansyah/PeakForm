package com.example.peakform.navigation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.peakform.data.model.Exercises
import com.example.peakform.screens.schedule.DetailSchedule
import com.example.peakform.screens.schedule.ShowSchedule
import com.example.peakform.viewmodel.VMShowSchedule
import com.example.peakform.screens.Home
import com.example.peakform.screens.profile.Profile
import com.example.peakform.screens.Search
import com.example.peakform.screens.auth.Login
import com.example.peakform.screens.auth.forgetpassword.ForgetPassword
import com.example.peakform.screens.auth.forgetpassword.ResetPassword
import com.example.peakform.screens.auth.register.Register
import com.example.peakform.screens.auth.register.VerifyRegister
import com.example.peakform.screens.schedule.MakeSchedule
import com.example.peakform.screens.profile.ChangePassword
import com.example.peakform.screens.schedule.StartExercise
import com.example.peakform.viewmodel.VMUser
import com.google.accompanist.navigation.animation.AnimatedNavHost

@SuppressLint("ViewModelConstructorInComposable")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier, VMUser: VMUser, startDestination: String) {
    var vmShowSchedule: VMShowSchedule = androidx.lifecycle.viewmodel.compose.viewModel()
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            slideInHorizontally(initialOffsetX = { it }) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -it }) + fadeOut(animationSpec = tween(300))
        },
        modifier = modifier,
    ) {
        // Authentication screens
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
        composable(Screens.ForgetPassword.route) {
            ForgetPassword(navController)
        }
        composable(
            route = "reset_password_screen/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            ResetPassword(navController, email = email)
        }

        // Home screen
        composable(Screens.Home.route) {
            Home(navController)
        }

        // Profile screen
        composable(Screens.Profile.route) {
            Profile(navController, userViewModel = VMUser)
        }
        composable(Screens.ChangePassword.route) {
            ChangePassword(navController, userViewModel = VMUser)
        }

        // Other screens
        composable(Screens.MakeSchedule.route) {
            MakeSchedule(navController)
        }
        composable(Screens.Search.route) {
            Search(navController)
        }
        composable(Screens.ShowSchedule.route) {
            vmShowSchedule = androidx.lifecycle.viewmodel.compose.viewModel()
            ShowSchedule(navController, vmShowSchedule)
        }
        composable(Screens.DetailSchedule.route) {
            DetailSchedule(navController, viewModel = vmShowSchedule)
        }
        composable(Screens.StartExercise.route) {
            StartExercise(navController, viewModel = vmShowSchedule)
        }
    }
}

