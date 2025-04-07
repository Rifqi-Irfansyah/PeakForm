package com.example.peakform.navigation

sealed class Screens(val route : String) {
    object Home : Screens("home_screen")
    object Profile : Screens("profile_screen")
    object MakeSchedule : Screens("makeschedule_screen")
    object Auth : Screens("auth_screen")
    object Register : Screens("register_screen")
    object VerifyRegister : Screens("verify_register_screen/{email}") {
        fun passEmail(email: String): String {
            return "verify_register_screen/$email"
        }
    }
    object Search : Screens("search_screen")
}