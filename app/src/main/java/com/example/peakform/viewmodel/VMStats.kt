package com.example.peakform.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import com.example.peakform.api.StatsService
import com.example.peakform.data.model.StatsSummary

class VMStats : ViewModel() {
    var stats by mutableStateOf<StatsSummary?>(null)
    var errorMessage by mutableStateOf<String?>(null)

    fun fetchStats(userId: String) {
        viewModelScope.launch {
            try {
                val response = StatsService.instance.getUserStats(userId)
                if (response.isSuccessful) {
                    Log.d("VMStats", "Response body: ${response.body()}")
                    stats = response.body()?.data
                } else {
                    errorMessage = "Failed: ${response.message()}"
                }
            } catch (e: Exception) {
                Log.e("VMStats", "Exception: ${e.message}")
                errorMessage = e.message
            }
        }
    }
}
