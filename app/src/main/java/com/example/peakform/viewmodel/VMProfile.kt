package com.example.peakform.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peakform.api.AuthService
import com.example.peakform.api.PhotoProfileService
import com.example.peakform.data.model.ChangePasswordRequest
import com.example.peakform.data.model.GenericResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.File

class VMProfile : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _logs = MutableStateFlow<List<com.example.peakform.data.model.Log>>(emptyList())
    val logs: StateFlow<List<com.example.peakform.data.model.Log>> = _logs

    private val _photoUrl = MutableStateFlow<String?>(null)
    val photoUrl: StateFlow<String?> = _photoUrl

    private val photoService = PhotoProfileService.instance

    fun resetState() {
        _loading.value = false
        _success.value = false
        _error.value = null
    }

    fun setPhotoUrl(url: String?) {
        _photoUrl.value = url
        if (url != null) {
            _success.value = true
            _error.value = null
        }
    }

    fun setError(message: String) {
        _error.value = message
        _success.value = false
    }

    fun changePassword(id: String, oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val changePasswordRequest = ChangePasswordRequest(id, oldPassword, newPassword)
                val response: Response<GenericResponse> = AuthService.instance.changePassword(changePasswordRequest)
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

    fun uploadUserPhoto(userId: String, file: File, onResult: (Boolean, String?) -> Unit) {
        if (userId.isBlank()) {
            onResult(false, "User ID is required")
            return
        }
        val allowedExt = listOf(".jpg", ".jpeg", ".png")
        val ext = ".${file.extension.lowercase()}"
        if (!allowedExt.contains(ext)) {
            onResult(false, "Only JPG, JPEG, or PNG files are allowed")
            return
        }

        val maxFileSizeBytes = 20 * 1024 * 1024
        if (file.length() > maxFileSizeBytes) {
            onResult(false, "File size exceeds 10MB limit")
            return
        }

        _loading.value = true
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val photoPart = MultipartBody.Part.createFormData("photo", file.name, requestFile)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("VMProfile", "Uploading file: ${file.name} with ext: $ext, size: ${file.length()} bytes")
                val response = photoService.uploadPhoto(userId, photoPart)
                withContext(Dispatchers.Main) {
                    Log.d("VMProfile", "Response code: ${response.code()}, Body: ${response.body()}")
                    if (response.isSuccessful && response.body()?.status == "success") {
                        val url = response.body()?.data?.url
                        _photoUrl.value = url
                        _success.value = true
                        _error.value = null
                        onResult(true, url)
                    } else {
                        val errorMessage = response.errorBody()?.string()?.let { parseErrorMessage(it) } ?: "Unknown error"
                        _error.value = errorMessage
                        onResult(false, errorMessage)
                    }
                    _loading.value = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.value = e.message ?: "Network error"
                    onResult(false, e.message)
                    _loading.value = false
                }
            }
        }
    }

    fun getUserPhoto(userId: String, onResult: (Boolean, String?) -> Unit) {
        if (userId.isBlank()) {
            onResult(false, "User ID is required")
            return
        }

        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = photoService.getPhoto(userId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body?.status == "success") {
                            val url = body.data?.url
                            _photoUrl.value = url
                            _error.value = null
                            onResult(true, url)
                        } else if (body?.status == "error" && body.message == "Photo not found") {
                            _photoUrl.value = null
                            _error.value = null
                            onResult(true, null)
                        } else {
                            val errorMessage = body?.message ?: "Unknown error"
                            _error.value = errorMessage
                            onResult(false, errorMessage)
                        }
                    } else {
                        val errorMessage = response.errorBody()?.string()?.let { parseErrorMessage(it) } ?: "Fetch failed: ${response.code()}"
                        _error.value = errorMessage
                        onResult(false, errorMessage)
                    }
                    _loading.value = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.value = e.message ?: "Network error"
                    onResult(false, e.message)
                    _loading.value = false
                }
            }
        }
    }

    private fun parseErrorMessage(json: String): String {
        return try {
            val jsonObject = JSONObject(json)
            val details = jsonObject.optString("details", "No details provided")
            val message = jsonObject.optString("message", "No message provided")
            val status = jsonObject.optString("status", "No status provided")
            "($status) $message\n$details"
        } catch (_: JSONException) {
            "Unknown error"
        }
    }
}