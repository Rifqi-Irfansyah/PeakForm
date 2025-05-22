package com.example.peakform.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user_id")
    val id: String,
    val name: String,
    val email: String,
    val token: String,
    val points: Int
)
