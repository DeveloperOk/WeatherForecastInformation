package com.enterprise.weatherforecastinformation.adapters.weatherforecast

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.enterprise.weatherforecastinformation.models.weatherforecast.NearCity
import com.enterprise.weatherforecastinformation.R
import com.enterprise.weatherforecastinformation.controllers.activities.weatherforecast.CityDetailActivity
import com.enterprise.weatherforecastinformation.models.weatherforecast.WeatherForecastConstants
import com.google.gson.Gson

class NearCityAdapter(val nearCitiesList: Array<NearCity>, val context: Context) :
    RecyclerView.Adapter<NearCityAdapter.NearCityViewHolder>() {

    class NearCityViewHolder(itemView: View, nearCitiesList: Array<NearCity>, context: Context) : RecyclerView.ViewHolder(itemView) {

        private val textViewDistanceValue: TextView = itemView.findViewById(R.id.textViewDistanceValue)
        private val textViewTitleValue: TextView = itemView.findViewById(R.id.textViewTitleValue)
        private val textViewWoeidValue: TextView = itemView.findViewById(R.id.textViewWoeidValue)
        private val textViewLattLongValue: TextView = itemView.findViewById(R.id.textViewLattLongValue)

        fun bind(nearCity: NearCity) {

            textViewDistanceValue.text = nearCity.distance.toString()
            textViewTitleValue.text = nearCity.title
            textViewWoeidValue.text = nearCity.woeid.toString()
            textViewLattLongValue.text = nearCity.latt_long

        }

        init{

            itemView.setOnClickListener{

                var selectedNearCity = nearCitiesList[adapterPosition]

                var gson = Gson()
                var selectedNearCityJson = gson.toJson(selectedNearCity)

                val intent = Intent(context, CityDetailActivity::class.java).apply {
                    putExtra(WeatherForecastConstants.SelectedNearCityJsonKey, selectedNearCityJson)
                }

                context.startActivity(intent)

            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearCityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.near_city, parent, false)

        return NearCityViewHolder(view, nearCitiesList, context)
    }

    override fun getItemCount(): Int {

        return nearCitiesList.size

    }

    override fun onBindViewHolder(holder: NearCityViewHolder, position: Int) {

        nearCitiesList[position]?.let { holder.bind(it) }

    }
}