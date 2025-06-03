package com.example.peakform.data.model

import com.google.gson.annotations.SerializedName

data class PhotoResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: PhotoData? = null
)

data class PhotoData(
    @SerializedName("url") val url: String
)