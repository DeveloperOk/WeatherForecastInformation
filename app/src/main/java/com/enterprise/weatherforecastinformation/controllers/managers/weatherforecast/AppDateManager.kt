package com.enterprise.weatherforecastinformation.controllers.managers.weatherforecast


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

    }

}