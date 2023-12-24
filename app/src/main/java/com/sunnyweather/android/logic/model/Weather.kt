package com.sunnyweather.android.logic.model

import com.sunnyweather.android.logic.model.DailyResponse
import com.sunnyweather.android.logic.model.HourlyResponse
import com.sunnyweather.android.logic.model.RealtimeResponse

data class Weather(val realtime: RealtimeResponse.Realtime, val hourly: HourlyResponse.Hourly, val daily: DailyResponse.Daily)
