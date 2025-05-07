package com.example.peakform.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.peakform.data.model.Notification
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications")
    fun getAll(): List<Notification>

    @Query("SELECT * FROM notifications")
    fun getAllFlow(): Flow<List<Notification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: Notification)

    @Query("DELETE FROM notifications")
    suspend fun deleteAll()

    @Query("UPDATE notifications SET hour = :hour, minute = :minute WHERE dayOfWeek = :day")
    suspend fun updateNotificationByDay(day: Int, hour: Int, minute: Int)

    @Query("DELETE FROM notifications WHERE dayOfWeek = :day")
    suspend fun deleteByDay(day: Int)
}