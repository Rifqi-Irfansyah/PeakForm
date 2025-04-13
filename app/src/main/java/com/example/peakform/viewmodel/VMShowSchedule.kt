package com.example.peakform.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peakform.api.ApiService
import com.example.peakform.api.LogService
import com.example.peakform.data.model.CreateLogRequest
import com.example.peakform.data.model.Exercises
import com.example.peakform.data.model.Schedule
import com.example.peakform.data.model.ScheduleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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

    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    init {
        sdf.timeZone = TimeZone.getTimeZone("UTC")
    }
    val timestamp = sdf.format(Date())

    private val _idUser = MutableStateFlow<String?>(null)
    fun setUserId(id: String) {
        _idUser.value = id
        fetchSchedule()
    }

    init {
        fetchSchedule()
    }

    fun createLog(exercise: Exercises) {
        viewModelScope.launch {
            try {
                val apiResponse = LogService.instance.createLog(
                    CreateLogRequest(
                        userId = _idUser.value ?: "",
                        exerciseId = exercise.id,
                        timestamp = timestamp,
                        set = exercise.set,
                        repetition = exercise.repetition
                    )
                )
                if (apiResponse.isSuccessful) {
                    Log.d("Log Created", "Log created successfully")
                } else {
                    Log.e("Error", "Failed to create log: ${apiResponse.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Error ethernet", "${e.message}")
            }
        }
    }

    private fun fetchSchedule() {
        viewModelScope.launch {
            try {
                val apiResponse = ApiService.instance.getSchedule(_idUser.value ?: "")
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