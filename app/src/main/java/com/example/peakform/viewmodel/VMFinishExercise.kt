package com.example.peakform.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peakform.api.StreakService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class VMFinishExercise: ViewModel() {
    private val _idUser = MutableStateFlow<String?>(null)
    fun setUserId(id: String) {
        _idUser.value = id
    }

    fun updateStreak() {
        viewModelScope.launch {
            try {
                val apiResponse = StreakService.instance.updateStreak(_idUser.value ?: "")
                if (apiResponse.isSuccessful) {
                    Log.d("Streak Updated", "Streak updated successfully")
                } else {
                    Log.e("Error", "Failed to update streak: ${apiResponse.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Error ethernet", "${e.message}")
            }
        }
    }
}