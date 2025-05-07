package com.example.peakform.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.peakform.Room.NotificationDao

class VMNotificationFactory(private val context: Context, private val notificationDao: NotificationDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VMNotification::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VMNotification(context, notificationDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}