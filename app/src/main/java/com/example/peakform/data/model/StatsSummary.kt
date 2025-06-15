package com.example.peakform.data.model

data class StatsSummary(
    val userId: String,
    val totalSets: Int,
    val totalReps: Int,
    val exerciseCounter: Map<String, Int>
)

data class StatsResponse(
    val status: String,
    val message: String,
    val data: StatsSummary
)
