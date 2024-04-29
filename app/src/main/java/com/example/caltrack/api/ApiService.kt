package com.example.caltrack.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://trackapi.nutritionix.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val _interface: ApiInterface = retrofit.create(ApiInterface::class.java)
}