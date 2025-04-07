package com.example.peakform.data.model

data class ApiResponse<T>(
    val status: String,
    val message: String,
    val data: T
)

data class Exercise(
    val Name: String,
    val Type: String,
    val Muscle: String,
    val Equipment: String,
    val Difficulty: String,
    val Instructions: String,
    val Image: String?
)