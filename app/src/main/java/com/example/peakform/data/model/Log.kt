package com.example.peakform.data.model

import com.google.gson.annotations.SerializedName
import java.security.Timestamp

data class Log(
    val id: Int,
    val timestamp: Timestamp,
    val exercise: Exercise,
    val set: Int,
    val repetition: Int,
)

data class CreateLogRequest(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("exercise_id")
    val exerciseId: Int,
    val timestamp: String,
    val set: Int,
    val repetition: Int
)

data class LogResponse(
    val status: String,
    val message: String,
)