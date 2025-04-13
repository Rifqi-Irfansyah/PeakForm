package com.example.peakform.utils

import android.content.Context
import androidx.core.content.edit
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PrefManager(context: Context) {
    private val pref = context.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
    private val exercisePref = context.getSharedPreferences("exercise_pref", Context.MODE_PRIVATE)
    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())


    fun saveToken(token: String) {
        pref.edit() { putString("TOKEN_KEY", token) }
    }

    fun getToken(): String? {
        return pref.getString("TOKEN_KEY", null)
    }

    fun clearToken() {
        pref.edit() { remove("TOKEN_KEY") }
    }

    fun setExerciseDone() {
        exercisePref.edit() {
            putBoolean("EXERCISE_KEY", true)
                .putString("LAST_EXERCISE_DATE", sdf.format(Date()))
        }
    }

    fun isHaveExercise(): Boolean {
        val lastDate = exercisePref.getString("LAST_EXERCISE_DATE", null)
        val today = sdf.format(Date())

        if (lastDate != today) {
            exercisePref.edit() {
                putBoolean("EXERCISE_KEY", false)
                    .putString("LAST_EXERCISE_DATE", today)
            }
        }

        return exercisePref.getBoolean("EXERCISE_KEY", false)
    }
}