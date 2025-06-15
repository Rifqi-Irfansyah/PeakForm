package com.example.peakform.screens.profile

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import com.example.peakform.api.PhotoProfileService
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
@Composable
fun Profile(
    navController: NavController,
    profileViewModel: VMProfile = viewModel(),
    userViewModel: VMUser = viewModel()
) {
    val loading by profileViewModel.loading.collectAsState()
    val photoUrl by profileViewModel.photoUrl.collectAsState()
    val user = userViewModel.user
    val coroutineScope = rememberCoroutineScope()
    val prefManager = PrefManager(navController.context)
    val context = LocalContext.current

    LaunchedEffect(user?.id) {
        user?.id?.let { userId ->
            profileViewModel.getUserPhoto(userId) { isSuccess, filename ->
                if (isSuccess && filename != null) {
                    profileViewModel.setPhotoUrl(filename)
                }
            }
        }
    }

    var showUploadPopup by remember { mutableStateOf(false) }
    var uploadPopupStatus by remember { mutableStateOf<PopupStatus>(PopupStatus.Loading) }
    var uploadPopupMessage by remember { mutableStateOf("") }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            user?.id?.let { userId ->
                try {
                    val inputStream = context.contentResolver.openInputStream(selectedUri)
                    val tempFile = File(context.cacheDir, "temp_profile_${System.currentTimeMillis()}.jpg")
                    val outputStream = FileOutputStream(tempFile)
                    inputStream?.copyTo(outputStream)
                    inputStream?.close()
                    outputStream.close()

                    showUploadPopup = true
                    uploadPopupStatus = PopupStatus.Loading
                    uploadPopupMessage = "Uploading..."

                    profileViewModel.uploadUserPhoto(userId, tempFile) { isSuccess, filenameOrMessage ->
                        coroutineScope.launch {
                            if (isSuccess && filenameOrMessage != null) {
                                profileViewModel.setPhotoUrl(PhotoProfileService.getBaseUrlForPhoto() + filenameOrMessage)
                                uploadPopupStatus = PopupStatus.Success
                                uploadPopupMessage = "Photo uploaded successfully!"
                            } else {
                                uploadPopupStatus = PopupStatus.Error
                                uploadPopupMessage = filenameOrMessage ?: "Failed to upload photo"
                            }

                            delay(2000L)
                            showUploadPopup = false
                            profileViewModel.resetState()
                        }
                    }
                } catch (e: Exception) {
                    profileViewModel.setError("Failed to process file: ${e.message}")
                    showUploadPopup = true
                    uploadPopupStatus = PopupStatus.Error
                    uploadPopupMessage = "Failed to process file: ${e.message}"
                    coroutineScope.launch {
                        delay(3000L)
                        showUploadPopup = false
                    }
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            if (showUploadPopup) {
                Popup(
                    status = uploadPopupStatus,
                    popupMessage = uploadPopupMessage
                )
            }

            Box(
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (photoUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(photoUrl)
                            .crossfade(true)
                            .size(150, 150)
                            .build(),
                        contentDescription = "Profile Photo",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            .clickable { galleryLauncher.launch("image/*") },
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            .clickable { galleryLauncher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Default Profile",
                                modifier = Modifier.size(50.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Photo",
                                modifier = Modifier.size(25.dp),
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
                    text = "Points: ${it.point}",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 20.sp
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Streak: ${it.streak}",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 20.sp
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(Screens.ChangePassword.route) },
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading
            ) {
                Text(
                    text = "Change Password",
                    color = MaterialTheme.colorScheme.onPrimary
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
                    text = "Logout",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
