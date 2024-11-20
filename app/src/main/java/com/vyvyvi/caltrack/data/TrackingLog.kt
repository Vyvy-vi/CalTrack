package com.vyvyvi.caltrack.data

data class TrackingLog(
    var uID: String? = null,
    var caloriesConsumed: Float = 0f,
    var waterConsumed: Float = 0f,
    var water_logs: MutableList<WaterLogItem> = mutableListOf(),
    var food_logs: MutableList<FoodLogItem> = mutableListOf()
)