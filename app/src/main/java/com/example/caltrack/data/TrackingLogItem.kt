package com.example.caltrack.data

data class TrackingLogItem(
    val foodname: String = "",
    val calories: Float = 0f,
    val water: Float = 0f,
    val time: String = "00:00",
)
