package com.example.peakform.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peakform.api.AuthService
import com.example.peakform.api.LogService
import com.example.peakform.data.model.ChangePasswordRequest
import com.example.peakform.data.model.GenericResponse
import com.example.peakform.data.model.GetLogResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

class VMProfile : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _logs = MutableStateFlow<List<com.example.peakform.data.model.Log>>(emptyList())
    val logs: StateFlow<List<com.example.peakform.data.model.Log>> = _logs


    fun resetState() {
        _loading.value = false
        _success.value = false
        _error.value = null
    }

    fun getLog(id: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response: Response<GetLogResponse> = LogService.instance.getLog(id)
                Thread.sleep(2000)

                if (response.isSuccessful) {
                    Log.d("VMProfile", "Log fetched successfully")
                    val logResponse = response.body()
                    Log.d("VMProfile", "Log response: $logResponse")
                    if (logResponse != null) {
                        _logs.value = logResponse.data
                        Log.d("VMProfile", "Log data: ${logResponse.data}")
                    } else {
                        Log.e("VMProfile", "Log response is null")
                    }
                } else {
                    Log.e("VMProfile", "Error fetching log: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("VMProfile", "Error fetching log: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

    fun changePassword(id: String, oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val changePasswordRequest = ChangePasswordRequest(id, oldPassword, newPassword)
                val response: Response<GenericResponse> = AuthService.instance.changePassword(changePasswordRequest)
                Thread.sleep(2000)

                if (response.isSuccessful) {
                    _success.value = true
                } else {
                    _error.value = response.errorBody()?.string()?.let { parseErrorMessage(it) } ?: "Unknown error"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    private fun parseErrorMessage(json: String): String {
        return try {
            val jsonObject = JSONObject(json)
            val details = jsonObject.optString("details", "No details provided")
            val message = jsonObject.optString("message", "No message provided")
            val status = jsonObject.optString("status", "No status provided")
            "($status) $message\n $details"
        } catch (_: JSONException) {
            "Unknown error"
        }
    }

}