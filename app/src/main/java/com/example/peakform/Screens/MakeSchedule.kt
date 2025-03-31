package com.example.peakform.Screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.peakform.Navigation.Screens
import com.example.peakform.R
import com.example.peakform.ViewModel.VMMakeSchedule
import com.example.peakform.ui.theme.NavigationBarMediumTheme
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import com.airbnb.lottie.compose.*


@Composable
fun MakeSchedule(navController: NavController, viewModel: VMMakeSchedule = viewModel()){
    val questions = listOf(
        "What is your goal?" to listOf("Build Muscle", "Endurance"),
        "How many times do you exercise in a week?" to listOf("Never", "1-2 times", "3-7 times")
    )
    val optionImages = mapOf(
        "Endurance" to R.drawable.cardio,
        "Build Muscle" to R.drawable.muscle
    )
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedAnswers by remember { mutableStateOf(mutableMapOf<Int, String>()) }
    val (question, options) = questions[currentQuestionIndex]
    val isLoading by viewModel.loading.collectAsState()
    val isSuccess by viewModel.success.collectAsState()
    val errorMessage by viewModel.error.collectAsState()

    NavigationBarMediumTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                errorMessage?.let {
                    Popup(navController, isSuccess, it, isLoading)
                } ?: Popup(navController, isSuccess, "", isLoading)

                Button(
                    modifier = Modifier
                        .align(alignment = Alignment.Start),
                    onClick = {
                        if (currentQuestionIndex == 0) {
                            navController.popBackStack()
                        } else {
                            currentQuestionIndex--
                        }
                    }
                ) {
                    Text(if (currentQuestionIndex == 0) "Exit" else "Back")
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = question, fontSize = 20.sp, modifier = Modifier.padding(bottom = 10.dp))
                    options.forEach { option ->
                        Card(
                            modifier = Modifier
                                .padding(10.dp)
                                .clip(RoundedCornerShape(35.dp))
                                .clickable {
                                    selectedAnswers[currentQuestionIndex] = option
                                    if (currentQuestionIndex < questions.lastIndex) {
                                        currentQuestionIndex++
                                    } else {
                                        viewModel.submitAnswers(selectedAnswers)
                                    }
                                },
                            colors = CardDefaults.cardColors(containerColor = Color.LightGray),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .width(200.dp)
                                    .padding(vertical = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                    optionImages[option]?.let { imageRes ->
                                    Image(
                                        painter = painterResource(id = imageRes), // Ganti dengan ID gambar yang sesuai
                                        contentDescription = null,
                                        modifier = Modifier
                                            .width(150.dp)
                                            .height(150.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                Text(
                                    text = option,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                )
                            }
                        }
                    }
                }

            }

        }
    }
}

@Composable
fun Popup(navController: NavController, isSuccess: Boolean, isError: String, isLoading:Boolean){
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            delay(2500)
            navController.navigate(Screens.Home.route) {
                launchSingleTop = true
            }
        }
    }
    if (isLoading) {
        AlertDialog(
            onDismissRequest = { },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LottieAnimation(
                        modifier = Modifier.size(200.dp),
                        composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading)).value,
                        iterations = LottieConstants.IterateForever
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Please wait...", fontSize = 18.sp, color = Color.Black)
                }
            },
            confirmButton = { }
        )
    }
    if (isSuccess) {
        AlertDialog(
            onDismissRequest = { },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LottieAnimation(
                        modifier = Modifier.size(200.dp),
                        composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success)).value,
                        iterations = LottieConstants.IterateForever
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Success created schedule", fontSize = 18.sp, color = Color.Black)
                }
            },
            confirmButton = { }
        )
    }

    if (isError != "") {
        AlertDialog(
            onDismissRequest = { },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LottieAnimation(
                        modifier = Modifier.size(200.dp),
                        composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error)).value,
                        iterations = LottieConstants.IterateForever
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Error\n $isError", fontSize = 18.sp, color = Color.Black)
                }
            },
            confirmButton = { }
        )
    }
}

@Composable
@Preview
fun PreviewMakeSchedule(){
    Popup(navController = rememberNavController(), false,"", true)
//    MakeSchedule(navController = rememberNavController(), viewModel = viewModel())
}
