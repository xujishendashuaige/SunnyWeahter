package com.sunnyweather.android.dao

import android.content.Context
import android.provider.Settings.Global.putString
import androidx.core.content.edit
import com.google.gson.Gson
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.model.Place

object PlaceDao {
    fun savePlace(place : Place){
            shardPreferences().edit {
            putString("place", Gson().toJson(place))
        }
    }

    fun getSavedPlace() : Place{
        val placeJson = shardPreferences().getString("place","")
        return Gson().fromJson(placeJson,Place::class.java)
    }

    fun isPlaceSaved() = shardPreferences().contains("place")
    private fun shardPreferences() = SunnyWeatherApplication.context.
    getSharedPreferences("sunng_weather",Context.MODE_PRIVATE)
}