package com.example.caltrack.data

import android.os.Parcelable

data class TrackingLog(
    var caloriesConsumed: Float = 0f,
    var waterConsumed: Float = 0f,
    var logs: MutableList<TrackingLogItem> = mutableListOf()
)