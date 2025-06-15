package com.example.peakform.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peakform.api.UserService
import com.example.peakform.data.model.User
import kotlinx.coroutines.launch

class VMUser : ViewModel() {
    var user by mutableStateOf<User?>(null)
        private set

    fun fetchUser(userid: String) {
        viewModelScope.launch {
            try {
                val response = UserService.instance.getUser(userid)
                if (response.isSuccessful) {
                    user = response.body()?.data
                } else {
                    return@launch
                }
            } catch (e: Exception) {
                return@launch
            }
        }
    }

    fun updateUser(user: User) {
        this.user = user
    }
}