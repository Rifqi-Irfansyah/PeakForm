package com.example.peakform.data.model

data class ScheduleResponse (
    val status: String,
    val message: String,
    val data: ScheduleData
)

data class ScheduleData(
    val schedules: List<Schedule>
)

data class Schedule(
    val id: Long,
    val day: Int,
    val type: String,
    val exercise: List<Exercises>
)

data class Exercises(
    val id: Int,
    val set: Int,
    val repetition: Int,
    val name: String,
    val type: String,
    val muscle: String,
    val equipment: String,
    val difficulty: String,
    val instructions: String,
    val image: String
)

data class ExerciseScheduleRequest(
    val id: String,
    val id_exercise: Int,
    val new_id_exercise: Int,
    val set: Int,
    val repetition: Int
)