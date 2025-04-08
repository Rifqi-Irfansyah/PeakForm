package com.example.peakform

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

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefManager = PrefManager(this)
        val token = prefManager.getToken()

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
                        } catch (_: Exception) {
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
                            if (startDestination.value == Screens.Home.route) {
                                BottomNavBar(navController)
                            }
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