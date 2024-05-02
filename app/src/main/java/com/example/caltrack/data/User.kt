package com.example.caltrack.data

data class User(
    val name: String = "",
    val age: Int = 0,
    val gender: String = "",
    val height: Float = 0f,
    val currentWeight: Float = 0f,
    val targetWeight: Float = 0f,
    val activityLevel: String = "",
    val dailyCalories: Float = 2000f,
    val dailyWater: Float = 8f,
    val weeksNeeded: Float = 10f
)
