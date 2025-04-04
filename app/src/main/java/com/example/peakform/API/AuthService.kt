package com.example.peakform.API

import com.example.peakform.data.model.AuthRequest
import com.example.peakform.data.model.AuthResponse
import com.example.peakform.data.model.RegisterRequest
import com.example.peakform.data.model.RegisterResponse
import com.example.peakform.data.model.VerifyRegisterRequest
import com.example.peakform.data.model.VerifyRegisterResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthService {
    @POST("auth")
    suspend fun auth(@Body responseBody: AuthRequest): Response<AuthResponse>

    @POST("register")
    suspend fun register(@Body responseBody: RegisterRequest): Response<RegisterResponse>

    @POST("register/verify-otp")
    suspend fun verifyRegister(@Body responseBody: VerifyRegisterRequest): Response<VerifyRegisterResponse>

    companion object {
        val instance: AuthService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AuthService::class.java)
        }
    }
}
