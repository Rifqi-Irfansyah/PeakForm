package com.example.peakform.viewmodel

import android.util.Log
import com.example.peakform.api.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peakform.data.model.ExerciseScheduleRequest
import com.example.peakform.ui.components.ExerciseSelectionItem
import kotlinx.coroutines.launch

class VMUpdateSchedule:ViewModel(){
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    var allSuccess = true

    fun editExerciseSchedule(idSchedule: Long, idExercise: Int, newIdExercise: Int, set: Int, rep: Int) {
        viewModelScope.launch {
            val requestBody = ExerciseScheduleRequest(
                id = idSchedule.toString(),
                id_exercise = idExercise,
                new_id_exercise = newIdExercise,
                set = set,
                repetition = rep
            )
            _loading.value = true
            try {
                val apiResponse = ApiService.instance.updateExerciseSchedule(requestBody)
                if (apiResponse.isSuccessful) {
                    _success.value = true
                } else {
                    val errorMessage = apiResponse.errorBody()?.string() ?: "Unknown error"
                    Log.e("Error ethernett", "${errorMessage}")
                    throw Exception("failed updated schedule: $errorMessage")
                }
            } catch (e: Exception) {
                Log.e("Error ethernet", "${e.message}")
                _error.value = e.message
                allSuccess = false
            } finally {
                _loading.value = false
            }
        }
    }
}