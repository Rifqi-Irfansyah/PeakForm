package com.example.peakform.navigation

sealed class Screens(val route : String) {
    object Auth : Screens("auth_screen")
    object Register : Screens("register_screen")
    object VerifyRegister : Screens("verify_register_screen/{email}") {
        fun passEmail(email: String): String {
            return "verify_register_screen/$email"
        }
    }
    object ForgetPassword : Screens("forget_password_screen")
    object ResetPassword : Screens("reset_password_screen/{email}") {
        fun passEmail(email: String): String {
            return "reset_password_screen/$email"
        }
    }
    object Home : Screens("home_screen")
    object Profile : Screens("profile_screen")
    object ChangePassword : Screens("change_password_screen")
    object MakeSchedule : Screens("make_schedule_screen")
    object ShowSchedule : Screens("show_schedule_screen")
    object DetailSchedule: Screens("detail_schedule_screen")
    object StartExercise: Screens("start_exercise_screen")
    object Search : Screens("search_screen")
    object SplashScreen : Screens("splash_screen")
    object Notification : Screens("notification_screen")
    object Leaderboard : Screens("leaderboard_screen")
    object FinishExercise: Screens("finish_exercise_screen")
}