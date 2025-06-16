package com.example.peakform.data.model

import com.google.gson.annotations.SerializedName

data class StatsSummary(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("total_sets")
    val totalSets: Int,
    @SerializedName("total_repetitions")
    val totalReps: Int,
    @SerializedName("exercise_counter")
    val exerciseCounter: Map<String, Int>
)

data class StatsResponse(
    val status: String,
    val message: String,
    val data: StatsSummary
)
