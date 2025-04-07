package com.example.peakform.Screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.peakform.ViewModel.VMSearch
import com.example.peakform.ui.components.CardExercise

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(navController: NavController? = null, viewModel: VMSearch = viewModel()) {
    val exercises by viewModel.exercises.collectAsState()
    val selectedExercise by viewModel.selectedExercise.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    val nameFilter by viewModel.nameFilter.collectAsState()
    val typeFilter by viewModel.typeFilter.collectAsState()
    val muscleFilter by viewModel.muscleFilter.collectAsState()
    val equipmentFilter by viewModel.equipmentFilter.collectAsState()
    val difficultyFilter by viewModel.difficultyFilter.collectAsState()

    val typeOptions by remember { derivedStateOf { listOf("") + viewModel.getUniqueTypes() } }
    val muscleOptions by remember { derivedStateOf { listOf("") + viewModel.getUniqueMuscles() } }
    val equipmentOptions by remember { derivedStateOf { listOf("") + viewModel.getUniqueEquipment() } }
    val difficultyOptions by remember { derivedStateOf { listOf("") + viewModel.getUniqueDifficulties() } }

    var searchText by remember { mutableStateOf(nameFilter ?: "") }

    var showFilterModal by remember { mutableStateOf(false) }

    var tempTypeFilter by remember { mutableStateOf(typeFilter) }
    var tempMuscleFilter by remember { mutableStateOf(muscleFilter) }
    var tempEquipmentFilter by remember { mutableStateOf(equipmentFilter) }
    var tempDifficultyFilter by remember { mutableStateOf(difficultyFilter) }

    var typeExpanded by remember { mutableStateOf(false) }
    var muscleExpanded by remember { mutableStateOf(false) }
    var equipmentExpanded by remember { mutableStateOf(false) }
    var difficultyExpanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { newText ->
                        searchText = newText
                        viewModel.applyFilters(name = newText)
                    },
                    label = { Text("Search by Name") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    enabled = !loading
                )

                Button(
                    onClick = {
                        tempTypeFilter = typeFilter
                        tempMuscleFilter = muscleFilter
                        tempEquipmentFilter = equipmentFilter
                        tempDifficultyFilter = difficultyFilter
                        showFilterModal = true
                    },
                    modifier = Modifier.height(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Open Filters",
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text("Open Filters")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        if (loading) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        error?.let { errorMessage ->
            item {
                Text(
                    text = "Error: $errorMessage",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        items(exercises) { exercise ->
            CardExercise(
                imageUrl = exercise.image,
                title = exercise.name ?: "Unknown Exercise",
                onClick = { viewModel.onExerciseClicked(exercise) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    if (showFilterModal) {
        AlertDialog(
            onDismissRequest = { showFilterModal = false },
            title = { Text("Filters") },
            text = {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(8.dp)
                ) {
                    ExposedDropdownMenuBox(
                        expanded = typeExpanded,
                        onExpandedChange = { typeExpanded = !typeExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = tempTypeFilter ?: "Any",
                            onValueChange = {},
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            readOnly = true,
                            label = { Text("Type") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                        )
                        ExposedDropdownMenu(
                            expanded = typeExpanded,
                            onDismissRequest = { typeExpanded = false }
                        ) {
                            typeOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(if (option.isEmpty()) "Any" else option) },
                                    onClick = {
                                        tempTypeFilter = if (option.isEmpty()) null else option
                                        typeExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    ExposedDropdownMenuBox(
                        expanded = muscleExpanded,
                        onExpandedChange = { muscleExpanded = !muscleExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = tempMuscleFilter ?: "Any",
                            onValueChange = {},
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            readOnly = true,
                            label = { Text("Muscle") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = muscleExpanded) },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                        )
                        ExposedDropdownMenu(
                            expanded = muscleExpanded,
                            onDismissRequest = { muscleExpanded = false }
                        ) {
                            muscleOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(if (option.isEmpty()) "Any" else option) },
                                    onClick = {
                                        tempMuscleFilter = if (option.isEmpty()) null else option
                                        muscleExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    ExposedDropdownMenuBox(
                        expanded = equipmentExpanded,
                        onExpandedChange = { equipmentExpanded = !equipmentExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = tempEquipmentFilter ?: "Any",
                            onValueChange = {},
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            readOnly = true,
                            label = { Text("Equipment") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = equipmentExpanded) },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                        )
                        ExposedDropdownMenu(
                            expanded = equipmentExpanded,
                            onDismissRequest = { equipmentExpanded = false }
                        ) {
                            equipmentOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(if (option.isEmpty()) "Any" else option) },
                                    onClick = {
                                        tempEquipmentFilter = if (option.isEmpty()) null else option
                                        equipmentExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    ExposedDropdownMenuBox(
                        expanded = difficultyExpanded,
                        onExpandedChange = { difficultyExpanded = !difficultyExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = tempDifficultyFilter ?: "Any",
                            onValueChange = {},
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            readOnly = true,
                            label = { Text("Difficulty") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = difficultyExpanded) },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                        )
                        ExposedDropdownMenu(
                            expanded = difficultyExpanded,
                            onDismissRequest = { difficultyExpanded = false }
                        ) {
                            difficultyOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(if (option.isEmpty()) "Any" else option) },
                                    onClick = {
                                        tempDifficultyFilter = if (option.isEmpty()) null else option
                                        difficultyExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.applyFilters(
                            name = searchText,
                            type = tempTypeFilter,
                            muscle = tempMuscleFilter,
                            equipment = tempEquipmentFilter,
                            difficulty = tempDifficultyFilter
                        )
                        showFilterModal = false
                    }
                ) {
                    Text("Apply")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showFilterModal = false }
                ) {
                    Text("Close")
                }
            }
        )
    }

    selectedExercise?.let { exercise ->
        Log.d("ExerciseDetail", "Name: ${exercise.name}")
        Log.d("ExerciseDetail", "Type: ${exercise.type}")
        Log.d("ExerciseDetail", "Muscle: ${exercise.muscle}")
        Log.d("ExerciseDetail", "Equipment: ${exercise.equipment}")
        Log.d("ExerciseDetail", "Difficulty: ${exercise.difficulty}")
        Log.d("ExerciseDetail", "Instructions: ${exercise.instructions}")
        Log.d("ExerciseDetail", "Image: ${exercise.image}")
        AlertDialog(
            onDismissRequest = { viewModel.clearSelectedExercise() },
            title = { Text(exercise.name ?: "Unknown Exercise") },
            text = {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(8.dp)
                ) {
                    CardExercise(
                        imageUrl = exercise.image,
                        title = exercise.name ?: "Unknown Exercise",
                        onClick = {}
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Type: ${exercise.type ?: "N/A"}")
                    Text("Muscle: ${exercise.muscle ?: "N/A"}")
                    Text("Equipment: ${exercise.equipment ?: "N/A"}")
                    Text("Difficulty: ${exercise.difficulty ?: "N/A"}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Instructions: ${exercise.instructions ?: "N/A"}")
                }
            },
            confirmButton = {
                Button(onClick = { viewModel.clearSelectedExercise() }) {
                    Text("Close")
                }
            }
        )
    }
}