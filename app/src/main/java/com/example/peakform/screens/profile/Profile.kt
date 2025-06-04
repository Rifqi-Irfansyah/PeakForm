package com.example.peakform.screens.profile

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.peakform.data.model.PopupStatus
import com.example.peakform.navigation.Screens
import com.example.peakform.ui.components.Popup
import com.example.peakform.utils.PrefManager
import com.example.peakform.viewmodel.VMProfile
import com.example.peakform.viewmodel.VMUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.material3.*
import androidx.compose.foundation.shape.RoundedCornerShape

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Profile(
    navController: NavController,
    profileViewModel: VMProfile = viewModel(),
    userViewModel: VMUser = viewModel()
) {
    val loading by profileViewModel.loading.collectAsState()
    val success by profileViewModel.success.collectAsState()
    val error by profileViewModel.error.collectAsState()
    val logs by profileViewModel.logs.collectAsState()
    val user = userViewModel.user
    val coroutineScope = rememberCoroutineScope()
    val prefManager = PrefManager(navController.context)

    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            if (loading) {
                Popup(
                    status = PopupStatus.Loading,
                    popupMessage = "Loading..."
                )
            }
            if (success) {
                Popup(
                    status = PopupStatus.Success,
                    popupMessage = "Profile loaded successfully!"
                )
            }
            if (error != null) {
                Popup(
                    status = PopupStatus.Error,
                    popupMessage = error ?: "An error occurred",
                )

                coroutineScope.launch {
                    delay(3000L)
                    profileViewModel.resetState()
                }
            }

            Text(
                text = "Profile",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Log.d("Profile", "User: $user")
            user?.let {
                Text(
                    text = it.name,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 20.sp
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = it.email,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 20.sp
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = it.point.toString(),
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 20.sp
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = it.streak.toString(),
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 20.sp
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Log.d("Profile", "Logs: $logs")

            Button(
                onClick = { navController.navigate(Screens.ChangePassword.route) },
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading
            ) {
                Text(
                    "Change Password",
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }

            Button(
                onClick = {
                    prefManager.clearToken()
                    navController.navigate(Screens.Auth.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading
            ) {
                Text(
                    "Logout",
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (logs.isNotEmpty()) {
                Text(
                    text = "Exercise Logs:",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                logs.forEach { log ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        shape = RoundedCornerShape(8.dp),
                        shadowElevation = 4.dp,
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Exercise: ${log.exercise.name}",
                                    style = TextStyle(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 16.sp
                                    )
                                )
                                Text(
                                    text = "Set: ${log.set}",
                                    style = TextStyle(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 16.sp
                                    )
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Reps: ${log.repetition}",
                                    style = TextStyle(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 16.sp
                                    )
                                )
//                                Text(
//                                    text = "Date: ${formatTimestamp(log.timestamp)}",
//                                    style = TextStyle(
//                                        color = MaterialTheme.colorScheme.onSurface,
//                                        fontSize = 16.sp
//                                    )
//                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

//@RequiresApi(Build.VERSION_CODES.O)
//fun formatTimestamp(timestamp: String): String {
//    return try {
//        val parsedDate = LocalDateTime.parse(timestamp)
//        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
//        parsedDate.format(formatter)
//    } catch (e: Exception) {
//        timestamp
//    }
//}