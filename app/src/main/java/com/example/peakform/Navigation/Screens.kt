package com.example.peakform.Navigation

sealed class Screens(val route : String) {
    object Home : Screens("home_screen")
    object Profile : Screens("profile_screen")
}