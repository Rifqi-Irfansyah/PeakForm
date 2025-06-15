package com.example.peakform.api
import com.example.peakform.data.model.ApiResponse
import com.example.peakform.data.model.StreakResponse
import okhttp3.HttpUrl.Companion.toHttpUrl
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface StreakService {
    @PUT("/streak/update/{userId}")
    suspend fun updateStreak(
        @Path("userId") userId: String,
    ): Response<StreakResponse>

    @GET("/streak/check/{userId}")
    suspend fun checkStreak(
        @Path("userId") userId: String,
    ): Response<ApiResponse<Int>>

    companion object {
        val instance: StreakService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL.toHttpUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(StreakService::class.java)
        }
    }
}