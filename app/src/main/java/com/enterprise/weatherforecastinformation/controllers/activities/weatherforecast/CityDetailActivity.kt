package com.enterprise.weatherforecastinformation.controllers.activities.weatherforecast

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enterprise.weatherforecastinformation.R
import com.enterprise.weatherforecastinformation.adapters.weatherforecast.CityDetailAdapter
import com.enterprise.weatherforecastinformation.controllers.managers.weatherforecast.AppDateManager
import com.enterprise.weatherforecastinformation.controllers.managers.weatherforecast.MetaweatherManager
import com.enterprise.weatherforecastinformation.models.weatherforecast.MetaweatherLocation
import com.enterprise.weatherforecastinformation.models.weatherforecast.MetaweatherWeatherForecastInformation
import com.enterprise.weatherforecastinformation.models.weatherforecast.NearCity
import com.enterprise.weatherforecastinformation.models.weatherforecast.WeatherForecastConstants


class CityDetailActivity : AppCompatActivity() {

    private var weatherForecastListBetweenStartAndEndDate:
            ArrayList<MetaweatherWeatherForecastInformation>? = null

    private var weatherStateAbbreviationImageMap: MutableMap<String, Bitmap>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_detail)

        var selectedNearCity = getSelectedNearCity()

        setTitle(selectedNearCity.title)

        getWeatherDataAndUpdateView(selectedNearCity)

    }


    private fun getSelectedNearCity() : NearCity{

        var selectedNearCityAsMetaweatherLocation =
            intent.getParcelableExtra(WeatherForecastConstants.SelectedNearCityKey) as MetaweatherLocation

        var selectedNearCity = NearCity()

        selectedNearCity.distance = selectedNearCityAsMetaweatherLocation.distance
        selectedNearCity.title = selectedNearCityAsMetaweatherLocation.title
        selectedNearCity.location_type = selectedNearCityAsMetaweatherLocation.location_type
        selectedNearCity.woeid = selectedNearCityAsMetaweatherLocation.woeid
        selectedNearCity.latt_long = selectedNearCityAsMetaweatherLocation.latt_long

        return selectedNearCity

    }


    private fun getWeatherDataAndUpdateView(nearCity: NearCity) {

        val metaweatherManager = MetaweatherManager()

        val firstDayIndex = WeatherForecastConstants.FirstDayIndex
        val lastDayIndex = WeatherForecastConstants.LastDayIndex

        weatherForecastListBetweenStartAndEndDate = ArrayList<MetaweatherWeatherForecastInformation>()
        weatherStateAbbreviationImageMap = mutableMapOf<String, Bitmap>()

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
                    metaweatherManager.getWeatherForecast(it, date, this, inputIndex, this::updateWeatherForecastList)
                }
            }

        }

    }

    private fun updateWeatherForecastList(weatherForecastListForGivenDate: Array<MetaweatherWeatherForecastInformation>, index: Int): Unit {

        if(weatherForecastListForGivenDate.isNullOrEmpty() == false){

            weatherForecastListBetweenStartAndEndDate?.let{

                it[index] = weatherForecastListForGivenDate[WeatherForecastConstants.IndexOfForecastClosestToCurrentDate]

                val metaweatherManager = MetaweatherManager()
                weatherForecastListForGivenDate[WeatherForecastConstants.IndexOfForecastClosestToCurrentDate].weather_state_abbr?.let { it1 ->

                    if(weatherStateAbbreviationImageMap?.containsKey(it1) == false) {

                        metaweatherManager.getImage(
                            it1, this, WeatherForecastConstants.ImageMaxWidth,
                            WeatherForecastConstants.ImageMaxHeight,
                            this::updateWeatherStateAbbreviationImageMap
                        )

                    } else {

                        updateView()

                    }

                }

            }

        }

    }

    private fun updateWeatherStateAbbreviationImageMap(weatherStateAbbreviation: String, bitmap:Bitmap): Unit {

        weatherStateAbbreviationImageMap?.put(weatherStateAbbreviation, bitmap)

        updateView()

    }

    private fun updateView() {

        val recyclerViewCityDetail: RecyclerView = findViewById(R.id.recyclerViewCityDetail)

        recyclerViewCityDetail.layoutManager = GridLayoutManager(this, WeatherForecastConstants.NumberOfColumnsOfRecyclerViewCityDetail)

        recyclerViewCityDetail.adapter =
            weatherForecastListBetweenStartAndEndDate?.let { weatherStateAbbreviationImageMap?.let { it1 ->
                CityDetailAdapter(it,
                    it1, this
                )
            } }


    }


}