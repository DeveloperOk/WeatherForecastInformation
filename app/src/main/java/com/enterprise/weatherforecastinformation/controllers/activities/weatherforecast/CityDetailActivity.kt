package com.enterprise.weatherforecastinformation.controllers.activities.weatherforecast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.enterprise.weatherforecastinformation.R
import com.enterprise.weatherforecastinformation.controllers.managers.weatherforecast.AppDateManager
import com.enterprise.weatherforecastinformation.controllers.managers.weatherforecast.MetaweatherManager
import com.enterprise.weatherforecastinformation.models.weatherforecast.MetaweatherWeatherForecastInformation
import com.enterprise.weatherforecastinformation.models.weatherforecast.NearCity
import com.enterprise.weatherforecastinformation.models.weatherforecast.WeatherForecastConstants
import com.google.gson.Gson

class CityDetailActivity : AppCompatActivity() {

    var weatherForecastListBetweenStartAndEndDate:
            ArrayList<MetaweatherWeatherForecastInformation>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_detail)

        var selectedNearCityJson =
            intent.getStringExtra(WeatherForecastConstants.SelectedNearCityJsonKey)

        var gson = Gson()
        var selectedNearCity = gson.fromJson(selectedNearCityJson, NearCity::class.java)

        setTitle(selectedNearCity.title)

        getWeatherDataAndUpdateView(selectedNearCity)

    }

    private fun getWeatherDataAndUpdateView(nearCity: NearCity) {

        val metaweatherManager = MetaweatherManager()

        val firstDayIndex = WeatherForecastConstants.FirstDayIndex
        val lastDayIndex = WeatherForecastConstants.LastDayIndex

        weatherForecastListBetweenStartAndEndDate = ArrayList<MetaweatherWeatherForecastInformation>()

        for (index in firstDayIndex..lastDayIndex) {

            weatherForecastListBetweenStartAndEndDate?.let{
                it.add(MetaweatherWeatherForecastInformation())
            }

            val date = AppDateManager.getCalculatedDate(
                WeatherForecastConstants.DateFormatForLocationDay,
                index
            )

            val inputIndex = index - firstDayIndex
            metaweatherManager?.let {
                nearCity.woeid?.let {
                    metaweatherManager.getWeatherForecast(it, date, this, inputIndex, this::updateView)
                }
            }

        }

    }

    private fun updateView(weatherForecastListForGivenDate: Array<MetaweatherWeatherForecastInformation>, index: Int): Unit {

        if(weatherForecastListForGivenDate.isNullOrEmpty() == false){

            weatherForecastListBetweenStartAndEndDate?.let{

                it[index] = weatherForecastListForGivenDate[WeatherForecastConstants.IndexOfForecastClosestToCurrentDate]

            }

        }

    }

}