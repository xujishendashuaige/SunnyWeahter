package com.sunnyweather.android.model

import com.sunnyweather.android.logic.model.RealtimeResponse

class Weather(val realtime: RealtimeResponse.Realtime, val daily: DailyResponse.Daily)
