package com.sunnyweather.android.network

import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.RealtimeResponse
import com.sunnyweather.android.model.DailyResponse
import com.sunnyweather.android.model.HourlyResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {
    @GET("v2.6/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/realtime")
    fun getRealtimeWeather(@Path("lng")lng : String,@Path("lat") lat : String): Call<RealtimeResponse>

    @GET("v2.6/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/daily?dailysteps=7")
    fun getDailyWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<DailyResponse>
    @GET("v2.6/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/hourly?hourlysteps=24")
    fun getHourlyWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<HourlyResponse>
}