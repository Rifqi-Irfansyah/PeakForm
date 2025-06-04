package com.example.peakform.screens.schedule

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.peakform.api.ExerciseService
import com.example.peakform.data.model.Exercises
import com.example.peakform.ui.theme.NavigationBarMediumTheme
import com.example.peakform.viewmodel.VMShowSchedule
import com.example.peakform.viewmodel.VMUser

@Composable
fun StartExercise(navController: NavController, userViewModel: VMUser,viewModel : VMShowSchedule){
    val selectedScheduleState = viewModel.selectedSchedule.collectAsState()
    val schedule = selectedScheduleState.value
    var currentIndex by remember { mutableStateOf(0) }
    var rest by remember { mutableStateOf(false) }
    val user = userViewModel.user

    LaunchedEffect(user) {
        user?.id?.let {
            viewModel.setUserId(it)
        }
    }

    NavigationBarMediumTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            if (schedule != null && currentIndex < schedule.exercise.size && !rest) {
                DoingExercise(
                    exercises = schedule.exercise[currentIndex],
                    onNext = {
                        currentIndex++
                        rest = true
                    },
                    viewModel = viewModel
                )
            }
            else if (schedule != null && currentIndex < schedule.exercise.size && rest){
                Rest(
                    exercises = schedule.exercise[currentIndex],
                    onNext = {
                        rest = false
                    }
                )
            }
            else{
                if (schedule != null) {
                    FinishExercise(navController, userViewModel)
                }
            }
        }
    }
}

@Composable
fun DoingExercise(exercises: Exercises, onNext: () -> Unit, viewModel: VMShowSchedule) {
    val context = LocalContext.current

    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        val fullImageUrl = exercises.image.let { "${ExerciseService.getBaseUrlForImages()}$it" }

        val request = ImageRequest.Builder(context)
            .data(fullImageUrl)
            .crossfade(true)
            .build()
        Box(modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .border(
                BorderStroke(5.dp, color = MaterialTheme.colorScheme.secondary),
                shape = RoundedCornerShape(21.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .weight(1.2f),
            contentAlignment = Alignment.Center
        ){
            SubcomposeAsyncImage(
                model = request,
                imageLoader = imageLoader,
                contentDescription = "Exercise Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(20.dp),
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.secondary)
                    )
                },
                error = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.errorContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Failed to load",
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 30.dp)
                .weight(1f),
            contentAlignment = Alignment.Center
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ){
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = exercises.name,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(2f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "x " + exercises.repetition.toString(),
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = exercises.set.toString() + " set",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(
                    modifier = Modifier
                        .width(200.dp)
                        .weight(1f),
                ){

                    Button(
                        modifier = Modifier
                            .width(200.dp)
                            .height(50.dp),
                        onClick = {
                            viewModel.createLog(
                                exercises
                            )
                            onNext()
                        }
                    ) {
                        Text(
                            text = "FINISH",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            letterSpacing = 2.sp
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun Rest(exercises: Exercises, onNext: () -> Unit){
    val context = LocalContext.current

    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()
    val fullImageUrl = exercises.image.let { "${ExerciseService.getBaseUrlForImages()}$it" }

    val request = ImageRequest.Builder(context)
        .data(fullImageUrl)
        .crossfade(true)
        .build()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubcomposeAsyncImage(
            model = request,
            imageLoader = imageLoader,
            contentDescription = "Exercise Image",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(20.dp)
                .weight(1.4f),
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.secondary)
                        .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                )
            },
            error = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.errorContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Failed to load",
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondary)
                .padding(16.dp)
                .weight(0.25f),
        ) {
            Text(
                text = "NEXT",
                fontSize = 18.sp,
                color = Color.White
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = exercises.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = exercises.set.toString() + " x " + exercises.repetition,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Rest",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = String.format("You Can Rest 20-60 seconds"),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(32.dp))

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNext,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                border = BorderStroke(2.dp, Color.White),
                modifier = Modifier
                    .width(180.dp)
                    .height(45.dp)
            ) {
                Text(
                    text = "NEXT",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}