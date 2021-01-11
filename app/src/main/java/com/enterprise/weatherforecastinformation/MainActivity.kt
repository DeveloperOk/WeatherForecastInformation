package com.enterprise.weatherforecastinformation

import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.enterprise.weatherforecastinformation.adapters.weatherforecast.NearLocationAdapter
import com.enterprise.weatherforecastinformation.controllers.activities.weatherforecast.NearCitiesActivity
import com.enterprise.weatherforecastinformation.controllers.managers.weatherforecast.AppConnectivityManager
import com.enterprise.weatherforecastinformation.controllers.managers.weatherforecast.AppLocationManager
import com.enterprise.weatherforecastinformation.controllers.managers.weatherforecast.MetaweatherManager
import com.enterprise.weatherforecastinformation.models.weatherforecast.NearLocation
import com.enterprise.weatherforecastinformation.models.weatherforecast.WeatherForecastConstants


class MainActivity : AppCompatActivity() {

    private var buttonNearLocations: Button? = null
    private var buttonNearCities: Button? = null

    private var appLocationManager: AppLocationManager? = null
    private var locationListener: LocationListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonNearLocations = findViewById(R.id.buttonNearLocations)
        buttonNearCities = findViewById(R.id.buttonNearCities)

        appLocationManager = AppLocationManager(this, this)

        if (AppConnectivityManager.isConnectedToInternet(this) == true) {

            setAndRegisterListenerForGPS(this::useObtainedLocationData)

        } else {

            AppConnectivityManager.showAlertDialogForNoInternetConnection(this)

        }

        addListeners()
    }

    override fun onResume() {

        super.onResume()

        appLocationManager?.registerListenerForGPSWhenReturningFromLocationSettingsPage(this)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        appLocationManager?.onActivityResult(requestCode, resultCode, data)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        appLocationManager?.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    private fun addListeners() {
        addButtonListeners()
    }

    private fun addButtonListeners() {

        addButtonNearLocationsListener()
        addButtonNearCitiesListener()

    }

    private fun addButtonNearLocationsListener() {

        buttonNearLocations?.setOnClickListener {

            if (AppConnectivityManager.isConnectedToInternet(this) == true) {

                setAndRegisterListenerForGPS(this::useObtainedLocationDataOnButtonNearLocations)

            } else {

                AppConnectivityManager.showAlertDialogForNoInternetConnection(this)

            }


        }
    }

    private fun addButtonNearCitiesListener() {

        buttonNearCities?.setOnClickListener {

            if (AppConnectivityManager.isConnectedToInternet(this) == true) {

                setAndRegisterListenerForGPS(this::useObtainedLocationDataOnButtonNearCities)

            } else {

                AppConnectivityManager.showAlertDialogForNoInternetConnection(this)

            }

        }
    }

    private fun setAndRegisterListenerForGPS(doOnResponse: (location: Location) -> (Unit)) {

        // Define a listener that responds to location updates
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // Called when a new location is found by the network location provider.
                doOnResponse(location)
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        appLocationManager?.setAppLocationListener(locationListener)
        appLocationManager?.checkPermissionAndRegisterListenerForGPS(this)

    }

    private fun useObtainedLocationData(location: Location) {

        appLocationManager?.removeListenerForGPS()

        var metaweatherManager = MetaweatherManager()
        metaweatherManager.getNearLocations(
            location.latitude, location.longitude, this,
            this::setAdapterOfRecyclerView
        )

    }

    private fun setAdapterOfRecyclerView(nearLocationsList: Array<NearLocation>): Unit {

        val recyclerViewNearLocations: RecyclerView = findViewById(R.id.recyclerViewNearLocations)
        recyclerViewNearLocations.adapter = NearLocationAdapter(nearLocationsList)

    }

    private fun useObtainedLocationDataOnButtonNearLocations(location: Location) {

        appLocationManager?.removeListenerForGPS()

        var metaweatherManager = MetaweatherManager()
        metaweatherManager.getNearLocations(
            location.latitude, location.longitude, this,
            this::setAdapterOfRecyclerViewOnButtonNearLocations
        )

    }

    private fun setAdapterOfRecyclerViewOnButtonNearLocations(nearLocationsList: Array<NearLocation>): Unit {

        val recyclerViewNearLocations: RecyclerView = findViewById(R.id.recyclerViewNearLocations)
        recyclerViewNearLocations.adapter = NearLocationAdapter(nearLocationsList)

    }

    private fun useObtainedLocationDataOnButtonNearCities(location: Location) {

        appLocationManager?.removeListenerForGPS()

        var metaweatherManager = MetaweatherManager()
        metaweatherManager.getNearLocations(
            location.latitude, location.longitude, this,
            this::getNearCitiesAndLaunchNearCitiesActivity
        )

    }

    private fun getNearCitiesAndLaunchNearCitiesActivity(nearLocationsList: Array<NearLocation>): Unit {

        var metaweatherManager = MetaweatherManager()
        var nearCitiesList = metaweatherManager.getNearCities(nearLocationsList)

        val intent = Intent(this, NearCitiesActivity::class.java)
        intent.putExtra(WeatherForecastConstants.NearCitiesKey, nearCitiesList)

        startActivity(intent)

    }

}