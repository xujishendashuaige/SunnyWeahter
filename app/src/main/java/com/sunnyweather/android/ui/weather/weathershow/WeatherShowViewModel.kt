package com.sunnyweather.android.ui.weather.weathershow

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.sunnyweather.android.logic.model.Location
import com.sunnyweather.android.logic.repository.Repository

class WeatherShowViewModel: ViewModel() {

    private val locationLiveData = MutableLiveData<Location>()

    var locationLng = ""

    var locationLat = ""

    var placeName = ""

    var placeAddress = ""

    var placeRealtimeTem = -100

    var placeSkycon = ""

    var isUpdatePlaceManage = false

    val weatherLiveData = locationLiveData.switchMap { location ->
        Repository.refreshWeather(location.lng,location.lat)
    }

    fun refreshWeather(lng: String, lat: String) {
        locationLiveData.value = Location(lng, lat)
    }

}