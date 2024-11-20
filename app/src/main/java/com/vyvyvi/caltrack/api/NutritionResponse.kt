package com.vyvyvi.caltrack.api

data class NutritionResponse(
    val foods: List<FoodNutritionDetails>
)

data class FoodNutritionDetails(
    val food_name: String,
    val serving_weight_grams: Float,
    val nf_calories: Float,
    val nf_total_fat: Float,
    val nf_protein: Float,
    val nf_total_carbohydrate: Float,
    val nf_dietary_fiber: Float
)