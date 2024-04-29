package com.example.caltrack.api

data class SearchResponse(
    val common: List<FoodItem>
)

data class FoodItem(
    val food_name: String,
)