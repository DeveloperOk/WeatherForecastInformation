package com.enterprise.weatherforecastinformation

import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.enterprise.weatherforecastinformation.controllers.managers.weatherforecast.AppLocationManager

class MainActivity : AppCompatActivity() {

    private var buttonStart: Button? = null
    private var textView: TextView? = null

    private var appLocationManager: AppLocationManager? = null
    private var locationListener: LocationListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonStart = findViewById(R.id.buttonStart)
        textView = findViewById(R.id.textView)

        appLocationManager = AppLocationManager(this, this)

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

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        appLocationManager?.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    private fun addListeners() {
        addButtonListeners()
        addListenerForGPS()
    }

    private fun addButtonListeners() {
        addStartButtonListener()
    }

    private fun addStartButtonListener() {

        buttonStart?.setOnClickListener {

            appLocationManager?.registerListenerForGPS(this@MainActivity)

        }
    }

    private fun addListenerForGPS() {

        // Define a listener that responds to location updates
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // Called when a new location is found by the network location provider.
                useObtainedLocationData(location)
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        appLocationManager?.setAppLocationListener(locationListener)
        appLocationManager?.checkPermissionAndRegisterListenerForGPS(this)

    }

    private fun useObtainedLocationData(location: Location) {

        textView?.setText("Latitude,Longitude: " + location?.latitude + "," + location?.longitude)

        appLocationManager?.removeListenerForGPS()
    }


}