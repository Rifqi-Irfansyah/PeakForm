package com.example.peakform.api

import com.example.peakform.data.model.StatsResponse
import okhttp3.HttpUrl.Companion.toHttpUrl
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface StatsService {
    @GET("/stats/{id}")
    suspend fun getUserStats(@Path("id") id: String): Response<StatsResponse>
    companion object {
        val instance: StatsService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL.toHttpUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(StatsService::class.java)
        }
    }
}
