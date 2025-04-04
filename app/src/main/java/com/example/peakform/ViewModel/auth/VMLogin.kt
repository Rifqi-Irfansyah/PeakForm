package com.example.peakform.ViewModel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peakform.API.AuthService
import com.example.peakform.data.model.AuthRequest
import com.example.peakform.data.model.AuthResponse
import com.example.peakform.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

class VMLogin : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val authRequest = AuthRequest(username, password)
                val response: Response<AuthResponse> = AuthService.instance.auth(authRequest)
                if (response.isSuccessful) {
                    _user.value = response.body()?.data
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
            val details = jsonObject.getJSONObject("details")
            val emailError = details.getString("Email")
            "Email: $emailError"
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