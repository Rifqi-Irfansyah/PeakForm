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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.peakform.ui.components.TextFieldWithIcon
import com.example.peakform.viewmodel.auth.VMRegister
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun VerifyRegister(navController: NavController, viewModel: VMRegister = viewModel(), email: String) {
    val otpState = rememberSaveable { mutableStateOf("") }
    val usernameState = rememberSaveable { mutableStateOf("") }
    val passwordState = rememberSaveable { mutableStateOf("") }
    val emailState = rememberSaveable { mutableStateOf(email) }
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
                popupMessage = "Registration successful!"
            )

            coroutineScope.launch {
                delay(2000L)
                navController.navigate(Screens.Auth.route)
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
            text = "Complete Your Registration",
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Enter the OTP sent to your emai and set up",
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
            ),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "your account details to get started!",
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
            ),
            modifier = Modifier.padding(bottom = 32.dp)
        )
        TextFieldWithIcon(
            value = otpState.value,
            onValueChange = { otpState.value = it },
            label = "OTP",
            icon = Icons.Filled.Lock,
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextFieldWithIcon(
            value = usernameState.value,
            onValueChange = { usernameState.value = it },
            label = "Username",
            icon = Icons.Filled.Person,
        )
        Spacer(modifier = Modifier.height(16.dp))
        PasswordTextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = "Password",
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.verifyRegister(emailState.value, otpState.value, usernameState.value, passwordState.value) },
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading
        ) {
            Text(
                "Complete Registration",
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}