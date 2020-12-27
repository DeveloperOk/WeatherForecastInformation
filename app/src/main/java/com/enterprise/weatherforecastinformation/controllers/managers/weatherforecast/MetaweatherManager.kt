package com.enterprise.weatherforecastinformation.controllers.managers.weatherforecast

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.enterprise.weatherforecastinformation.models.weatherforecast.NearCity
import com.enterprise.weatherforecastinformation.models.weatherforecast.NearLocation
import com.enterprise.weatherforecastinformation.models.weatherforecast.WeatherForecastConstants
import com.google.gson.Gson

class MetaweatherManager {

    fun getNearLocations(latitude: Double, longitude: Double, context: Context,
                         doOnResponse: (nearLocationsList: Array<NearLocation>) -> (Unit)){

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
                var nearLocationsList = gson.fromJson(response.toString(), Array<NearLocation>::class.java)

                doOnResponse(nearLocationsList)

            },
            Response.ErrorListener { /*Do on error*/ })

        // Add the request to the RequestQueue.
        queue.add(stringRequest)

    }

    fun getNearCities(nearLocationsList: Array<NearLocation>): ArrayList<NearCity>{

        var nearCitiesList = ArrayList<NearCity>()

        for(nearLocation in nearLocationsList){

            if(nearLocation.location_type == WeatherForecastConstants.LocationTypeCity){

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

}