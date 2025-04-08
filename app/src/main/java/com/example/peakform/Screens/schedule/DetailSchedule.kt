package com.example.peakform.screens.schedule

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.peakform.viewmodel.VMShowSchedule
import com.example.peakform.data.model.Exercises
import com.example.peakform.data.model.Schedule
import com.example.peakform.data.model.ScheduleData
import com.example.peakform.data.model.getDayName
import com.example.peakform.navigation.Screens
import com.example.peakform.ui.components.CardExerciseSchedule
import com.example.peakform.ui.theme.NavigationBarMediumTheme
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailSchedule(navController: NavController, viewModel : VMShowSchedule){
    val selectedScheduleState = viewModel.selectedSchedule.collectAsState()
    val schedule = selectedScheduleState.value
    val today = LocalDate.now().dayOfWeek.value // Monday = 1, Sunday = 7
    val isToday = schedule?.day == today

    NavigationBarMediumTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize().padding(15.dp)
//                    .fillMaxHeight(),
//                horizontalAlignment = Alignment.CenterHorizontally,
//            )
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
                .fillMaxHeight()
            ) {
                Button(
                    modifier = Modifier
                        .align(Alignment.TopStart),
                    onClick = { navController.popBackStack() }
                ) { Text("Back") }

                if (schedule != null) {
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 60.dp, top = 50.dp)
                    ) {
                        ExerciseList(schedule.exercise)
                    }
                }
                else {
                    Text("Tidak ada jadwal yang dipilih.")
                }

                if (isToday) {
                    Button(
                        modifier = Modifier
                            .height(50.dp)
                            .align(Alignment.BottomCenter),
                        onClick = {
                            navController.navigate(Screens.StartExercise.route)
                        },
                    ) { Text(
                        text = "Start Exercise",
                        fontSize = 18.sp,
                    ) }
                }
                else {
                    Button(
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.background),
                        modifier = Modifier
                            .align(Alignment.BottomCenter),
                        onClick = { },
                    ) { Text(
                        text = "Can't Start Yet ",
                        fontSize = 16.sp,
                        style = TextStyle( fontStyle = FontStyle.Italic),
                        color = MaterialTheme.colorScheme.onBackground
                    ) }
                }

            }

        }
    }
}

@Composable
fun ExerciseList(exercises: List<Exercises>) {
    LazyColumn (
        modifier = Modifier
            .padding(16.dp, 5.dp, 16.dp, 0.dp)
    ){
        items(exercises.size) { index ->
            val exercise = exercises[index]
            CardExerciseSchedule(
                exercise
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}