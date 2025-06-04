package com.example.peakform.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peakform.api.AuthService
import com.example.peakform.api.PhotoProfileService
import com.example.peakform.data.model.ChangePasswordRequest
import com.example.peakform.data.model.GenericResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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

    // Tambahkan state untuk URL foto
    private val _photoUrl = MutableStateFlow<String?>(null)
    val photoUrl: StateFlow<String?> = _photoUrl

    private val photoService = PhotoProfileService.instance

    fun resetState() {
        _loading.value = false
        _success.value = false
        _error.value = null
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

    fun uploadPhoto(userId: String, file: File) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("photo", file.name, requestFile)

                val response = photoService.uploadPhoto(userId, body)
                if (response.isSuccessful) {
                    _photoUrl.value = response.body()?.data?.url
                    _success.value = true
                } else {
                    _error.value = response.errorBody()?.string()?.let { parseErrorMessage(it) } ?: "Upload failed: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                Log.e("VMProfile", "Upload error", e)
            } finally {
                _loading.value = false
            }
        }
    }

    fun getPhoto(userId: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val response = photoService.getPhoto(userId)
                if (response.isSuccessful) {
                    _photoUrl.value = response.body()?.data?.url
                } else {
                    _error.value = response.errorBody()?.string()?.let { parseErrorMessage(it) } ?: "Fetch failed: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                Log.e("VMProfile", "Get photo error", e)
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