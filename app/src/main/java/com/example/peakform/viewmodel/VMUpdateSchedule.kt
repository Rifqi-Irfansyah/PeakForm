package com.example.peakform.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.peakform.api.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peakform.Room.AppDatabase
import com.example.peakform.data.model.ChangeScheduleRequest
import com.example.peakform.data.model.ExerciseScheduleRequest
import kotlinx.coroutines.launch

class VMUpdateSchedule(application: Application) : AndroidViewModel(application){
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    var allSuccess = true

    fun addExerciseSchedule(idUser: String, idExercise: Int, day: Int, type:String, set: Int, rep: Int) {
        viewModelScope.launch {
            val requestBody = mutableMapOf<String, Any>(
                "id_user" to idUser,
                "id_exercise" to idExercise,
                "day" to day,
                "set" to set,
                "repetition" to rep,
                "type" to type
            )
            _loading.value = true
            try {
                val apiResponse = ApiService.instance.createSchedule(requestBody)
                if (apiResponse.isSuccessful) {
                    _success.value = true
                } else {
                    val errorMessage = apiResponse.errorBody()?.string() ?: "Unknown error"
                    Log.e("Error ethernet", errorMessage)
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

    fun addListExerciseSchedule(idUser: String, idExercise: List<Int>, day: Int, type:String, set: Int, rep: Int) {
        viewModelScope.launch {
                _loading.value = true
                try {
                    for (exerciseId in idExercise) {
                        val requestBody = mutableMapOf<String, Any>(
                            "id_user" to idUser,
                            "id_exercise" to exerciseId,
                            "day" to day,
                            "set" to set,
                            "repetition" to rep,
                            "type" to type
                        )
                        val apiResponse = ApiService.instance.createSchedule(requestBody)
                        if (apiResponse.isSuccessful) {
                            _success.value = true
                        } else {
                            val errorMessage = apiResponse.errorBody()?.string() ?: "Unknown error"
                            Log.e("Error ethernet", errorMessage)
                            throw Exception("failed updated schedule: $errorMessage")
                        }
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
                    Log.e("Error ethernet", errorMessage)
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

    fun deleteExerciseSchedule(idSchedule: Long, idExercise: Int){
        viewModelScope.launch {
            _loading.value = true
            try {
                val apiResponse = ApiService.instance.deleteExerciseSchedule(idSchedule.toString(), idExercise.toString())
                if (apiResponse.isSuccessful) {
                    _success.value = true
                } else {
                    val errorMessage = apiResponse.errorBody()?.string() ?: "Unknown error"
                    Log.e("Error ethernet", errorMessage)
                    throw Exception("failed deleted schedule: $errorMessage")
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
    private val context = getApplication<Application>().applicationContext

    fun updateDay(idSchedule: String, oldday:Int, newday: Int) {
        val db = AppDatabase.getInstance(context)
        val dao = db.notificationDao()
        viewModelScope.launch {
            val requestBody = ChangeScheduleRequest(
                id = idSchedule,
                day = newday,
            )
            _loading.value = true
            try {
                val apiResponse = ApiService.instance.updateDaySchedule(requestBody)
                if (apiResponse.isSuccessful) {
                    _success.value = true
                } else {
                    val errorMessage = apiResponse.errorBody()?.string() ?: "Unknown error"
                    Log.e("Error ethernet", errorMessage)
                    throw Exception("failed updated schedule: $errorMessage")
                }
                if(oldday != 0){
                    dao.updateDatNotification(newday, oldday)
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

    fun switchDay(firstIdSchedule: String, secondIdSchedule:String, firstDay:Int, secondDay:Int){
        updateDay(firstIdSchedule, 0, secondDay)
        updateDay(secondIdSchedule, 0, firstDay)
    }

    fun deleteScheudle(idSchedule: String, idUser: String){
        viewModelScope.launch {
            _loading.value = true
            try {
                val apiResponse = ApiService.instance.deleteSchedule(idSchedule, idUser)
                if (apiResponse.isSuccessful) {
                    _success.value = true
                } else {
                    val errorMessage = apiResponse.errorBody()?.string() ?: "Unknown error"
                    Log.e("Error ethernet", errorMessage)
                    throw Exception("failed deleted schedule: $errorMessage")
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