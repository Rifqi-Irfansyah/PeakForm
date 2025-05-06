package com.example.peakform.notification

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.Calendar
import java.util.concurrent.TimeUnit

fun scheduleDailyNotification(context: Context) {
    val targetHour = 23
    val targetMinute = 22
    val calendar = Calendar.getInstance()
    val currentTimeInMillis = System.currentTimeMillis()
    calendar.set(Calendar.HOUR_OF_DAY, targetHour)
    calendar.set(Calendar.MINUTE, targetMinute)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    if (calendar.timeInMillis < currentTimeInMillis) {
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }
    val delayInMillis = calendar.timeInMillis - currentTimeInMillis

    val data = workDataOf(
        "title" to "Workout Time!",
        "message" to "Your workout is waiting — let’s crush those goals today!"
    )
    val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInputData(data)
        .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)
}