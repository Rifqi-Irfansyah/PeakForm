package com.example.peakform.screens.auth

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.peakform.viewmodel.VMUser
import com.example.peakform.viewmodel.auth.VMLogin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Login(navController: NavController, loginViewModel: VMLogin = viewModel(), userViewModel: VMUser = viewModel()) {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val loading by loginViewModel.loading.collectAsState()
    val success by loginViewModel.success.collectAsState()
    val error by loginViewModel.error.collectAsState()
    val user by loginViewModel.user.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

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
            Log.d("Login", "Login successful: $user")
            user?.let { userViewModel.updateUser(it) }
            Popup(
                status = PopupStatus.Success,
                popupMessage = "Login successful!"
            )

            coroutineScope.launch {
                delay(2000L)
                navController.navigate(Screens.Home.route) {
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
                loginViewModel.resetState()
            }

        }

        Text(
            text = "PeakForm",
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Welcome back! Please login to your account.",
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
        Spacer(modifier = Modifier.height(8.dp))
        PasswordTextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = "Password"
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { loginViewModel.login(emailState.value, passwordState.value, context) },
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading
        ) {
            Text(
                "Login",
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(
            onClick = { navController.navigate(Screens.ForgetPassword.route) },
            modifier = Modifier.height(34.dp),
        ) {
            Text("Forget your password? Recover")
        }
        TextButton(
            onClick = { navController.navigate(Screens.Register.route) },
            modifier = Modifier.height(34.dp),
        ) {
            Text("Don't have an account? Sign up")
        }
    }
}
