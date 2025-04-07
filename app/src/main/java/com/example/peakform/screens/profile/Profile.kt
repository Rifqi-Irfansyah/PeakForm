package com.example.peakform.screens.profile

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.peakform.ui.components.Popup
import com.example.peakform.viewmodel.VMProfile
import com.example.peakform.viewmodel.VMUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Profile(navController: NavController, profileViewModel: VMProfile = viewModel(), userViewModel: VMUser = viewModel()) {
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
        }
        Spacer(modifier = Modifier.height(16.dp))
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
            onClick = { navController.navigate(Screens.Auth.route) {
                popUpTo(0) { inclusive = true }
            } },
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading
        ) {
            Text(
                "Logout",
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}