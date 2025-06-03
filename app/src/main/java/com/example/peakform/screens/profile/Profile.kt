package com.example.peakform.screens.profile

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.peakform.data.model.PopupStatus
import com.example.peakform.navigation.Screens
import com.example.peakform.ui.components.Popup
import com.example.peakform.utils.PrefManager
import com.example.peakform.viewmodel.VMProfile
import com.example.peakform.viewmodel.VMUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

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
    val photoUrl by profileViewModel.photoUrl.collectAsState()
    val user = userViewModel.user
    val coroutineScope = rememberCoroutineScope()
    val prefManager = PrefManager(navController.context)
    val context = LocalContext.current

    var showImagePicker by remember { mutableStateOf(false) }

    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            user?.id?.let { userId ->
                // Convert URI to File
                try {
                    val inputStream = context.contentResolver.openInputStream(selectedUri)
                    val tempFile = File(context.cacheDir, "temp_profile_${System.currentTimeMillis()}.jpg")
                    val outputStream = FileOutputStream(tempFile)

                    inputStream?.copyTo(outputStream)
                    inputStream?.close()
                    outputStream.close()

                    profileViewModel.uploadPhoto(userId, tempFile)
                } catch (e: Exception) {
                    Log.e("Profile", "Error converting URI to File: ${e.message}")
                }
            }
        }
    }

    LaunchedEffect(user) {
        Log.d("Profile", "User: $user")
        user?.id?.let {
            profileViewModel.getLog(it)
            profileViewModel.getPhoto(it) // Load existing photo
        }
    }

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

                // Reset success state after showing popup
                coroutineScope.launch {
                    delay(2000L)
                    profileViewModel.resetState()
                }
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

            // Profile Photo Section
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (photoUrl != null) {
                    // Display uploaded photo
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(photoUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Profile Photo",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            .clickable { showImagePicker = true },
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Default profile icon with upload option
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            .clickable { galleryLauncher.launch("image/*") }, // Menambahkan aksi klik untuk membuka galeri
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Default Profile",
                                modifier = Modifier.size(40.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Photo",
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
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
                            }
                        }
                    }
                }
            }
        }
    }
}