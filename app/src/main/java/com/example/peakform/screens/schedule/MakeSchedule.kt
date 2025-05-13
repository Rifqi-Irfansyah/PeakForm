package com.example.peakform.screens.schedule

import android.app.TimePickerDialog
import android.widget.TimePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.peakform.navigation.Screens
import com.example.peakform.R
import com.example.peakform.Room.AppDatabase
import com.example.peakform.data.model.getDayName
import com.example.peakform.viewmodel.VMMakeSchedule
import com.example.peakform.screens.PopupState
import com.example.peakform.ui.theme.NavigationBarMediumTheme
import com.example.peakform.viewmodel.VMNotification
import com.example.peakform.viewmodel.VMNotificationFactory
import com.example.peakform.viewmodel.VMUser
import kotlinx.coroutines.delay
import java.util.Calendar

@Composable
fun MakeSchedule(navController: NavController, userViewModel: VMUser,viewModel: VMMakeSchedule = viewModel()){
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
    val user = userViewModel.user
    var notif by remember { mutableStateOf(false) }

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
                                        notif = true
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
                                        painter = painterResource(id = imageRes),
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
        if (notif){
            SetNotification(viewModel, selectedAnswers, user?.id ?: "")
        }
    }
}

@Composable
private fun SetNotification(viewModel: VMMakeSchedule, selectedAnswers: MutableMap<Int, String>, user: String){
    val context = LocalContext.current
    val database = AppDatabase.getInstance(context)
    val vmNotification: VMNotification = viewModel(
        factory = VMNotificationFactory(context, database.notificationDao())
    )
    val calendar = Calendar.getInstance()
    var time by remember { mutableStateOf("") }
    var hour by remember { mutableStateOf(0) }
    var minute by remember { mutableStateOf(0) }
    val timePickerDialog = TimePickerDialog(
        context,
        R.style.RedTimePickerDialog,
        { _: TimePicker, hourOfDay: Int, minuteofHour: Int ->
            hour = hourOfDay
            minute = minuteofHour
            time = String.format("%02d:%02d", hourOfDay, minute)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )
    var showEditPopup = true
    if (showEditPopup) {
        AlertDialog(
            onDismissRequest = { showEditPopup = false },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Set Notification",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = time.ifEmpty { "Select Time" },
                        modifier = Modifier
                            .clip(RoundedCornerShape(15.dp))
                            .fillMaxWidth()
                            .height(55.dp)
                            .clickable { timePickerDialog.show() }
                            .border(1.dp, Color.Gray, RoundedCornerShape(15.dp))
                            .padding(16.dp),
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            },
            confirmButton = {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth(0.3f),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
                    onClick = {
                        showEditPopup = false
                        viewModel.makeSchedule(selectedAnswers, user, hour, minute)
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {}
        )
    }
}

@Composable
private fun Popup(navController: NavController, isSuccess: Boolean, isError: String, isLoading:Boolean){
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            delay(2000)
            navController.navigate(Screens.Home.route) {
                popUpTo(Screens.Home.route) { inclusive = true }
                launchSingleTop = true
            }
        }
        if (isError != "") {
            delay(3000)
            navController.navigate(Screens.Home.route) {
                popUpTo(Screens.Home.route) { inclusive = true }
                launchSingleTop = true
            }
        }
    }
    PopupState(isLoading, isSuccess, isError, "create schedule")
}