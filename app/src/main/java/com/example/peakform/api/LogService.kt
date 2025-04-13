package com.example.peakform.api

import com.example.peakform.data.model.CreateLogRequest
import com.example.peakform.data.model.GenericResponse
import okhttp3.HttpUrl.Companion.toHttpUrl
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface LogService {
    @POST("logs/create")
    suspend fun createLog(@Body responseBody: CreateLogRequest): Response<GenericResponse>


    companion object {
        val instance: LogService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL.toHttpUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LogService::class.java)
        }
    }
}