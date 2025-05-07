package com.example.peakform.notification

import android.content.Context
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.peakform.Room.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.concurrent.TimeUnit

suspend fun scheduleDailyNotification(context: Context) {
    val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1

    val dao = AppDatabase.getInstance(context).notificationDao()
    val todayNotifs = withContext(Dispatchers.IO) {
        dao.getAll().filter { it.dayOfWeek == today }
    }
    val now = Calendar.getInstance()

    val upcomingNotif = todayNotifs.firstOrNull { notif ->
        val notifTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, notif.hour)
            set(Calendar.MINUTE, notif.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        notifTime.timeInMillis > now.timeInMillis
    }

    if (upcomingNotif != null) {
        val targetTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, upcomingNotif.hour)
            set(Calendar.MINUTE, upcomingNotif.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val delay = targetTime.timeInMillis - System.currentTimeMillis()

        val data = workDataOf(
            "title" to "Workout Time!",
            "message" to "Your workout is waiting — let’s crush those goals today!"
        )

        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInputData(data)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }
}