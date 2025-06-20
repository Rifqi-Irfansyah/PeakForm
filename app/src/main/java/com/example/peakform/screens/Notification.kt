package com.example.peakform.screens

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Build
import android.util.Log
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.peakform.R
import com.example.peakform.Room.AppDatabase
import com.example.peakform.viewmodel.VMShowSchedule
import com.example.peakform.data.model.Schedule
import com.example.peakform.data.model.getDayName
import com.example.peakform.ui.components.ButtonBack
import com.example.peakform.ui.components.WarningDialog
import com.example.peakform.ui.theme.NavigationBarMediumTheme
import com.example.peakform.viewmodel.VMNotification
import com.example.peakform.viewmodel.VMNotificationFactory
import com.example.peakform.viewmodel.VMSearch
import com.example.peakform.viewmodel.VMUser
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Notification(navController: NavController, userViewModel: VMUser, vmShowSchedule: VMShowSchedule = viewModel()){
    val schedules by vmShowSchedule.schedule.collectAsState()
    val user = userViewModel.user
    val context = LocalContext.current
    val database = AppDatabase.getInstance(context)
    val vmNotification: VMNotification = viewModel(
        factory = VMNotificationFactory(context, database.notificationDao())
    )
    val notifications by vmNotification.notifications.collectAsStateWithLifecycle()
    val notificationMap = notifications.associate { notification ->
        notification.dayOfWeek to listOf(notification.hour, notification.minute)
    }

    LaunchedEffect(user) {
        user?.id?.let {
            vmShowSchedule.setUserId(it)
        }
    }

    val usedDays: List<Int> = schedules?.schedules?.map { it.day } ?: emptyList()

    NavigationBarMediumTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            if(usedDays.isEmpty()){
                Column(
                    modifier = Modifier
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ){
                    Text(
                        text = "OOPS \n You don't have schedule",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 300.dp)
                            .fillMaxHeight()
                            .fillMaxSize()
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize().padding(15.dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.Start,
            ) {
                ButtonBack(navController)
                user?.id?.let {
                    NotificationList(schedules?.schedules ?: emptyList(), vmNotification, notificationMap)
                }
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationList(schedules: List<Schedule>, vmNotification: VMNotification, notificationMap: Map<Int, List<Int>>) {
    LazyColumn (
        modifier = Modifier
            .padding(16.dp, 16.dp, 16.dp, 50.dp)
    ){
        items(schedules.sortedBy { it.day }.size) { index ->
            val schedule = schedules.sortedBy { it.day }[index]
            NotificationItem(schedule, vmNotification, notificationMap)
        }
    }
}

@SuppressLint("DefaultLocale")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationItem(schedule: Schedule, vmNotification: VMNotification, notificationMap: Map<Int, List<Int>>) {
    val today = LocalDate.now().dayOfWeek.value // Monday = 1, Sunday = 7
    val isToday = schedule.day == today
    var showEditPopup by remember { mutableStateOf(false) }
    var showWarningPopup by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    var time by remember { mutableStateOf("") }
    var hour = notificationMap[schedule.day]?.getOrNull(0) ?: 0
    var minute = notificationMap[schedule.day]?.getOrNull(1) ?: 0
    val timeStart = String.format("%02d : %02d", hour, minute)
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
    val isLoading by vmNotification.loading.collectAsState()
    val isSuccess by vmNotification.success.collectAsState()
    val errorMessage by vmNotification.error.collectAsState()
    var showPopupTrigger by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .height(100.dp)
            .clickable {
                showEditPopup = true
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxHeight()
            ){
                Text(
                    text = getDayName(schedule.day),
                    fontSize = 25.sp,
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = timeStart,
                    fontSize = 25.sp,
                    textAlign = TextAlign.End,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    if (showEditPopup) {
        AlertDialog(
            onDismissRequest = { showEditPopup = false },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Set Notification for\n" + getDayName(schedule.day),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = time.ifEmpty{timeStart.ifEmpty { "Select Time" }},
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
                        vmNotification.updateNotificationByDay(schedule.day, hour, minute)
                        showPopupTrigger = true
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth(0.3f),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
                    onClick = { showEditPopup = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showWarningPopup){
        WarningDialog(true, "Please Complete Data", { showWarningPopup = false })
    }

    if (showPopupTrigger) {
        Popup(
            isSuccess = isSuccess,
            isError = errorMessage ?: "",
            isLoading = isLoading,
            onPopupFinished = {
                showPopupTrigger = false
            }
        )
    }
}

@Composable
private fun Popup(isSuccess: Boolean, isError: String, isLoading:Boolean, onPopupFinished: () -> Unit){
    var showPopup by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (isSuccess) {
            delay(2000)
        } else if (isError.isNotEmpty()) {
            delay(3000)
        }
        showPopup = false
        onPopupFinished()
    }
    if(showPopup){
        PopupState(isLoading, isSuccess, isError, "Update Notification")
    }
}