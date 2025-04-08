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
    object MakeSchedule : Screens("makeschedule_screen")
    object Search : Screens("search_screen")
}