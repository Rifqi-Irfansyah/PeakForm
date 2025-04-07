package com.example.peakform.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peakform.API.ExerciseService
import com.example.peakform.data.model.Exercise
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VMSearch : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _allExercises = MutableStateFlow<List<Exercise>>(emptyList())

    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises: StateFlow<List<Exercise>> = _exercises.asStateFlow()

    private val _selectedExercise = MutableStateFlow<Exercise?>(null)
    val selectedExercise: StateFlow<Exercise?> = _selectedExercise

    private val _nameFilter = MutableStateFlow<String?>(null)
    val nameFilter: StateFlow<String?> = _nameFilter.asStateFlow()

    private val _typeFilter = MutableStateFlow<String?>(null)
    val typeFilter: StateFlow<String?> = _typeFilter.asStateFlow()

    private val _muscleFilter = MutableStateFlow<String?>(null)
    val muscleFilter: StateFlow<String?> = _muscleFilter.asStateFlow()

    private val _equipmentFilter = MutableStateFlow<String?>(null)
    val equipmentFilter: StateFlow<String?> = _equipmentFilter.asStateFlow()

    private val _difficultyFilter = MutableStateFlow<String?>(null)
    val difficultyFilter: StateFlow<String?> = _difficultyFilter.asStateFlow()

    init {
        fetchExercises()
    }

    fun fetchExercises() {
        viewModelScope.launch {
            _loading.value = true
            _success.value = false
            _error.value = null
            try {
                val apiResponse = ExerciseService.instance.getExercises()
                if (apiResponse.isSuccessful) {
                    val responseBody = apiResponse.body()
                    if (responseBody != null) {
                        if (responseBody.status == "success") {
                            Log.d("API Response", "Success: ${responseBody.message}, Data: ${responseBody.data}")
                            _allExercises.value = responseBody.data
                            applyFilters()
                            _success.value = true
                        } else {
                            Log.e("API Response", "Error: ${responseBody.message}")
                            _error.value = responseBody.message
                        }
                    } else {
                        Log.e("API Response", "Error: Response body is null")
                        _error.value = "Response body is null"
                    }
                } else {
                    val errorMessage = apiResponse.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Response", "Error: $errorMessage")
                    _error.value = errorMessage
                }
            } catch (e: Exception) {
                Log.e("Error ethernet", "${e.message}")
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun onExerciseClicked(exercise: Exercise) {
        _selectedExercise.value = exercise
    }

    fun clearSelectedExercise() {
        _selectedExercise.value = null
    }

    fun applyFilters(
        name: String? = _nameFilter.value,
        type: String? = _typeFilter.value,
        muscle: String? = _muscleFilter.value,
        equipment: String? = _equipmentFilter.value,
        difficulty: String? = _difficultyFilter.value
    ) {
        _nameFilter.value = name
        _typeFilter.value = type
        _muscleFilter.value = muscle
        _equipmentFilter.value = equipment
        _difficultyFilter.value = difficulty

        _exercises.value = _allExercises.value.filter { exercise ->
            val matchesName = name.isNullOrEmpty() || exercise.Name?.contains(name, ignoreCase = true) == true
            val matchesType = type.isNullOrEmpty() || exercise.Type == type
            val matchesMuscle = muscle.isNullOrEmpty() || exercise.Muscle == muscle
            val matchesEquipment = equipment.isNullOrEmpty() || exercise.Equipment == equipment
            val matchesDifficulty = difficulty.isNullOrEmpty() || exercise.Difficulty == difficulty
            matchesName && matchesType && matchesMuscle && matchesEquipment && matchesDifficulty
        }
    }

    fun getUniqueTypes(): List<String> = _allExercises.value.mapNotNull { it.Type }.distinct()
    fun getUniqueMuscles(): List<String> = _allExercises.value.mapNotNull { it.Muscle }.distinct()
    fun getUniqueEquipment(): List<String> = _allExercises.value.mapNotNull { it.Equipment }.distinct()
    fun getUniqueDifficulties(): List<String> = _allExercises.value.mapNotNull { it.Difficulty }.distinct()
}