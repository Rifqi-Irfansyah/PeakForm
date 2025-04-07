package com.example.peakform.api

import com.example.peakform.api.BASE_URL
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
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
    suspend fun getSchedule(@Query("UID") userId: String): Response<ResponseBody>

    companion object {
        val instance: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL.toHttpUrlOrNull()!!)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
