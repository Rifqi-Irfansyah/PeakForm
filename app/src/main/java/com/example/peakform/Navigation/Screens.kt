package com.example.peakform.Navigation

sealed class Screens(val route : String) {
    object Home : Screens("home_screen")
    object Profile : Screens("profile_screen")
    object MakeSchedule : Screens("makeschedule_screen")
    object Auth : Screens("auth_screen")
}