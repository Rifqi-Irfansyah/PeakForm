package com.example.peakform.API

import com.example.peakform.data.model.ScheduleResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {
    @POST("schedule")
    suspend fun createSchedule(@Body responseBody: MutableMap<String, Any>): Response<ResponseBody>

    @GET("schedule")
    suspend fun getSchedule(@Query("UID") userId: String): Response<ScheduleResponse>

    companion object {
        val instance: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
