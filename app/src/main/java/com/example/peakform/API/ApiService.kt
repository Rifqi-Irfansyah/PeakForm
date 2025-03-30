package com.example.peakform.API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {
    @POST("schedule")
    suspend fun createSchedule(@Body responseBody: MutableMap<String, Any>): Response<ResponseBody>

    companion object {
        val instance: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl("http://" + "192.168.4.198" + ":3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
