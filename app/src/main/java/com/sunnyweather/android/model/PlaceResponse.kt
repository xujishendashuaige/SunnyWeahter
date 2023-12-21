package com.sunnyweather.android.model

import com.google.gson.annotations.SerializedName

data class PlaceResponse(val status : String, val places: List<Place>)

data class Place(@SerializedName("formatted_address")val address: String,val id : String,
                 @SerializedName("place_id")val placeId : String,
                 val name : String, val location : Location)

data class Location(val lng : String, val lat : String)