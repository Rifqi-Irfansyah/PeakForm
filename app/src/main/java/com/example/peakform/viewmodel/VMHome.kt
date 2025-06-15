package com.example.peakform.viewmodel
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peakform.api.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VMHome : ViewModel() {
    private val _scheduleStatus = MutableStateFlow(false)
    val scheduleStatus: StateFlow<Boolean> = _scheduleStatus

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _idUser = MutableStateFlow<String?>(null)

    fun resetState() {
        _scheduleStatus.value = false
        _isLoading.value = false
        _error.value = null
        _idUser.value = null
    }

    fun setUserId(id: String) {
        _idUser.value = id
        fetchSchedule()
    }

    private fun fetchSchedule() {
        val id = _idUser.value
        if (id == null) {
            _error.value = "User ID is null"
            Log.e("VMHome", "User ID is null")
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val apiResponse = ApiService.instance.getSchedule(id)
                Log.d("VMHome", "Schedule response: ${apiResponse.body()}")
                if (apiResponse.isSuccessful) {
                    val scheduleData = apiResponse.body()
                    _scheduleStatus.value = scheduleData != null
                    Log.d("VMHome", "Schedule status updated to: ${_scheduleStatus.value}")
                    if (scheduleData == null) {
                        _error.value = "Schedule data is null"
                    }
                } else {
                    _error.value = "Failed to fetch schedule: ${apiResponse.errorBody()?.string()}"
                    Log.e("VMHome", "Failed to fetch schedule: ${apiResponse.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Error ethernet", "${e.message}")
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}