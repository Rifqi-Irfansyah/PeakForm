package com.example.peakform

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.peakform.api.AuthService
import com.example.peakform.navigation.AppNavHost
import com.example.peakform.navigation.BottomNavBar
import com.example.peakform.navigation.Screens
import com.example.peakform.ui.theme.NavigationBarMediumTheme
import com.example.peakform.ui.theme.PeakFormTheme
import com.example.peakform.utils.PrefManager
import com.example.peakform.viewmodel.VMUser
import android.Manifest
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.peakform.notification.scheduleDailyNotification
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefManager = PrefManager(this)
        val token = prefManager.getToken()

        createNotificationChannel()
        checkNotificationPermissionIfNeeded()

        setContent {
            NavigationBarMediumTheme {
                val navController = rememberNavController()
                val VMUser: VMUser = viewModel()

                val isLoading = remember { mutableStateOf(true) }
                val startDestination = remember { mutableStateOf(Screens.Auth.route) }

                LaunchedEffect(Unit) {
                    if (token != null) {
                        try {
                            val response = AuthService.instance.getUser(token)
                            if (response.isSuccessful) {
                                response.body()?.data?.let {
                                    VMUser.updateUser(it)
                                    startDestination.value = Screens.Home.route
                                } ?: prefManager.clearToken()
                            } else {
                                prefManager.clearToken()
                            }
                        } catch (e: Exception) {
                            prefManager.clearToken()
                        }
                    }
                    isLoading.value = false
                }

                if (isLoading.value) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                    }
                } else {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = MaterialTheme.colorScheme.background,
                        bottomBar = {
                            BottomNavBar(navController)
                        }
                    ) { paddingValues ->
                        AppNavHost(
                            navController = navController,
                            VMUser = VMUser,
                            startDestination = startDestination.value,
                            modifier = Modifier.padding(paddingValues),
                        )
                    }
                }
            }
        }

        lifecycleScope.launch {
            try {
                scheduleDailyNotification(applicationContext)
            } catch (e: Exception) {
                Log.e("MainActivity", "Error scheduling notification", e)
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "daily_reminder_channel"
            val name = "Daily Reminder"
            val descriptionText = "Channel for daily notification"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun checkNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_NOTIFICATION_PERMISSION
                )
            }
        }
    }

    companion object {
        private const val REQUEST_NOTIFICATION_PERMISSION = 1001
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PeakFormTheme {
        Greeting("Android")
    }
}