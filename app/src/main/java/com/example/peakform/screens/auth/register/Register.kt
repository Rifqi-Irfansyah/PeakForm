package com.example.peakform.screens.auth.register

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.peakform.navigation.Screens
import com.example.peakform.viewmodel.auth.VMRegister
import com.example.peakform.data.model.PopupStatus
import com.example.peakform.ui.components.Popup
import com.example.peakform.ui.components.TextFieldWithIcon
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Register(navController: NavController, viewModel: VMRegister = viewModel()) {
    val emailState = remember { mutableStateOf("") }
    val loading by viewModel.loading.collectAsState()
    val success by viewModel.success.collectAsState()
    val error by viewModel.error.collectAsState()
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
                popupMessage = "Verification email sent!"
            )

            coroutineScope.launch {
                delay(2000L)
                navController.navigate(Screens.VerifyRegister.passEmail(emailState.value))
            }
        }
        if (error != null) {
            Popup(
                status = PopupStatus.Error,
                popupMessage = error ?: "An error occurred",
            )

            coroutineScope.launch {
                delay(3000L)
                viewModel.resetState()
            }

        }

        Text(
            text = "Join PeakForm Today!",
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Sign up with your email to get started",
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
            ),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "on your exercise journey",
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
            ),
            modifier = Modifier.padding(bottom = 32.dp)
        )
        TextFieldWithIcon(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = "Email",
            icon = Icons.Filled.Mail,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.sendVerificationEmail(emailState.value) },
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading
        ) {
            Text(
                "Verify Email",
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(
            onClick = { navController.navigate(Screens.Auth.route) }
        ) {
            Text("Already have an account? Login")
        }
    }
}

