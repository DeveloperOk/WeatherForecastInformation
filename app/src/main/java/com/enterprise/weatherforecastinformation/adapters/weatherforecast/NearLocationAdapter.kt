package com.enterprise.weatherforecastinformation.adapters.weatherforecast

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.enterprise.weatherforecastinformation.R
import com.enterprise.weatherforecastinformation.models.weatherforecast.NearLocation
import com.enterprise.weatherforecastinformation.models.weatherforecast.WeatherForecastConstants
import kotlin.concurrent.thread


class NearLocationAdapter(
    val nearLocationsList: Array<NearLocation>,
    val buttonNearCities: Button?, val buttonNearLocations: Button?
) :
    RecyclerView.Adapter<NearLocationAdapter.NearLocationViewHolder>() {

    class NearLocationViewHolder(
        itemView: View, val buttonNearCities: Button?,
        val buttonNearLocations: Button?
    ) : RecyclerView.ViewHolder(itemView) {

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

        init {

            itemView.setOnClickListener {

                setVisibilityOfButtons()

            }

        }

        private fun setVisibilityOfButtons(){

            buttonNearCities?.visibility = View.VISIBLE
            buttonNearLocations?.visibility = View.VISIBLE

            thread {

                Thread.sleep(WeatherForecastConstants.VisibilityDurationOfButtons)

                //To run on ui thread
                Handler(Looper.getMainLooper()).post(Runnable {

                    buttonNearCities?.visibility = View.GONE
                    buttonNearLocations?.visibility = View.GONE

                })

            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearLocationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.near_location, parent, false)

        return NearLocationViewHolder(view, buttonNearCities, buttonNearLocations)
    }

    override fun getItemCount(): Int {

        return nearLocationsList.size

    }

    override fun onBindViewHolder(holder: NearLocationViewHolder, position: Int) {

        nearLocationsList[position]?.let { holder.bind(it) }

    }
}