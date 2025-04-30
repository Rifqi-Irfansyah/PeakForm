package com.example.peakform.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    val status: String,
    val message: String,
    val data: T
)

data class Exercise(
    @SerializedName("ID")
    val id: Int,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Type")
    val type: String,
    @SerializedName("Muscle")
    val muscle: String,
    @SerializedName("Equipment")
    val equipment: String,
    @SerializedName("Difficulty")
    val difficulty: String,
    @SerializedName("Instructions")
    val instructions: String,
    @SerializedName("Image")
    val image: String?
)