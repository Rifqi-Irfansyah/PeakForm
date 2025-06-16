package com.example.peakform.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peakform.api.StreakService
import com.example.peakform.api.UserService
import com.example.peakform.data.model.User
import kotlinx.coroutines.launch

class VMUser : ViewModel() {
    var user by mutableStateOf<User?>(null)

    fun checkStreak(userid: String) {
        viewModelScope.launch {
            try {
                val apiResponse = StreakService.instance.checkStreak(userid)
                if (apiResponse.isSuccessful) {
                    return@launch
                } else {
                    return@launch
                }
            } catch (e: Exception) {
                Log.e("Error ethernet", "${e.message}")
            }
        }
    }

    fun fetchUser(userid: String): User? {
        viewModelScope.launch {
            try {
                val response = UserService.instance.getUser(userid)
                if (response.isSuccessful) {
                    user = response.body()?.data?.copy(id = user?.id ?: "")
                    Log.d("VMUser", "User fetched successfully: ${user?.name}")
                } else {
                    return@launch
                }
            } catch (e: Exception) {
                return@launch
            }
        }
        return user
    }

    fun updateUser(user: User) {
        this.user = user
    }
}