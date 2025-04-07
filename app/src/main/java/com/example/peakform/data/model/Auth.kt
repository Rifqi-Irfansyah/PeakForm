package com.example.peakform.data.model

import com.google.gson.annotations.SerializedName

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
    val email: String,
)

data class ChangePasswordRequest(
    val id: String,
    @SerializedName("old_password")
    val oldPassword: String,
    @SerializedName("new_password")
    val newPassword: String
)

data class GenericResponse(
    val message: String,
    val status: String,
)

data class VerifyRegisterRequest(
    val email: String,
    val otp: String,
    val name: String,
    val password: String
)

data class VerifyRegisterResponse(
    val message: String,
    val status: String,
)