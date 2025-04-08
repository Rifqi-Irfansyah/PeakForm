package com.example.peakform.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peakform.api.AuthService
import com.example.peakform.data.model.EmailRequest
import com.example.peakform.data.model.GenericResponse
import com.example.peakform.data.model.ResetPasswordRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

class VMForgetPassword: ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun forgetPassword(email: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response: Response<GenericResponse> = AuthService.instance.forgotPassword(
                    EmailRequest(email)
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

    fun resetPassword(email: String, otp: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response: Response<GenericResponse> = AuthService.instance.resetPassword(
                    ResetPasswordRequest(email, otp, password)
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

    fun resetState() {
        _loading.value = false
        _success.value = false
        _error.value = null
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