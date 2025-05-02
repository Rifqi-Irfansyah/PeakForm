package com.example.peakform.screens.schedule

import android.os.Build
import android.util.Log
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.peakform.viewmodel.VMShowSchedule
import com.example.peakform.data.model.Schedule
import com.example.peakform.data.model.getDayName
import com.example.peakform.navigation.Screens
import com.example.peakform.screens.PopupState
import com.example.peakform.ui.components.ButtonBack
import com.example.peakform.ui.components.Dropdown
import com.example.peakform.ui.components.MultiSelectExercise
import com.example.peakform.ui.components.NumberInputField
import com.example.peakform.ui.components.WarningDialog
import com.example.peakform.ui.theme.NavigationBarMediumTheme
import com.example.peakform.utils.ScheduleUtils
import com.example.peakform.viewmodel.VMSearch
import com.example.peakform.viewmodel.VMUpdateSchedule
import com.example.peakform.viewmodel.VMUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowSchedule(navController: NavController, userViewModel: VMUser,vmShowSchedule: VMShowSchedule = viewModel()){
    val schedules by vmShowSchedule.schedule.collectAsState()
    val viewModelExercise: VMUpdateSchedule = viewModel()
    val vmSearch: VMSearch = viewModel()
    val exercises by vmSearch.exercises.collectAsState()
    val user = userViewModel.user
    var showAddDialog by remember { mutableStateOf(false) }
    var setUpdate by remember { mutableStateOf<Int?>(null) }
    var repUpdate by remember { mutableStateOf<Int?>(null) }
    var dayUpdate by remember { mutableStateOf<Int?>(null) }
    var typeUpdate by remember { mutableStateOf<String?>(null) }
    var showWarningPopup by remember { mutableStateOf(false) }
    var listExerciseUpdate by remember { mutableStateOf(setOf<Int>()) }
    var showSelectExercise by remember { mutableStateOf(false) }
    val selectedExercises = listExerciseUpdate.mapNotNull { index ->
        exercises.getOrNull(index - 1)
    }
    val isLoading by viewModelExercise.loading.collectAsState()
    val isSuccess by viewModelExercise.success.collectAsState()
    val errorMessage by viewModelExercise.error.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(user) {
        user?.id?.let {
            vmShowSchedule.setUserId(it)
        }
    }

    errorMessage?.let {
        Popup(navController, isSuccess, it, isLoading)
    } ?: Popup(navController, isSuccess, "", isLoading)

    val dayNames = mapOf(
        1 to "Monday",
        2 to "Tuesday",
        3 to "Wednesday",
        4 to "Thursday",
        5 to "Friday",
        6 to "Saturday",
        7 to "Sunday"
    )
    val usedDays: List<Int> = schedules?.schedules?.map { it.day } ?: emptyList()
    val availableDaysMap = dayNames.filterKeys { it !in usedDays }
    val availableDayNames = availableDaysMap.values.toList()

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
                            .clickable{ showAddDialog = true }
                    ){
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .padding(start = 15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Schedule",
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .padding(end = 15.dp)
                        )
                    }
                }
                user?.id?.let {
                    ScheduleList(schedules?.schedules ?: emptyList(), navController, vmShowSchedule, availableDayNames, dayNames,
                        it
                    )
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
                            text = "Add Schedule",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                text = {
                    Column{
                        Dropdown(
                            title = "Day",
                            item = availableDayNames,
                            selectedItem = dayNames[dayUpdate],
                            onItemSelected = { selectedName ->
                                dayUpdate = availableDaysMap.filterValues { it == selectedName }.keys.first()
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Dropdown(
                            title = "Type",
                            item = listOf("strength", "cardio"),
                            selectedItem = typeUpdate,
                            onItemSelected = {typeUpdate = it}
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
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedButton(
                            onClick = { showSelectExercise = true },
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(55.dp)
                        ){
                            Text("Choose Exercise")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        if (selectedExercises.isNotEmpty()) {
                            Text("Exercise selected: ${selectedExercises.joinToString { it.name }}")
                        } else {
                            Text("No exercises selected")
                        }
                        if (showSelectExercise) {
                            MultiSelectExercise(
                                title = "Exercises",
                                items = exercises,
                                selectedItems = listExerciseUpdate,
                                onItemSelected = { index ->
                                    listExerciseUpdate = listExerciseUpdate + index
                                },
                                onItemUnselected = { index ->
                                    listExerciseUpdate = listExerciseUpdate - index
                                },
                                onDismiss = { showSelectExercise = false },
                                onConfirm = {
                                    showSelectExercise = false
                                    println("Exercise Selected: ${selectedExercises.joinToString { it.name }}")
                                }
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth(0.65f),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                        onClick = {
                            scope.launch {
                                val selectedExerciseIds = listExerciseUpdate.mapNotNull { index ->
                                    exercises.getOrNull(index - 1)?.id
                                }
                                if (
                                    user != null &&
                                    dayUpdate != null &&
                                    typeUpdate != null &&
                                    setUpdate != null &&
                                    repUpdate != null) {
                                    showAddDialog = false
                                    viewModelExercise.addListExerciseSchedule(
                                        user.id,
                                        selectedExerciseIds,
                                        dayUpdate!!,
                                        typeUpdate!!,
                                        setUpdate!!,
                                        repUpdate!!
                                    )
                                } else {
                                    showWarningPopup = true
                                }
                            }
                        }
                    ) {
                        Text("Add Schedule")
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
    }

    if (showWarningPopup){
        WarningDialog(true, "Please Complete Data", { showWarningPopup = false })

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleList(schedules: List<Schedule>, navController: NavController, vmShowSchedule: VMShowSchedule, availableDayNames:List<String>, dayNames:Map<Int, String>, idUser:String) {
    LazyColumn (
        modifier = Modifier
            .padding(16.dp, 16.dp, 16.dp, 50.dp)
    ){
        items(schedules.size) { index ->
            val schedule = schedules[index]
            ScheduleItem(schedules, schedule, navController, vmShowSchedule, availableDayNames, dayNames, idUser)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleItem(schedules:List<Schedule>, schedule: Schedule, navController: NavController, vmShowSchedule: VMShowSchedule, availableDayNames:List<String>, dayNames:Map<Int, String>, idUser: String) {
    val today = LocalDate.now().dayOfWeek.value // Monday = 1, Sunday = 7
    val isToday = schedule.day == today
    var showDetailPopup by remember { mutableStateOf(false) }
    var showChangeDayPopup by remember { mutableStateOf(false) }
    var showSwitchDayPopup by remember { mutableStateOf(false) }
    var showDeletePopup by remember { mutableStateOf(false) }
    var dayChange by remember { mutableStateOf<Int?>(null) }
    var dayFirst by remember { mutableStateOf<Int?>(null) }
    var daySecond by remember { mutableStateOf<Int?>(null) }
    val usedDays: MutableList<Int> = schedules.map { it.day }.toMutableList()
    val availableDaysMap = dayNames.filterKeys { it !in usedDays }
    var unavailableDaysMap = dayNames.filterKeys { it in usedDays }
    var unavailableDayNames = unavailableDaysMap.values.toMutableList()
    val viewModelExercise: VMUpdateSchedule = viewModel()
    var showWarningPopup by remember { mutableStateOf(false) }
    val itemsForFirstDropdown = unavailableDayNames.filter { it != unavailableDaysMap[daySecond] }
    val itemsForSecondDropdown = unavailableDayNames.filter { it != unavailableDaysMap[dayFirst] }

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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxHeight()
                        .weight(1f)
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
                Icon(
                    Icons.Filled.MoreVert,
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .weight(0.2f)
                        .fillMaxHeight()
                        .clickable { showDetailPopup = true }
                        .padding(15.dp)
                )
            }
        }
    }

    if (showDetailPopup) {
        AlertDialog(
            onDismissRequest = { showDetailPopup = false },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { showChangeDayPopup = true },
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                    ) {
                        Text(
                            text = "Change Day",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { showSwitchDayPopup = true },
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                    ) {
                        Text(
                            text = "Switch Schedule Days",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { showDeletePopup = true },
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                    ) {
                        Text(
                            text = "Delete Schedule",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            },
            confirmButton = { },
            dismissButton = {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth(0.3f),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
                    onClick = { showDetailPopup = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showChangeDayPopup) {
        AlertDialog(
            onDismissRequest = { showDetailPopup = false },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Dropdown(
                        title = "Day",
                        item = availableDayNames,
                        selectedItem = dayNames[dayChange],
                        onItemSelected = { selectedName ->
                            dayChange =
                                availableDaysMap.filterValues { it == selectedName }.keys.first()
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            },
            confirmButton = {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth(0.65f),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                    onClick = {
                        if (dayChange != null) {
                            viewModelExercise.updateDay(schedule.id.toString(), dayChange!!)
                        } else {
                            showWarningPopup = true
                        }
                    },
                ) {
                    Text(
                        text = "OK",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            dismissButton = {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth(0.3f),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
                    onClick = { showChangeDayPopup = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showSwitchDayPopup) {
        AlertDialog(
            onDismissRequest = { showDetailPopup = false },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Switch Day Schedule",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.height(16.dp))
                    Dropdown(
                        title = "Day",
                        item = itemsForFirstDropdown,
                        selectedItem = dayNames[dayFirst],
                        onItemSelected = { selectedName ->
                            dayFirst =
                                unavailableDaysMap.filterValues { it == selectedName }.keys.first()
                        }
                    )
                    Icon(
                        Icons.Filled.SwapVert,
                        contentDescription = "Switch",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .padding(5.dp)
                    )
                    Dropdown(
                        title = "Day",
                        item = itemsForSecondDropdown,
                        selectedItem = dayNames[daySecond],
                        onItemSelected = { selectedName ->
                            daySecond =
                                unavailableDaysMap.filterValues { it == selectedName }.keys.first()
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            },
            confirmButton = {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth(0.65f),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                    onClick = {
                        if (dayFirst != null && daySecond != null) {
                            viewModelExercise.switchDay(
                                ScheduleUtils.findScheduleIdByDay(schedules, dayFirst!!).toString(),
                                ScheduleUtils.findScheduleIdByDay(schedules, daySecond!!).toString(),
                                dayFirst!!, daySecond!!
                            )
                        } else {
                            showWarningPopup = true
                        }
                    },
                ) {
                    Text(
                        text = "OK",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            dismissButton = {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth(0.3f),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
                    onClick = { showSwitchDayPopup = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showDeletePopup) {
        AlertDialog(
            onDismissRequest = { showDetailPopup = false },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Filled.WarningAmber,
                        contentDescription = "warning",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .height(150.dp)
                            .fillMaxWidth()
                            .padding(start = 15.dp)
                    )
                    Spacer(Modifier.height(20.dp))
                    Text(
                        text = "Are u sure want \n delete this schedule?",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = getDayName(schedule.day) + " | " + schedule.type,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.height(16.dp))
                }
            },
            confirmButton = {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth(0.65f),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                    onClick = { viewModelExercise.deleteScheudle(schedule.id.toString(), idUser)},
                ) {
                    Text(
                        text = "OK",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            dismissButton = {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth(0.3f),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
                    onClick = { showDeletePopup = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showWarningPopup){
        WarningDialog(true, "Please Complete Data", { showWarningPopup = false })
    }
}

@Composable
private fun Popup(navController: NavController, isSuccess: Boolean, isError: String, isLoading:Boolean){
    var showPopup by remember { mutableStateOf(true) }

    LaunchedEffect(isSuccess) {
        showPopup = true
        if (isSuccess) {
            delay(2000)
            navController.navigate(Screens.ShowSchedule.route) {
                popUpTo(Screens.ShowSchedule.route) { inclusive = true }
                launchSingleTop = true
            }
        }
        if (isError != "") {
            delay(3000)
            navController.navigate(Screens.ShowSchedule.route) {
                popUpTo(Screens.ShowSchedule.route) { inclusive = true }
                launchSingleTop = true
            }
        }
        showPopup = false
    }
    if(showPopup){
        PopupState(isLoading, isSuccess, isError, "Update schedule")
    }
}