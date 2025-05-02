package com.example.peakform.screens.schedule

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.peakform.data.model.Exercises
import com.example.peakform.data.model.Schedule
import com.example.peakform.navigation.Screens
import com.example.peakform.ui.components.ButtonBack
import com.example.peakform.ui.components.CardExerciseSchedule
import com.example.peakform.ui.components.DropdownExercise
import com.example.peakform.ui.components.NumberInputField
import com.example.peakform.ui.theme.NavigationBarMediumTheme
import com.example.peakform.viewmodel.VMSearch
import com.example.peakform.viewmodel.VMShowSchedule
import com.example.peakform.viewmodel.VMUpdateSchedule
import com.example.peakform.viewmodel.VMUser
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailSchedule(navController: NavController, viewModel : VMShowSchedule, userViewModel:VMUser){
    val selectedScheduleState = viewModel.selectedSchedule.collectAsState()
    val schedule = selectedScheduleState.value
    val vmSearch: VMSearch = viewModel()
    val exercises by vmSearch.exercises.collectAsState()
    val viewModelExercise: VMUpdateSchedule = viewModel()
    viewModel.updateSelectedSchedule(schedule?.id.toString())
    val today = LocalDate.now().dayOfWeek.value // Monday = 1, Sunday = 7
    val isToday = schedule?.day == today
    val prefManager = com.example.peakform.utils.PrefManager(navController.context)
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedIdExercise: Int? = null
    var setUpdate by remember { mutableStateOf<Int?>(null) }
    var repUpdate by remember { mutableStateOf<Int?>(null) }
    val user = userViewModel.user
    var showWarningDialog by remember { mutableStateOf(false) }
    val warningMessage by remember { mutableStateOf("Please Complete Data") }

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
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
                .fillMaxHeight()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    ButtonBack(navController)
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .height(35.dp)
                            .clip(shape = RoundedCornerShape(35.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable{showAddDialog = true}
                    ){
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Add",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .padding(start = 15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Add",
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .padding(end = 15.dp)
                        )
                    }
                }

                if (schedule != null) {
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 60.dp, top = 50.dp)
                    ) {
                        ExerciseList(navController, schedule, schedule.exercise)
                    }
                }
                else {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ){
                        Text(
                            text = "OOPS \n There no exercise \nin this schedule",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 300.dp)
                                .fillMaxHeight()
                                .fillMaxSize()
                        )
                    }
                }

                if (isToday) {
                    if (prefManager.isHaveExercise() == true) {
                        Button(
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.background),
                            modifier = Modifier
                                .align(Alignment.BottomCenter),
                            onClick = { },
                        ) { Text(
                            text = "You have completed this schedule",
                            fontSize = 16.sp,
                            style = TextStyle( fontStyle = FontStyle.Italic),
                            color = MaterialTheme.colorScheme.onBackground
                        ) }
                    } else {
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

        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Add Exercise",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                text = {
                    Column{
                        DropdownExercise(
                            exercises = exercises,
                            selectedExercise = null,
                            onExerciseSelected = { selected ->
                                selectedIdExercise = selected.id
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        NumberInputField(
                            label = "Sets",
                            value = setUpdate,
                            maxValue = 10,
                            onValueChange = { setUpdate = it }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        NumberInputField(
                            label = "Reps",
                            value = repUpdate,
                            maxValue = 100,
                            onValueChange = { repUpdate = it }
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth(0.65f),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                        onClick = {
                            if (user != null) {
                                if (
                                    setUpdate != null &&
                                    repUpdate != null &&
                                    schedule?.day != null &&
                                    selectedIdExercise != null
                                ) {
                                    showAddDialog = false
                                    viewModelExercise.addExerciseSchedule(
                                        user.id,
                                        selectedIdExercise!!,
                                        schedule.day,
                                        schedule.type,
                                        setUpdate!!,
                                        repUpdate!!
                                    )
                                }
                                else{
                                    showWarningDialog = true
                                }
                            }
                        }
                    ) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth(0.3f),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
                        onClick = {
                            showAddDialog = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (showWarningDialog) {
            AlertDialog(
                onDismissRequest = { showWarningDialog = false },
                title = {Text(
                    text = "Warning",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )},
                text = { Text(text = warningMessage) },
                confirmButton = {
                    TextButton(
                        onClick = { showWarningDialog = false },
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(15.dp))
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = "OK",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            )
        }
    }
}

@Composable
fun ExerciseList(navController: NavController, schedule: Schedule, exercises: List<Exercises>) {
    LazyColumn (
        modifier = Modifier
            .padding(16.dp, 5.dp, 16.dp, 0.dp)
    ){
        items(exercises.size) { index ->
            val exercise = exercises[index]
            CardExerciseSchedule(navController, schedule, exercise)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}