package com.example.peakform.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.peakform.Room.NotificationDao
import com.example.peakform.data.model.Notification
import com.example.peakform.notification.scheduleDailyNotification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class VMNotification(private val context: Context, private val notificationDao: NotificationDao) : ViewModel() {
    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications.asStateFlow()
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

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

                        scheduleDailyNotification(context)
                    }
            } catch (e: Exception) {
                Log.e("VMNotification", "Exception in init: $e")
            }
        }
    }

    fun insertNotification(notification: Notification) {
        viewModelScope.launch {
            try {
                notificationDao.insert(notification)
                Log.d("VMNotification", "Inserted notification: $notification")
            } catch (e: Exception) {
                Log.e("VMNotification", "Error inserting notification: $e")
            }
        }
    }

    fun updateNotificationByDay(day: Int, hour: Int, minute: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                notificationDao.updateNotificationByDay(day, hour, minute)
                Log.d("VMNotification", "Updated notification for day: $day")
                _success.value = true
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("VMNotification", "Error updating notification: $e")
            }
        }
    }

    fun deleteNotificationByDay(day: Int) {
        viewModelScope.launch {
            try {
                notificationDao.deleteByDay(day)
                Log.d("VMNotification", "Deleted notification for day: $day")
            } catch (e: Exception) {
                Log.e("VMNotification", "Error deleting notification: $e")
            }
        }
    }
}
