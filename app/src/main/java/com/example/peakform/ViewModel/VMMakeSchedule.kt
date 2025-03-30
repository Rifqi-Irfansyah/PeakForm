package com.example.peakform.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peakform.API.ApiService
import kotlinx.coroutines.launch

class VMMakeSchedule : ViewModel() {
    private val _response = MutableLiveData<String>()
    val response: LiveData<String> get() = _response

    fun submitAnswers(answers: Map<Int, String>) {
        viewModelScope.launch {
            try {
                val requestBody = mutableMapOf<String, Any>()

                if(answers[0] == "Build Muscle"){
                    requestBody["type"] = "strength"
                }
                else
                    requestBody["type"] = "cardio"

                if (answers[1] == "3-7 times") {
                    requestBody["set"] = 4
                    requestBody["repetition"] = 12
                } else if (answers[1] == "1-2 times"){
                    requestBody["set"] = 4
                    requestBody["repetition"] = 8
                } else{
                    requestBody["set"] = 3
                    requestBody["repetition"] = 8
                }

                Log.d("RequestBody", "Modified Request type: $requestBody")

                requestBody["id_user"] = "115dd593-1f58-454f-bd25-318cfd2b4819"
                requestBody["id_exercise"] = 1
                requestBody["day"] = 4

                val apiResponse = ApiService.instance.createSchedule(requestBody)

                if (apiResponse.isSuccessful) {
                    val responseString = apiResponse.body()?.string()
                    Log.d("API Response", "Success: $responseString")
                } else {
                    Log.e("API Response", "Error: ${apiResponse.errorBody()?.string()}")
                }

                _response.postValue("Success: $apiResponse")
            } catch (e: Exception) {
                Log.e("Error ethernet", "${e.message}")
                _response.postValue("Error: ${e.message}")
            }
        }
    }
}
