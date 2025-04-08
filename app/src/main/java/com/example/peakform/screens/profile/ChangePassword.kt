package com.example.peakform.screens.profile

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.peakform.ui.components.PasswordTextField
import com.example.peakform.ui.components.Popup
import com.example.peakform.viewmodel.VMProfile
import com.example.peakform.viewmodel.VMUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ChangePassword(
    navController: NavController,
    profileViewModel: VMProfile = viewModel(),
    userViewModel: VMUser = viewModel(),
) {
    val oldPasswordState = remember { mutableStateOf("") }
    val newPasswordState = remember { mutableStateOf("") }
    val loading by profileViewModel.loading.collectAsState()
    val success by profileViewModel.success.collectAsState()
    val error by profileViewModel.error.collectAsState()
    val user = userViewModel.user
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        if (loading) {
            Popup(
                status = PopupStatus.Loading,
                popupMessage = "Loading..."
            )
        }
        if (success) {
            Log.d("Login", "Your password has been changed successfully: $user")
            user?.let { userViewModel.updateUser(it) }
            Popup(
                status = PopupStatus.Success,
                popupMessage = "Your password has been changed!"
            )

            coroutineScope.launch {
                delay(2000L)
                navController.navigate(Screens.Profile.route) {
                    popUpTo(0) { inclusive = true }
                }
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

        Text(
            text = "Change Password",
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Keep your account secure by updating your password regularly.",
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
            ),
            modifier = Modifier.padding(bottom = 32.dp)
        )
        PasswordTextField(
            value = oldPasswordState.value,
            onValueChange = { oldPasswordState.value = it },
            label = "Old Password",
        )
        Spacer(modifier = Modifier.height(8.dp))
        PasswordTextField(
            value = newPasswordState.value,
            onValueChange = { newPasswordState.value = it },
            label = "New Password",
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                profileViewModel.changePassword(
                    user?.id.toString(),
                    oldPasswordState.value,
                    newPasswordState.value
                )
            },
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading
        ) {
            Text(
                "Change Password",
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}