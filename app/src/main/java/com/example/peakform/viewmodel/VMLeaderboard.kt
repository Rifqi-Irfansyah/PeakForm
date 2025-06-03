package com.example.peakform.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peakform.api.LogService
import com.example.peakform.data.model.LeaderboardResponse
import com.example.peakform.data.model.UserLeaderboard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response


class VMLeaderboard: ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _usersLeaderboard = MutableStateFlow<List<UserLeaderboard>>(emptyList())
    val userLeaderboard: StateFlow<List<UserLeaderboard>> = _usersLeaderboard


    fun resetState() {
        _loading.value = false
        _success.value = false
        _error.value = null
    }

    fun getLeaderboard() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response: Response<LeaderboardResponse> = LogService.instance.getLeaderboard()
                if (response.isSuccessful) {
                    val leaderboardResponse = response.body()?.data
                    if (leaderboardResponse != null) {
                        _usersLeaderboard.value = leaderboardResponse
                        _success.value = true
                    } else {
                        _error.value = "Leaderboard data is null"
                    }
                } else {
                    _error.value = "Error fetching leaderboard: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                _error.value = "Error fetching leaderboard: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}