package com.example.peakform.ViewModel
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peakform.API.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VMHome : ViewModel(){
    private val _scheduleStatus = MutableStateFlow(false)
    val scheduleStatus: StateFlow<Boolean> = _scheduleStatus

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    val idUser = "115dd593-1f58-454f-bd25-318cfd2b4810"

    init {
        fetchSchedule()
    }

    private fun fetchSchedule() {
        viewModelScope.launch {
            try {
                val apiResponse = ApiService.instance.getSchedule(idUser)
                _scheduleStatus.value = apiResponse.isSuccessful
            } catch (e: Exception) {
                Log.e("Error ethernet", "${e.message}")
                _error.value = e.message
            }
        }
    }
}