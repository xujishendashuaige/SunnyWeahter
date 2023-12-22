package com.sunnyweather.android.weather

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.Validators.or
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.model.getSky
import com.sunnyweather.android.model.HourlyForecast
import com.sunnyweather.android.model.Weather
import java.text.SimpleDateFormat
import java.util.Locale
import android.graphics.Color
class WeatherActivity : AppCompatActivity() {
    private lateinit var placeName: TextView

    private lateinit var nowLayout: RelativeLayout

    private lateinit var currentTemp : TextView

    private lateinit var currentSky : TextView

    private lateinit var currentAQI : TextView

    private lateinit var forecastLayout: LinearLayout

    private lateinit var coldRiskText: TextView

    private lateinit var dressingText: TextView

    private lateinit var ultravioletText: TextView

    private lateinit var carWashingText: TextView

    private lateinit var weatherLayout: ScrollView

    private lateinit var navBtn: Button



    val viewmodel by lazy { ViewModelProvider(this)[WeatherViewModel::class.java] }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val decorView = window.decorView
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT
        setContentView(R.layout.activity_weather)

        placeName = findViewById(R.id.placeName)
        nowLayout = findViewById(R.id.nowLayout)
        currentTemp = findViewById(R.id.currentTemp)
        currentSky = findViewById(R.id.currentSky)
        currentAQI = findViewById(R.id.currentAQI)
        forecastLayout = findViewById(R.id.forecastLayout)
        coldRiskText = findViewById(R.id.coldRiskText)
        dressingText = findViewById(R.id.dressingText)
        ultravioletText = findViewById(R.id.ultravioletText)
        carWashingText = findViewById(R.id.carWashingText)
        weatherLayout = findViewById(R.id.weatherLayout)
        navBtn = findViewById(R.id.navBtn)

        if(viewmodel.locationLat.isEmpty()){
            viewmodel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if(viewmodel.locationLng.isEmpty()){
            viewmodel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if(viewmodel.placeName.isEmpty()){
            viewmodel.placeName = intent.getStringExtra("place_name") ?: ""
        }

        viewmodel.weatherLiveData.observe(this, Observer {result ->
            val weather = result.getOrNull()
            if(weather != null){
                showWeatherInfo(weather)
            }else{
                Toast.makeText(this,"无法成功获取天气信息",Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
        viewmodel.refreshWeather(viewmodel.locationLng,viewmodel.locationLat)
    }
    fun refreshWeather() {
        viewmodel.refreshWeather(viewmodel.locationLng, viewmodel.locationLat)
    }

    @SuppressLint("MissingInflatedId")
    private fun showWeatherInfo(weather: Weather) {
        placeName.text = viewmodel.placeName
        val realtime = weather.realtime
        val daily = weather.daily

        // 填充now.xml布局中数据
        val currentTempText = "${realtime.temperature.toInt()} ℃"
        currentTemp.text = currentTempText
        currentSky.text = getSky(realtime.skycon).info
        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        currentAQI.text = currentPM25Text
        nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)

        // 填充forecast.xml布局中的数据
        forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false)
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text = simpleDateFormat.format(skycon.date)
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            temperatureInfo.text = tempText
            forecastLayout.addView(view)
        }

        // 填充life_index.xml布局中的数据
        val lifeIndex = daily.lifeIndex
        coldRiskText.text = lifeIndex.coldRisk[0].desc
        dressingText.text = lifeIndex.dressing[0].desc
        ultravioletText.text = lifeIndex.ultraviolet[0].desc
        carWashingText.text = lifeIndex.carWashing[0].desc
        weatherLayout.visibility = View.VISIBLE
    }
}