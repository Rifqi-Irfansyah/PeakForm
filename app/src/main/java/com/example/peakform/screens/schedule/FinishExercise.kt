package com.example.peakform.screens.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.peakform.R
import com.example.peakform.ui.theme.NavigationBarMediumTheme
import com.example.peakform.viewmodel.VMFinishExercise
import com.example.peakform.viewmodel.VMUser

@Composable
fun FinishExercise(navController: NavController, userViewModel: VMUser,viewModel : VMFinishExercise = viewModel()) {
    val user = userViewModel.user
    
    LaunchedEffect(user) {
        user?.id?.let {
            viewModel.setUserId(it)
        }
    }
    
    NavigationBarMediumTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primary
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 100.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    LottieAnimation(
                        modifier = Modifier
                            .fillMaxWidth(0.9f),
                        composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.congrats)).value,
                        iterations = LottieConstants.IterateForever
                    )
                    Text(
                        text = "Congratulations, you have \n finished today's exercise",
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                    Button(
                        colors = ButtonDefaults.buttonColors(Color.White),
                        onClick = {
                            viewModel.updateStreak()
                            userViewModel.fetchUser(user?.id ?: "")
                            navController.popBackStack()
                        }
                    ){
                        Text(
                            text = "Back to Home",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

    }
}