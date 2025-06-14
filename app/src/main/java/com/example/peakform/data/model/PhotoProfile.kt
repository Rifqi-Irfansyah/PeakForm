package com.example.peakform.data.model

data class UploadResponse(
    val status: String,
    val message: String,
    val data: PhotoData?
)

data class PhotoData(
    val url: String
)