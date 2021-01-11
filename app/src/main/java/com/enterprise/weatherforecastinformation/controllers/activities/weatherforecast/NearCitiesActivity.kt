package com.enterprise.weatherforecastinformation.controllers.activities.weatherforecast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.enterprise.weatherforecastinformation.R
import com.enterprise.weatherforecastinformation.adapters.weatherforecast.NearCityAdapter
import com.enterprise.weatherforecastinformation.models.weatherforecast.MetaweatherLocation
import com.enterprise.weatherforecastinformation.models.weatherforecast.NearCity
import com.enterprise.weatherforecastinformation.models.weatherforecast.WeatherForecastConstants


class NearCitiesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_near_cities)

        setTitle(R.string.near_cities_activity_title)

        var nearCitiesList = getNearCities()

        val recyclerViewNearCities: RecyclerView = findViewById(R.id.recyclerViewNearCities)
        recyclerViewNearCities.adapter = NearCityAdapter(nearCitiesList, this)

    }

    private fun getNearCities() : ArrayList<NearCity> {

        var nearCitiesList = ArrayList<NearCity>()

        var nearCitiesListAsMetaweatherLocationList =
            intent.getParcelableArrayListExtra<MetaweatherLocation>(WeatherForecastConstants.NearCitiesKey)

        if(nearCitiesListAsMetaweatherLocationList != null ) {

            for (nearCityAsMetaweatherLocation in nearCitiesListAsMetaweatherLocationList) {

                var nearCity = NearCity()

                nearCity.distance = nearCityAsMetaweatherLocation.distance
                nearCity.title = nearCityAsMetaweatherLocation.title
                nearCity.location_type = nearCityAsMetaweatherLocation.location_type
                nearCity.woeid = nearCityAsMetaweatherLocation.woeid
                nearCity.latt_long = nearCityAsMetaweatherLocation.latt_long

                nearCitiesList.add(nearCity)

            }

        }

        return  nearCitiesList

    }


}