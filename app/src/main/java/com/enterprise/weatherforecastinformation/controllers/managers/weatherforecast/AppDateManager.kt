package com.enterprise.weatherforecastinformation.controllers.managers.weatherforecast


import com.enterprise.weatherforecastinformation.models.weatherforecast.WeatherForecastConstants
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class AppDateManager {

    companion object {

        fun getCalculatedDate(dateFormat: String, days: Int): String {

            val calendar = Calendar.getInstance()
            val simpleDateFormat = SimpleDateFormat(dateFormat)
            calendar.add(Calendar.DAY_OF_YEAR, days)
            val calculatedDate = simpleDateFormat.format(Date(calendar.getTimeInMillis()))

            return calculatedDate

        }

        fun getNameOfDay(inputDate: String): String{

            val inputSimpleDateFormat = SimpleDateFormat(WeatherForecastConstants.CityDetailInputDateFormat)
            val date = inputSimpleDateFormat.parse(inputDate)
            val outputSimpleDateFormat: DateFormat = SimpleDateFormat(WeatherForecastConstants.CityDetailOutputDateFormat)
            val nameOfDay: String = outputSimpleDateFormat.format(date)

            return nameOfDay

        }

    }

}