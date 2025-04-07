package com.example.peakform.screens.schedule

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import com.example.peakform.ui.theme.NavigationBarMediumTheme
import java.time.LocalDate

@Composable
fun DetailSchedule(viewModel : VMShowSchedule){
    val selectedScheduleState = viewModel.selectedSchedule.collectAsState()
    val schedule = selectedScheduleState.value

    NavigationBarMediumTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize().padding(15.dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Button(
                    modifier = Modifier
                        .align(alignment = Alignment.Start),
                    onClick = {
                    }
                ) {
                    Text("Back")
                }
                if (schedule != null) {
                    ExerciseList(schedule.exercise)
                }
                else {
                    Text("Tidak ada jadwal yang dipilih.")
                }
            }

        }
    }
}

@Composable
fun ExerciseList(exercises: List<Exercises>) {
    LazyColumn (
        modifier = Modifier
            .padding(16.dp, 16.dp, 16.dp, 50.dp)
    ){
        items(exercises.size) { index ->
            val exercise = exercises[index]
            ExerciseItem(exercise)
        }
    }
}

@Composable
fun ExerciseItem(exercise: Exercises){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .height(120.dp)
            .clickable {
            },
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.primary
            )
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxHeight()
                    .fillMaxWidth()
            ) {
                Text(
                    text = exercise.name,
                    fontSize = 20.sp,
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = exercise.repetition.toString(),
                    fontSize = 18.sp,
                    color = Color.White,
                )
            }
        }
    }
}