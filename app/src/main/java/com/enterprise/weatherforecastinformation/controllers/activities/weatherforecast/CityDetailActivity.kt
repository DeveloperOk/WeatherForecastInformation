package com.enterprise.weatherforecastinformation.controllers.activities.weatherforecast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.enterprise.weatherforecastinformation.R
import com.enterprise.weatherforecastinformation.models.weatherforecast.NearCity
import com.enterprise.weatherforecastinformation.models.weatherforecast.WeatherForecastConstants
import com.google.gson.Gson

class CityDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_detail)

        var selectedNearCityJson = intent.getStringExtra(WeatherForecastConstants.SelectedNearCityJsonKey)

        var gson = Gson()
        var selectedNearCity = gson.fromJson(selectedNearCityJson, NearCity::class.java)

        setTitle(selectedNearCity.title)

    }
}