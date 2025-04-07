package com.example.peakform.Navigation

sealed class Screens(val route : String) {
    object Home : Screens("home_screen")
    object Profile : Screens("profile_screen")
    object MakeSchedule : Screens("make_schedule_screen")
    object ShowSchedule : Screens("show_schedule_screen")
    object Auth : Screens("auth_screen")
    object Register : Screens("register_screen")
    object VerifyRegister : Screens("verify_register_screen/{email}") {
        fun passEmail(email: String): String {
            return "verify_register_screen/$email"
        }
    }
}