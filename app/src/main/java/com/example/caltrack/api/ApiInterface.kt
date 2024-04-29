package com.example.caltrack.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

const val appId: String = "33b674fa"
const val appKey: String = "a8d79fa9640734211bc8b291dec35111"

interface ApiInterface {
    @GET("/v2/search/instant")
    @Headers("Content-Type: application/json", "x-app-id: $appId", "x-app-key: $appKey")
    suspend fun search(@Query("query") query: String,
                       @Query("branded") branded: String = "No"): Response<SearchResponse>
}