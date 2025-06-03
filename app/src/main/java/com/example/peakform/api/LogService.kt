package com.example.peakform.api

import com.example.peakform.data.model.CreateLogRequest
import com.example.peakform.data.model.GenericResponse
import com.example.peakform.data.model.GetLogResponse
import com.example.peakform.data.model.LeaderboardResponse
import okhttp3.HttpUrl.Companion.toHttpUrl
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LogService {
    @POST("logs/create")
    suspend fun createLog(@Body responseBody: CreateLogRequest): Response<GenericResponse>

    @GET("/leaderboard")
    suspend fun getLeaderboard(): Response<LeaderboardResponse>

    @GET("/logs/{userId}")
    suspend fun getLog(
        @Path("userId") id: String,
    ): Response<GetLogResponse>


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