package com.example.peakform.utils

import android.content.Context
import androidx.core.content.edit

class PrefManager(context: Context) {
    private val pref = context.getSharedPreferences("user_pref", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        pref.edit() { putString("TOKEN_KEY", token) }
    }

    fun getToken(): String? {
        return pref.getString("TOKEN_KEY", null)
    }

    fun clearToken() {
        pref.edit() { remove("TOKEN_KEY") }
    }
}