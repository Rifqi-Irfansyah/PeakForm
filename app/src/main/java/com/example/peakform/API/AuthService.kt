package com.example.peakform.API

import com.example.peakform.data.model.AuthRequest
import com.example.peakform.data.model.AuthResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthService {
    @POST("auth")
    suspend fun auth(@Body responseBody: AuthRequest): Response<AuthResponse>

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
