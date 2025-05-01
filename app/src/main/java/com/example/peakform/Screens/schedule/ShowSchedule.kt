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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.peakform.viewmodel.VMShowSchedule
import com.example.peakform.data.model.Schedule
import com.example.peakform.data.model.getDayName
import com.example.peakform.navigation.Screens
import com.example.peakform.ui.theme.NavigationBarMediumTheme
import com.example.peakform.viewmodel.VMUser
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowSchedule(navController: NavController, userViewModel: VMUser,vmShowSchedule: VMShowSchedule = viewModel()){
    val schedules by vmShowSchedule.schedule.collectAsState()
    val user = userViewModel.user

    LaunchedEffect(user) {
        user?.id?.let {
            vmShowSchedule.setUserId(it)
        }
    }

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
                        navController.popBackStack()
                    }
                ) {
                    Text("Back")
                }
                ScheduleList(schedules?.schedules ?: emptyList(), navController, vmShowSchedule)
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleList(schedules: List<Schedule>, navController: NavController, vmShowSchedule: VMShowSchedule) {
    LazyColumn (
        modifier = Modifier
            .padding(16.dp, 16.dp, 16.dp, 50.dp)
    ){
        items(schedules.size) { index ->
            val schedule = schedules[index]
            ScheduleItem(schedule, navController, vmShowSchedule)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleItem(schedule: Schedule, navController: NavController, vmShowSchedule: VMShowSchedule){
    val today = LocalDate.now().dayOfWeek.value // Monday = 1, Sunday = 7
    val isToday = schedule.day == today

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .height(100.dp)
            .clickable {
                vmShowSchedule.selectSchedule(schedule)
                navController.navigate(Screens.DetailSchedule.route)
            },
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor =
                if (isToday)
                    MaterialTheme.colorScheme.secondary
                else
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
                    text = getDayName(schedule.day),
                    fontSize = 25.sp,
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = schedule.type,
                    fontSize = 18.sp,
                    color = Color.White,
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun PreviewShowSchedule(){
    ShowSchedule(
        navController = rememberNavController(),
        userViewModel = TODO(),
        vmShowSchedule = TODO()
    )
//    MakeSchedule(navController = rememberNavController(), viewModel = viewModel())
}