package com.example.peakform.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.peakform.Room.NotificationDao
import com.example.peakform.data.model.Notification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class VMNotification(private val notificationDao: NotificationDao) : ViewModel() {
    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications.asStateFlow()

    init {
        Log.d("VMNotification", "ViewModel initialized")
        viewModelScope.launch {
            try {
                notificationDao.getAllFlow()
                    .catch { e ->
                        Log.e("VMNotification", "Error collecting notifications: $e")
                        _notifications.value = emptyList()
                    }
                    .collect { notifications ->
                        Log.d("VMNotification", "Notifications fetched: $notifications")
                        _notifications.value = notifications
                    }
            } catch (e: Exception) {
                Log.e("VMNotification", "Exception in init: $e")
            }
        }
    }

    // Add a sample notification for testing
    fun addSampleNotification() {
        viewModelScope.launch {
            try {
                val notification = Notification(dayOfWeek = 1, hour = 10, minute = 30)
                notificationDao.insert(notification)
                Log.d("VMNotification", "Sample notification inserted: $notification")
            } catch (e: Exception) {
                Log.e("VMNotification", "Error inserting notification: $e")
            }
        }
    }
}
