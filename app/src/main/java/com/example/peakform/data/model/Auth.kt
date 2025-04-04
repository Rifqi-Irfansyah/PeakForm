package com.example.peakform.data.model

data class AuthRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val status: String,
    val message: String,
    val data: User
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class RegisterResponse(
    val message: String,
    val status: String,
)