package com.example.peakform.api

import com.example.peakform.api.BASE_URL
import com.example.peakform.data.model.AuthRequest
import com.example.peakform.data.model.AuthResponse
import com.example.peakform.data.model.ChangePasswordRequest
import com.example.peakform.data.model.EmailRequest
import com.example.peakform.data.model.GenericResponse
import com.example.peakform.data.model.ResetPasswordRequest
import com.example.peakform.data.model.VerifyRegisterRequest
import com.example.peakform.data.model.VerifyRegisterResponse
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface AuthService {
    @POST("auth")
    suspend fun auth(@Body responseBody: AuthRequest): Response<AuthResponse>

    @POST("register")
    suspend fun register(@Body responseBody: EmailRequest): Response<GenericResponse>

    @POST("register/verify-otp")
    suspend fun verifyRegister(@Body responseBody: VerifyRegisterRequest): Response<VerifyRegisterResponse>

    @POST("change-password")
    suspend fun changePassword(@Body responseBody: ChangePasswordRequest): Response<GenericResponse>

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body responseBody: EmailRequest): Response<GenericResponse>

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body responseBody: ResetPasswordRequest): Response<GenericResponse>

    @GET("/auth/user/{token}")
    suspend fun getUser(
        @Path("token") token: String,
    ): Response<AuthResponse>

    companion object {
        val instance: AuthService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL.toHttpUrlOrNull()!!)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AuthService::class.java)
        }
    }
}
