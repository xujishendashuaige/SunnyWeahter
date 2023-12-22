package com.sunnyweather.android.Repository

import android.content.Context
import androidx.lifecycle.liveData
import com.sunnyweather.android.dao.PlaceDao
import com.sunnyweather.android.model.Place
import com.sunnyweather.android.model.Weather
import com.sunnyweather.android.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

object Repository {
    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
            if(placeResponse.status == "ok"){
                val places = placeResponse.places
                Result.success(places)
            }else{
                Result.failure(RuntimeException("response status is${placeResponse.status}"))
            }
        }

    fun refreshWeather(lng : String, lat : String) = fire(Dispatchers.IO) {
            coroutineScope {
                val deferredDaily = async {
                    SunnyWeatherNetwork.getDailyWeather(lng, lat)
                }
                val deferredRealtime = async {
                    SunnyWeatherNetwork.getRealtimeWeather(lng, lat)
                }
                val deferredHourly = async {
                    SunnyWeatherNetwork.getHourkyWeather(lng, lat)
                }
                val dailyResponse = deferredDaily.await()
                val realtimeResponse = deferredRealtime.await()
                val hourlyResponse =deferredHourly.await()
                if(dailyResponse.status == "ok" && realtimeResponse.status == "ok"){
                    val weather = Weather(realtimeResponse.result.realtime,
                                          dailyResponse.result.daily)
                    Result.success(weather)
                }else{
                    Result.failure(
                        RuntimeException(
                            "realtime response status is ${realtimeResponse.status}" +
                            "daily response status is ${dailyResponse.status}"
                        )
                    )
                }
            }
    }

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>)=
            liveData<Result<T>>(context) {
                val result = try {
                    block()
                }catch (e : Exception){
                    Result.failure<T>(e)
                }
                emit(result)
            }

    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isSavePlace() = PlaceDao.isPlaceSaved()
}