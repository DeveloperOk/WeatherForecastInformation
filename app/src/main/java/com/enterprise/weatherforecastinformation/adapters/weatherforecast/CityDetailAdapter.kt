package com.enterprise.weatherforecastinformation.adapters.weatherforecast

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.enterprise.weatherforecastinformation.R
import com.enterprise.weatherforecastinformation.controllers.managers.weatherforecast.AppDateManager
import com.enterprise.weatherforecastinformation.models.weatherforecast.MetaweatherWeatherForecastInformation
import kotlin.math.roundToLong

class CityDetailAdapter(val weatherForecastListBetweenStartAndEndDate:
                        ArrayList<MetaweatherWeatherForecastInformation>,
                        val weatherStateAbbreviationImageMap: MutableMap<String, Bitmap>,
                        val context: Context) :
    RecyclerView.Adapter<CityDetailAdapter.CityDetailViewHolder>() {

    class CityDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val textViewApplicableDate: TextView = itemView.findViewById(R.id.textViewApplicableDate)
        private val textViewNameOfDay: TextView = itemView.findViewById(R.id.textViewNameOfDay)
        private val textViewWeatherStateName: TextView = itemView.findViewById(R.id.textViewWeatherStateName)
        private val imageView: ImageView = itemView.findViewById(R.id.imageViewWeatherForecast)
        private val textViewTemperature: TextView = itemView.findViewById(R.id.textViewTemperature)
        private val textViewMaximumTemperature: TextView = itemView.findViewById(R.id.textViewMaximumTemperature)
        private val textViewMinimumTemperature: TextView = itemView.findViewById(R.id.textViewMinimumTemperature)
        private val textViewWindSpeed: TextView = itemView.findViewById(R.id.textViewWindSpeed)
        private val textViewHumidity: TextView = itemView.findViewById(R.id.textViewHumidity)

        fun bind(metaweatherWeatherForecastInformation: MetaweatherWeatherForecastInformation,
                 weatherStateAbbreviationImageMap: MutableMap<String, Bitmap>, context: Context
        ) {

            textViewApplicableDate.text = metaweatherWeatherForecastInformation.applicable_date

            textViewNameOfDay.text = metaweatherWeatherForecastInformation.applicable_date?.let {
                AppDateManager.getNameOfDay(
                    it
                )
            }

            textViewWeatherStateName.text = metaweatherWeatherForecastInformation.weather_state_name

            imageView.setImageBitmap(weatherStateAbbreviationImageMap.get(metaweatherWeatherForecastInformation.weather_state_abbr))

            textViewTemperature.text = buildString {
                append(context.getString(R.string.city_detail_temperature))
                append(context.getString(R.string.city_detail_space))
                append(metaweatherWeatherForecastInformation.the_temp?.roundToLong().toString())
            }

            textViewMaximumTemperature.text = buildString {
                append(context.getString(R.string.city_detail_maximum_temperature))
                append(context.getString(R.string.city_detail_space))
                append(metaweatherWeatherForecastInformation.max_temp?.roundToLong().toString())
            }

            textViewMinimumTemperature.text = buildString {
                append(context.getString(R.string.city_detail_minimum_temperature))
                append(context.getString(R.string.city_detail_space))
                append(metaweatherWeatherForecastInformation.min_temp?.roundToLong().toString())
            }

            textViewWindSpeed.text = buildString {
                append(context.getString(R.string.city_detail_wind_speed))
                append(context.getString(R.string.city_detail_space))
                append(metaweatherWeatherForecastInformation.wind_speed?.roundToLong().toString())
            }

            textViewHumidity.text = buildString {
                append(context.getString(R.string.city_detail_humidity))
                append(context.getString(R.string.city_detail_space))
                append(metaweatherWeatherForecastInformation.humidity?.roundToLong().toString())
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityDetailViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.weather_forecast, parent, false)

        return CityDetailViewHolder(view)
    }

    override fun getItemCount(): Int {

        return weatherForecastListBetweenStartAndEndDate.size

    }

    override fun onBindViewHolder(holder: CityDetailViewHolder, position: Int) {

        weatherForecastListBetweenStartAndEndDate[position]?.let { holder.bind(it, weatherStateAbbreviationImageMap, context) }

    }
}