package com.example.peakform.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.peakform.R
import com.example.peakform.api.ExerciseService
import com.example.peakform.data.model.Exercises
import com.example.peakform.viewmodel.VMSearch
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.peakform.data.model.Schedule
import com.example.peakform.navigation.Screens
import com.example.peakform.screens.PopupState
import com.example.peakform.viewmodel.VMUpdateSchedule
import kotlinx.coroutines.delay
import com.example.peakform.viewmodel.VMShowSchedule

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CardExerciseSchedule(navController: NavController, schedule: Schedule, exercise: Exercises, ) {
    val viewModel: VMSearch = viewModel()
    val exercises by viewModel.exercises.collectAsState()
    val viewModelExercise: VMUpdateSchedule = viewModel()
    val isLoading by viewModelExercise.loading.collectAsState()
    val isSuccess by viewModelExercise.success.collectAsState()
    val errorMessage by viewModelExercise.error.collectAsState()
    val context = LocalContext.current
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val imageLoader = ImageLoader.Builder(context)
        .components { add(SvgDecoder.Factory()) }.build()
    val fullImageUrl = exercise.image.let { "${ExerciseService.getBaseUrlForImages()}$it" }
    val request = ImageRequest.Builder(context)
        .data(fullImageUrl)
        .crossfade(true)
        .build()

    errorMessage?.let {
        Popup(navController, isSuccess, it, isLoading)
    } ?: Popup(navController, isSuccess, "", isLoading)


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable { },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubcomposeAsyncImage(
                model = request,
                imageLoader = imageLoader,
                contentDescription = "Exercise Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(100.dp)
                    .weight(1f),
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
            Column (
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(3f)
                    .padding(start = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ){
                Text(
                    text = exercise.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color =  MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = exercise.set.toString() + " x " + exercise.repetition.toString() + " reps",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color =  MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Column (
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.5f)
                    .padding(start = 8.dp, top = 10.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_edit),
                    contentDescription = "Edit Icon",
                    modifier = Modifier
                        .size(20.dp)
                        .weight(1f)
                        .padding(bottom = 4.dp)
                        .clickable { showEditDialog = true }
                )
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete Icon",
                    modifier = Modifier
                        .size(20.dp)
                        .weight(1f)
                        .padding(top = 4.dp)
                        .clickable { showDeleteDialog = true }
                )
            }
        }
    }

    var currentExercise = exercise
    var selectedIdExercise = currentExercise.id
    var setUpdate = exercise.set
    var repUpdate = exercise.repetition
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Edit Exercise",
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
                        selectedExercise = currentExercise,
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
                        showEditDialog = false
                        viewModelExercise.editExerciseSchedule(schedule.id, exercise.id, selectedIdExercise, setUpdate, repUpdate)
//                        navController.navigate(Screens.DetailSchedule.route) {
//                            popUpTo(Screens.DetailSchedule.route) { inclusive = true }
//                            launchSingleTop = true
//                        }
                    }
                ) {
                    Text("Edit")
                }
            },
            dismissButton = {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth(0.3f),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
                    onClick = {
                        showEditDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Delete Exercise",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    SubcomposeAsyncImage(
                        model = request,
                        imageLoader = imageLoader,
                        contentDescription = "Exercise Image",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .align(alignment = Alignment.CenterHorizontally)
                            .height(150.dp),
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
                    Spacer(modifier = Modifier .height(10.dp))
                    Text(
                        text = "Are u sure want delete \n"+ exercise.name + "\nfrom exercise?",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                }
            },
            confirmButton = {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth(0.65f),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                    onClick = {
                        showDeleteDialog = false
                        viewModelExercise.deleteExerciseSchedule(schedule.id, exercise.id)
//                        navController.navigate(Screens.DetailSchedule.route) {
//                            popUpTo(Screens.DetailSchedule.route) { inclusive = true }
//                            launchSingleTop = true
//                        }
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth(0.3f),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun NumberInputField(
    label: String,
    value: Int,
    maxValue: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentValue by remember { mutableStateOf(value.toString()) }
    var isError by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextField(
            shape = RoundedCornerShape(15.dp),
            value = currentValue,
            onValueChange = { newValue ->
                if (newValue.isEmpty()) {
                    currentValue = ""
                    onValueChange(0)
                    isError = false
                    return@OutlinedTextField
                }

                val numericValue = newValue.toIntOrNull()
                if (numericValue != null && numericValue >= 1 && numericValue <= maxValue) {
                    currentValue = newValue
                    onValueChange(numericValue)
                    isError = false
                } else {
                    isError = true
                }
            },
            label = { Text(label) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            isError = isError,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                if (isError) {
                    Icon(
                        Icons.Default.Error,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        )
        if (isError) {
            Text(
                text = "Nilai harus antara 1-$maxValue",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun Popup(navController: NavController, isSuccess: Boolean, isError: String, isLoading:Boolean){
    var showPopup by remember { mutableStateOf(true) }
    var viewModel: VMShowSchedule = viewModel()
    var scheduleState = viewModel.schedule.collectAsState()
    Log.d("sebelum", scheduleState.toString())

    LaunchedEffect(isSuccess) {
        showPopup = true
        if (isSuccess) {
            delay(2000)
            navController.navigate(Screens.DetailSchedule.route) {
                popUpTo(Screens.DetailSchedule.route) { inclusive = true }
                launchSingleTop = true
            }
        }
        if (isError != "") {
            delay(3000)
            navController.navigate(Screens.DetailSchedule.route) {
                popUpTo(Screens.DetailSchedule.route) { inclusive = true }
                launchSingleTop = true
            }
        }
        showPopup = false
    }
    if(showPopup){
        PopupState(isLoading, isSuccess, isError, "edit exercise")
    }
}