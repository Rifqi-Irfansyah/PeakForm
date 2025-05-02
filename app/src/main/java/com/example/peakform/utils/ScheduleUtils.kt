package com.example.peakform.utils

import com.example.peakform.data.model.Schedule

object ScheduleUtils {
    fun findScheduleIdByDay(schedules: List<Schedule>?, targetDay: Int): Long? {
        return schedules
            ?.find { it.day == targetDay }
            ?.id
    }
}