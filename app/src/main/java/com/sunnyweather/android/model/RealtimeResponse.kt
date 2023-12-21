package com.sunnyweather.android.model

import com.google.gson.annotations.SerializedName

data class RealtimeResponse(val status : String,val result: RealtimeResult)

data class RealtimeResult(val realtime: Realtime)

data class Realtime(val skycon : String,val temperature : Float,
                    @SerializedName("air_qualtity")val airQualtity : AirQuality)

data class AirQuality(val aqi : AQI)

data class AQI(val chn : Float,val usa : Float)