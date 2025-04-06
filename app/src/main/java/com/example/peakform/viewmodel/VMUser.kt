package com.example.peakform.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.peakform.data.model.User

class VMUser : ViewModel() {
    var user by mutableStateOf<User?>(null)
        private set

    fun updateUser(user: User) {
        this.user = user
    }
}