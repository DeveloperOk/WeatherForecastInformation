package com.enterprise.weatherforecastinformation.controllers.managers.weatherforecast

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.enterprise.weatherforecastinformation.models.weatherforecast.MetaweatherWeatherForecastInformation
import com.enterprise.weatherforecastinformation.models.weatherforecast.NearCity
import com.enterprise.weatherforecastinformation.models.weatherforecast.NearLocation
import com.enterprise.weatherforecastinformation.models.weatherforecast.WeatherForecastConstants
import com.google.gson.Gson

class MetaweatherManager {

    fun getNearLocations(
        latitude: Double, longitude: Double, context: Context,
        doOnResponse: (nearLocationsList: Array<NearLocation>) -> (Unit)
    ) {

        var absoluteUrl = buildString {
            append(WeatherForecastConstants.BaseUrl)
            append(WeatherForecastConstants.PartOfRelativeUrlForLocationSearch)
            append(latitude)
            append(WeatherForecastConstants.LatitudeLongitudeSeparator)
            append(longitude)
        }

        val queue = Volley.newRequestQueue(context)

        val stringRequest = StringRequest(
            Request.Method.GET, absoluteUrl,
            Response.Listener<String> { response ->

                var gson = Gson()
                var nearLocationsList =
                    gson.fromJson(response.toString(), Array<NearLocation>::class.java)

                doOnResponse(nearLocationsList)

            },
            Response.ErrorListener { /*Do on error*/ })

        // Add the request to the RequestQueue.
        queue.add(stringRequest)

    }

    fun getNearCities(nearLocationsList: Array<NearLocation>): ArrayList<NearCity> {

        var nearCitiesList = ArrayList<NearCity>()

        for (nearLocation in nearLocationsList) {

            if (nearLocation.location_type == WeatherForecastConstants.LocationTypeCity) {

                var nearCity = NearCity()

                nearCity.distance = nearLocation.distance
                nearCity.title = nearLocation.title
                nearCity.location_type = nearLocation.location_type
                nearCity.woeid = nearLocation.woeid
                nearCity.latt_long = nearLocation.latt_long

                nearCitiesList.add(nearCity)

            }

        }

        return nearCitiesList

    }

    fun getWeatherForecast(
        woeid: Int, date: String, context: Context, index: Int,
        doOnResponse: (weatherForecastList: Array<MetaweatherWeatherForecastInformation>, index: Int) -> (Unit)
    ) {

        var absoluteUrl = generateAbsoluteUrlForGetWeatherForecast(woeid, date)

        val queue = Volley.newRequestQueue(context)

        val stringRequest = StringRequest(
            Request.Method.GET, absoluteUrl,
            Response.Listener<String> { response ->

                var gson = Gson()
                var weatherForecastList =
                    gson.fromJson(response.toString(), Array<MetaweatherWeatherForecastInformation>::class.java)

                doOnResponse(weatherForecastList, index)

            },
            Response.ErrorListener { /*Do on error*/ })

        // Add the request to the RequestQueue.
        queue.add(stringRequest)

    }

    private fun generateAbsoluteUrlForGetWeatherForecast(
        woeid: Int,
        date: String
    ): String {

        var absoluteUrl = buildString {
            append(WeatherForecastConstants.BaseUrl)
            append(WeatherForecastConstants.PartOfRelativeUrlForLocationDay)
            append(woeid)
            append(WeatherForecastConstants.PunctuationMarkSlash)
            append(date)
            append(WeatherForecastConstants.PunctuationMarkSlash)
        }

        return absoluteUrl

    }

    fun getImage(
        weatherStateAbbreviation: String, context: Context, maxWidth: Int, maxHeight: Int,
        doOnResponse: (weatherStateAbbreviation: String, bitmap: Bitmap) -> (Unit)
    ) {

        val absoluteUrl = generateAbsoluteUrlForGetImage(weatherStateAbbreviation)

        val queue = Volley.newRequestQueue(context)

        val imageRequest = ImageRequest(absoluteUrl,
            Response.Listener<Bitmap> { response ->

                doOnResponse(weatherStateAbbreviation, response)

            }, maxWidth, maxHeight, ImageView.ScaleType.CENTER, Bitmap.Config.ARGB_8888,
            Response.ErrorListener { /*Do on error*/ })

        // Add the request to the RequestQueue.
        queue.add(imageRequest)

    }

    private fun generateAbsoluteUrlForGetImage(
        weatherStateAbbreviation: String
    ): String {

        var absoluteUrl = buildString {
            append(WeatherForecastConstants.BaseUrl)
            append(WeatherForecastConstants.PartOfRelativeUrlForGettingImage)
            append(weatherStateAbbreviation)
            append(WeatherForecastConstants.ImageFileExtension)
        }

        return absoluteUrl

    }

}