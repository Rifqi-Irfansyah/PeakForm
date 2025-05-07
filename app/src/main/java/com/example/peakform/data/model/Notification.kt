package com.example.peakform.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dayOfWeek: Int,
    val hour: Int,
    val minute: Int
)