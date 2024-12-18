package com.vyvyvi.caltrack.api

data class SearchResponse(
    val common: List<FoodItem>
)

data class FoodItem(
    val food_name: String,
    val photo: PhotoDetails,
)

data class PhotoDetails(
    val thumb: String
)