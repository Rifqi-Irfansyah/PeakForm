package com.example.peakform.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peakform.API.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VMMakeSchedule : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error


    fun makeSchedule(answers: Map<Int, String>) {
        val idExerciseType = mutableMapOf<String, Any>()
        idExerciseType["ChestBeginner"] = 1
        idExerciseType["ChestIntermediate"] = 2

        viewModelScope.launch {
            _loading.value = true
            try {
                val idExercises : List<Int>
                val idExerciseStrength1 = listOf(1, 2, 3, 4)
                val idExerciseStrength2 = listOf(1, 2, 3, 4)
                val idExerciseStrength3 = listOf(1, 2, 3, 4)
                val idExercisesCardio1 = listOf(1, 2, 3, 4)
                val idExercisesCardio2 = listOf(1, 2, 3, 4)
                val idExercisesCardio3 = listOf(1, 2, 3, 4)
                val requestBody = mutableMapOf<String, Any>()
                requestBody["id_user"] = "115dd593-1f58-454f-bd25-318cfd2b4810"
                requestBody["id_exercise"] = 1
                requestBody["day"] = 4

                if(answers[0] == "Build Muscle"){
                    requestBody["type"] = "strength"
                    if (answers[1] == "3-7 times") {
                        requestBody["set"] = 4
                        requestBody["repetition"] = 12
                        idExercises = idExerciseStrength1
                    } else if (answers[1] == "1-2 times"){
                        requestBody["set"] = 4
                        requestBody["repetition"] = 8
                        idExercises = idExerciseStrength2
                    } else{
                        requestBody["set"] = 3
                        requestBody["repetition"] = 8
                        idExercises = idExerciseStrength3
                    }
                }
                else{
                    requestBody["type"] = "cardio"
                    if (answers[1] == "3-7 times") {
                        requestBody["set"] = 4
                        requestBody["repetition"] = 12
                        idExercises = idExercisesCardio1
                    } else if (answers[1] == "1-2 times"){
                        requestBody["set"] = 4
                        requestBody["repetition"] = 8
                        idExercises = idExercisesCardio2
                    } else{
                        requestBody["set"] = 3
                        requestBody["repetition"] = 8
                        idExercises = idExercisesCardio3
                    }
                }

                for (exerciseId in idExercises) {
                    requestBody["id_exercise"] = exerciseId

                    val apiResponse = ApiService.instance.createSchedule(requestBody)

                    if (apiResponse.isSuccessful) {
                        val responseString = apiResponse.body()?.string()
                        Log.d("API Response", "Success for exercise $exerciseId: $responseString")
                    } else {
                        val errorMessage = apiResponse.errorBody()?.string() ?: "Unknown error"
                        Log.e("API Response", "Error for exercise $exerciseId: $errorMessage")
                    }
                }
            } catch (e: Exception) {
                Log.e("Error ethernet", "${e.message}")
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}
