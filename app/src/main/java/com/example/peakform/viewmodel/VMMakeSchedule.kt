package com.example.peakform.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peakform.api.ApiService
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
    var allSuccess = true

    fun makeSchedule(answers: Map<Int, String>) {
        val idExerciseType = mutableMapOf<String, List<Int>>()
        idExerciseType["ChestEasy"]     = listOf(125,165,166)
        idExerciseType["ChestHard"]     = listOf(167,168,169)
        idExerciseType["TricepsEasy"]   = listOf(144,145)
        idExerciseType["TricepsHard"]   = listOf(146,147)
        idExerciseType["BackEasy"]      = listOf(129,130,133,136)
        idExerciseType["BackHard"]      = listOf(129,130,134,137)
        idExerciseType["Shoulder"]      = listOf(170,171,172,173)
        idExerciseType["Traps"]         = listOf(142,143)
        idExerciseType["Neck"]          = listOf(138)
        idExerciseType["BicepsEasy"]    = listOf(156,157,158)
        idExerciseType["BicepsHard"]    = listOf(159,160,161)
        idExerciseType["Forearms"]      = listOf(126,127)
        idExerciseType["Quadriceps"]    = listOf(139,140,141)
        idExerciseType["CalvesEasy"]    = listOf(162)
        idExerciseType["CalvesHard"]    = listOf(164)
        idExerciseType["ABSEasy"]       = listOf(153,154)
        idExerciseType["ABSHard"]       = listOf(153,154)
        idExerciseType["Jumping"]       = listOf(148,150,151)
        idExerciseType["SlowJog"]       = listOf(149)

        viewModelScope.launch {
            _loading.value = true
            try {
                val idExercises = mutableMapOf<Int,List<Int>>()
                val requestBody = mutableMapOf<String, Any>()
                requestBody["id_user"] = "115dd593-1f58-454f-bd25-318cfd2b4810"
                requestBody["id_exercise"] = 1
                requestBody["day"] = 4

                if(answers[0] == "Build Muscle"){
                    requestBody["type"] = "strength"
                    if (answers[1] == "3-7 times") {
                        requestBody["set"] = 4
                        requestBody["repetition"] = 12
                        //Jadwal per hari
                        idExercises[1] = idExerciseType["ChestEasy"] as List<Int> + idExerciseType["TricepsEasy"] as List<Int>
                        idExercises[2] = idExerciseType["BackEasy"] as List<Int>
                        idExercises[3] = idExerciseType["Shoulder"] as List<Int> + idExerciseType["Traps"] as List<Int>
                        idExercises[5] = idExerciseType["BicepsEasy"] as List<Int> + idExerciseType["Forearms"] as List<Int>
                        idExercises[6] = idExerciseType["Quadriceps"] as List<Int> + idExerciseType["CalvesEasy"] as List<Int> + idExerciseType["ABSEasy"] as List<Int>
                    }
                    else if (answers[1] == "1-2 times"){
                        requestBody["set"] = 4
                        requestBody["repetition"] = 8
                        idExercises[1] = idExerciseType["ChestHard"] as List<Int> + idExerciseType["TricepsHard"] as List<Int>
                        idExercises[2] = idExerciseType["BackHard"] as List<Int>
                        idExercises[3] = idExerciseType["Shoulder"] as List<Int> + idExerciseType["Traps"] as List<Int>
                        idExercises[5] = idExerciseType["BicepsHard"] as List<Int> + idExerciseType["Forearms"] as List<Int>
                        idExercises[6] = idExerciseType["Quadriceps"] as List<Int> + idExerciseType["CalvesHard"] as List<Int> + idExerciseType["ABSHard"] as List<Int>
                    }
                    else{
                        requestBody["set"] = 3
                        requestBody["repetition"] = 8
                        idExercises[1] = idExerciseType["ChestHard"] as List<Int> + idExerciseType["TricepsHard"] as List<Int>
                        idExercises[2] = idExerciseType["BackHard"] as List<Int>
                        idExercises[3] = idExerciseType["Shoulder"] as List<Int> + idExerciseType["Traps"] as List<Int>
                        idExercises[5] = idExerciseType["BicepsHard"] as List<Int> + idExerciseType["Forearms"] as List<Int>
                        idExercises[6] = idExerciseType["Quadriceps"] as List<Int> + idExerciseType["CalvesHard"] as List<Int> + idExerciseType["ABSHard"] as List<Int>
                    }
                    insertData(idExercises, requestBody)
                }
                else{
                    // cardio repetition merepresentasikan waktu dalam detik
                    requestBody["type"] = "cardio"
                    if (answers[1] == "3-7 times") {
                        requestBody["set"] = 2
                        requestBody["repetition"] = 60
                        idExercises[2] = idExerciseType["Jumping"] as List<Int>
                        idExercises[6] = idExerciseType["Jumping"] as List<Int>
                        insertData(idExercises, requestBody)

                        requestBody["set"] = 1
                        requestBody["repetition"] = 25*60
                        idExercises.clear()
                        idExercises[1] = idExerciseType["SlowJog"] as List<Int>
                        idExercises[5] = idExerciseType["SlowJog"] as List<Int>
                        insertData(idExercises, requestBody)
                    }
                    else if (answers[1] == "1-2 times"){
                        requestBody["set"] = 2
                        requestBody["repetition"] = 45
                        idExercises[2] = idExerciseType["Jumping"] as List<Int>
                        idExercises[6] = idExerciseType["Jumping"] as List<Int>
                        insertData(idExercises, requestBody)

                        requestBody["set"] = 1
                        requestBody["repetition"] = 15*60
                        idExercises.clear()
                        idExercises[1] = idExerciseType["SlowJog"] as List<Int>
                        idExercises[5] = idExerciseType["SlowJog"] as List<Int>
                        insertData(idExercises, requestBody)
                    }
                    else{
                        requestBody["set"] = 2
                        requestBody["repetition"] = 30
                        idExercises[2] = idExerciseType["Jumping"] as List<Int>
                        idExercises[6] = idExerciseType["Jumping"] as List<Int>
                        insertData(idExercises, requestBody)

                        requestBody["set"] = 1
                        requestBody["repetition"] = 10*60
                        idExercises.clear()
                        idExercises[1] = idExerciseType["SlowJog"] as List<Int>
                        idExercises[5] = idExerciseType["SlowJog"] as List<Int>
                        insertData(idExercises, requestBody)
                    }
                }
            }
            catch (e: Exception) {
                Log.e("Error ethernet", "${e.message}")
                _error.value = e.message
                allSuccess = false
            }
            finally {
                _loading.value = false
            }

            if (allSuccess) {
                _success.value = true
            }
        }
    }

    private suspend fun insertData(
        idExercises: Map<Int, List<Int>>,
        requestBody: MutableMap<String, Any>
    ){
        for((day, exerciseList) in idExercises){
            requestBody["day"] = day
            for (exerciseId in exerciseList) {
                requestBody["id_exercise"] = exerciseId

                val apiResponse = ApiService.instance.createSchedule(requestBody)
                if (apiResponse.isSuccessful) {
                    val responseString = apiResponse.body()?.string()
                    Log.d("API Response", "Success for exercise $exerciseId: $responseString")
                }
                else {
                    val errorMessage = apiResponse.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Response", "Error for exercise $exerciseId: $errorMessage")
                    throw Exception("failed create schedule: $errorMessage")
                }
            }
        }
    }
}
