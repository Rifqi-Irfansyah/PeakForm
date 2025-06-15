package com.example.peakform.api

import com.example.peakform.data.model.UploadResponse
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.Part
import retrofit2.http.Path

interface PhotoProfileService {

    @Multipart
    @POST("users/{id}/photo")
    suspend fun uploadPhoto(
        @Path("id") userId: String,
        @Part file: MultipartBody.Part
    ): Response<UploadResponse>

    @GET("users/{id}/photo")
    suspend fun getPhoto(
        @Path("id") userId: String
    ): Response<UploadResponse>

    companion object {
      val instance: PhotoProfileService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL.toHttpUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PhotoProfileService::class.java)
        }
        fun getBaseUrlForPhoto(): String = "${BASE_URL}users/static/photo/"
    }
}