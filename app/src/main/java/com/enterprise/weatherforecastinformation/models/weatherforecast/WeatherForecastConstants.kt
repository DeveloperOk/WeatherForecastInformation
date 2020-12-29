package com.enterprise.weatherforecastinformation.models.weatherforecast

class WeatherForecastConstants {

    companion object{

        val REQUEST_CHECK_SETTINGS: Int = 1

        val SmallestDisplacement: Float = 0f
        val Interval: Long = 0
        val FastestInterval: Long = 0

        const val MinTime: Long = 0
        const val MinDistance: Float = 0f

        val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: Int = 0

        val BaseUrl: String = "https://www.metaweather.com"
        val PartOfRelativeUrlForLocationSearch: String = "/api/location/search/?lattlong="
        val LatitudeLongitudeSeparator: String = ","
        val PartOfRelativeUrlForLocationDay: String = "/api/location/"
        val PunctuationMarkSlash: String = "/"
        val DateFormatForLocationDay = "yyyy/MM/dd"
        val FirstDayIndex:  Int = 0
        val LastDayIndex: Int = 7
        val IndexOfForecastClosestToCurrentDate = 0

        val LocationTypeCity = "City"

        val NearCitiesKey = "NearCitiesKey"
        val SelectedNearCityJsonKey = "SelectedNearCityJsonKey"

    }

}