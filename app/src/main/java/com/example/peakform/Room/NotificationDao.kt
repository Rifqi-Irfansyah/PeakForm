package com.example.peakform.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.peakform.data.model.Notification

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications")
    suspend fun getAll(): List<Notification>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: Notification)
}