package com.example.peakform.api

import com.example.peakform.data.model.ApiResponse
import com.example.peakform.data.model.User
import okhttp3.HttpUrl.Companion.toHttpUrl
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("/users/{userId}")
    suspend fun getUser(
        @Path("userId") userId: String,
    ): Response<ApiResponse<User>>

    companion object {
        val instance: UserService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL.toHttpUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UserService::class.java)
        }
    }
}