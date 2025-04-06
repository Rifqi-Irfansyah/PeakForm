package com.example.peakform.ViewModel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peakform.API.AuthService
import com.example.peakform.data.model.RegisterRequest
import com.example.peakform.data.model.RegisterResponse
import com.example.peakform.data.model.VerifyRegisterRequest
import com.example.peakform.data.model.VerifyRegisterResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

class VMRegister: ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error


    fun verifyRegister(email: String, otp: String, username: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response: Response<VerifyRegisterResponse> = AuthService.instance.verifyRegister(VerifyRegisterRequest(
                    email,
                    otp,
                    username,
                    password
                ))
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

    fun sendVerificationEmail(email: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response: Response<RegisterResponse> = AuthService.instance.register(
                    RegisterRequest(email)
                )
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
       } catch (e: JSONException) {
           "Unknown error"
       }
   }

    fun resetState() {
        _loading.value = false
        _success.value = false
        _error.value = null
    }
}