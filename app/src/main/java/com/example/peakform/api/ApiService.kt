package com.example.peakform.api

import com.example.peakform.API.BASE_URL
import com.example.peakform.data.model.ChangeScheduleRequest
import com.example.peakform.data.model.ExerciseScheduleRequest
import com.example.peakform.data.model.ScheduleResponse
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query


interface ApiService {
    @POST("schedule")
    suspend fun createSchedule(@Body responseBody: MutableMap<String, Any>): Response<ResponseBody>
    @GET("schedule")
    suspend fun getSchedule(@Query("UID") userId: String): Response<ScheduleResponse>
    @PUT("schedule")
    suspend fun updateDaySchedule(@Body requestBody: ChangeScheduleRequest): Response<ResponseBody>
    @PUT("schedule/exercise")
    suspend fun updateExerciseSchedule(@Body requestBody: ExerciseScheduleRequest): Response<ResponseBody>
    @DELETE("schedule")
    suspend fun deleteSchedule(
        @Query ("id_schedule") idSchedule:String,
        @Query ("UID") idUser:String)
        : Response<ResponseBody>
    @DELETE("schedule/exercise")
    suspend fun deleteExerciseSchedule(
        @Query("id_schedule") idSchedule:String,
        @Query("id_exercise") idExercise:String)
        : Response<ResponseBody>

    companion object {
        val instance: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL.toHttpUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
