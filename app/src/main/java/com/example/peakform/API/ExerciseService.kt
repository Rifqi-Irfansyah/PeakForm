package com.example.peakform.API

import com.example.peakform.data.model.ApiResponse
import com.example.peakform.data.model.Exercise
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ExerciseService {
    @GET("exercises")
    suspend fun getExercises(): Response<ApiResponse<List<Exercise>>>

    companion object {
        private const val BASE_URL = "http://192.168.20.48:3000/"

        val instance: ExerciseService by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofit.create(ExerciseService::class.java)
        }

        fun getBaseUrlForImages(): String = "$BASE_URL/exercises/"
    }
}