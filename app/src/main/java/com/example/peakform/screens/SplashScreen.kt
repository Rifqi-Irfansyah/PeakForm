package com.example.peakform.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.*
import com.example.peakform.R
import com.example.peakform.navigation.Screens
import com.example.peakform.utils.PrefManager
import com.example.peakform.viewmodel.VMUser
import com.example.peakform.api.AuthService
import androidx.compose.foundation.background
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController, vmUser: VMUser, prefManager: PrefManager) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.cat))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    val isLoading = remember { mutableStateOf(true) }
    val startDestination = remember { mutableStateOf(Screens.Auth.route) }

    LaunchedEffect(Unit) {
        val token = prefManager.getToken()
        if (token != null) {
            try {
                val response = AuthService.instance.getUser(token)
                if (response.isSuccessful) {
                    response.body()?.data?.let {
                        vmUser.updateUser(it)
                        startDestination.value = Screens.Home.route
                    } ?: prefManager.clearToken()
                } else {
                    prefManager.clearToken()
                }
            } catch (e: Exception) {
                prefManager.clearToken()
            }
        }
        delay(3000)
        navController.navigate(startDestination.value) {
            popUpTo(Screens.SplashScreen.route) { inclusive = true }
        }
        isLoading.value = false
    }

    if (isLoading.value) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(250.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "PeakForm",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}