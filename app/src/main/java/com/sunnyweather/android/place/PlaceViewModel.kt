package com.sunnyweather.android.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sunnyweather.android.Repository.Repository
import com.sunnyweather.android.model.Place
import androidx.lifecycle.switchMap

class PlaceViewModel : ViewModel() {
    private val searchLiveData = MutableLiveData<String>()

    val placeList = ArrayList<Place>()

    val placeLiveData = searchLiveData.switchMap{
        query -> Repository.searchPlaces(query)
    }

    fun searchPlace(query: String){
        searchLiveData.value = query
    }
}