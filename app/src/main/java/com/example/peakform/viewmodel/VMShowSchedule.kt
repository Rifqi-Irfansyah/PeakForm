package com.example.peakform.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peakform.api.ApiService
import com.example.peakform.data.model.Exercises
import com.example.peakform.data.model.Schedule
import com.example.peakform.data.model.ScheduleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VMShowSchedule : ViewModel() {
    private val _schedule = MutableStateFlow<ScheduleData?>(null)
    val schedule: StateFlow<ScheduleData?> = _schedule

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _selectedSchedule = MutableStateFlow<Schedule?>(null)
    val selectedSchedule: StateFlow<Schedule?> = _selectedSchedule

    fun selectSchedule(schedule: Schedule) {
        _selectedSchedule.value = schedule
    }

    val idUser = "115dd593-1f58-454f-bd25-318cfd2b4810"

    init {
        fetchSchedule()
    }

    private fun fetchSchedule() {
        viewModelScope.launch {
            try {
                val apiResponse = ApiService.instance.getSchedule(idUser)
                if (apiResponse.isSuccessful) {
                    _schedule.value = apiResponse.body()?.data
                } else {
                    _error.value = "Unknown error"
                }
            } catch (e: Exception) {
                Log.e("Error ethernet", "${e.message}")
                _error.value = e.message
            }
        }
    }
}