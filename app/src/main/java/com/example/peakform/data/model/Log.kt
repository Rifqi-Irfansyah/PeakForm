package com.example.peakform.data.model

import com.google.gson.annotations.SerializedName

data class Log(
    @SerializedName("ID")
    val id: Int,
    @SerializedName("UserID")
    val userId: String,
    @SerializedName("exerciseID")
    val exerciseId: Int,
    @SerializedName("Timestamp")
    val timestamp: String,
    @SerializedName("Exercise")
    val exercise: Exercise,
    @SerializedName("Set")
    val set: Int,
    @SerializedName("Repetition")
    val repetition: Int,
)

data class GetLogResponse(
    val status: String,
    val message: String,
    val data: List<Log>
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