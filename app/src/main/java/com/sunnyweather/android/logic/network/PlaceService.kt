package com.sunnyweather.android.logic.network

import com.sunnyweather.android.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {

    @GET("v2/place")
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>
}
