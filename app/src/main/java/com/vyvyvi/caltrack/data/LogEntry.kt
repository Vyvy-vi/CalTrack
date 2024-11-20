package com.vyvyvi.caltrack.data

sealed class LogEntry

data class FoodLogItem(
    val foodname: String = "",
    val calories: Float = 0f,
    val quantity: Float = 0f,
    val fat: Float = 0f,
    val carbs: Float = 0f,
    val protein: Float = 0f,
    val fiber: Float = 0f,
    val time: String = "00:00",
) : LogEntry()

data class WaterLogItem(
    val quantity: Float = 0f,
    val time: String = "00:00",
) : LogEntry()