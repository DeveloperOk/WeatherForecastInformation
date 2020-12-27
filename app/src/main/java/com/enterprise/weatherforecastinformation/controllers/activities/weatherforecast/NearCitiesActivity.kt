package com.enterprise.weatherforecastinformation.controllers.activities.weatherforecast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.enterprise.weatherforecastinformation.R
import com.enterprise.weatherforecastinformation.adapters.weatherforecast.NearCityAdapter
import com.enterprise.weatherforecastinformation.models.weatherforecast.NearCity
import com.enterprise.weatherforecastinformation.models.weatherforecast.WeatherForecastConstants
import com.google.gson.Gson

class NearCitiesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_near_cities)

        setTitle(R.string.near_cities_activity_title)

        var nearCitiesListJson = intent.getStringExtra(WeatherForecastConstants.NearCitiesKey)

        var gson = Gson()
        var nearCitiesList = gson.fromJson(nearCitiesListJson, Array<NearCity>::class.java)

        val recyclerViewNearCities: RecyclerView = findViewById(R.id.recyclerViewNearCities)
        recyclerViewNearCities.adapter = NearCityAdapter(nearCitiesList, this)

    }

}