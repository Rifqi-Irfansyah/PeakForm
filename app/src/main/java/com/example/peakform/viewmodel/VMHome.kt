package com.example.peakform.viewmodel
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peakform.api.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VMHome : ViewModel(){
    private val _scheduleStatus = MutableStateFlow(false)
    val scheduleStatus: StateFlow<Boolean> = _scheduleStatus

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _idUser = MutableStateFlow<String?>(null)

    fun setUserId(id: String) {
        _idUser.value = id
        fetchSchedule()
    }

    private fun fetchSchedule() {
        val id = _idUser.value
        if (id == null) {
            _error.value = "User ID is null"
            return
        }

        viewModelScope.launch {
            try {
                val apiResponse = ApiService.instance.getSchedule(id)
                _scheduleStatus.value = apiResponse.isSuccessful
            } catch (e: Exception) {
                Log.e("Error ethernet", "${e.message}")
                _error.value = e.message
            }
        }
    }
}