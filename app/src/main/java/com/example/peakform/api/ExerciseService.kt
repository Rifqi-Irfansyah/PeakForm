package com.example.peakform.api

import com.example.peakform.data.model.ApiResponse
import com.example.peakform.data.model.Exercise
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import com.example.peakform.api.BASE_URL
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

interface ExerciseService {
    @GET("exercises")
    suspend fun getExercises(): Response<ApiResponse<List<Exercise>>>

    companion object {
        val instance: ExerciseService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL.toHttpUrlOrNull()!!)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ExerciseService::class.java)
        }

        fun getBaseUrlForImages(): String = "${BASE_URL}exercises/"
    }
}