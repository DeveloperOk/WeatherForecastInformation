package com.enterprise.weatherforecastinformation.adapters.weatherforecast

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.enterprise.weatherforecastinformation.R
import com.enterprise.weatherforecastinformation.controllers.activities.weatherforecast.CityDetailActivity
import com.enterprise.weatherforecastinformation.models.weatherforecast.NearCity
import com.enterprise.weatherforecastinformation.models.weatherforecast.NearLocation
import com.enterprise.weatherforecastinformation.models.weatherforecast.WeatherForecastConstants
import com.google.gson.Gson

class NearLocationAdapter(val nearLocationsList: Array<NearLocation>) :
    RecyclerView.Adapter<NearLocationAdapter.NearLocationViewHolder>() {

    class NearLocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val textViewDistanceValue: TextView = itemView.findViewById(R.id.textViewDistanceValue)
        private val textViewTitleValue: TextView = itemView.findViewById(R.id.textViewTitleValue)
        private val textViewLocationTypeValue: TextView = itemView.findViewById(R.id.textViewLocationTypeValue)
        private val textViewWoeidValue: TextView = itemView.findViewById(R.id.textViewWoeidValue)
        private val textViewLattLongValue: TextView = itemView.findViewById(R.id.textViewLattLongValue)

        fun bind(nearLocation: NearLocation) {

            textViewDistanceValue.text = nearLocation.distance.toString()
            textViewTitleValue.text = nearLocation.title
            textViewLocationTypeValue.text = nearLocation.location_type
            textViewWoeidValue.text = nearLocation.woeid.toString()
            textViewLattLongValue.text = nearLocation.latt_long

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearLocationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.near_location, parent, false)

        return NearLocationViewHolder(view)
    }

    override fun getItemCount(): Int {

        return nearLocationsList.size

    }

    override fun onBindViewHolder(holder: NearLocationViewHolder, position: Int) {

        nearLocationsList[position]?.let { holder.bind(it) }

    }
}