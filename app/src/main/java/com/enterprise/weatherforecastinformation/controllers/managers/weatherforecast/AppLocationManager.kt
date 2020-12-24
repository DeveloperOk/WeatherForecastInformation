package com.enterprise.weatherforecastinformation.controllers.managers.weatherforecast

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.enterprise.weatherforecastinformation.R
import com.enterprise.weatherforecastinformation.models.weatherforecast.WeatherForecastConstants
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task

class AppLocationManager {

    private var locationManager: LocationManager? = null
    private var context: Context? = null
    private var activity: Activity? = null

    private var locationListener: LocationListener? = null
    private var isLocationUpdateActive = false

    private var locationSettingsRequestBuilder: LocationSettingsRequest.Builder? = null
    private var locationRequestPriorityHighAccuracy: LocationRequest? = null

    private var taskLocationSettingsResponse: Task<LocationSettingsResponse>? = null
    private var resolvable: ResolvableApiException? = null

    private var previousStatusIsProviderEnabled = false
    private var currentStatusIsProviderEnabled = false

    private var alertDialogBuilderToTurnOnGPS: AlertDialog.Builder? = null
    private var alertDialogToTurnOnGPS: AlertDialog? = null

    private var comingFromLocationSettingsPageFlag = false

    constructor(inputContext: Context?, activity: Activity?) {

        context = inputContext
        this.activity = activity

        locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    }

    fun setAppLocationListener(locationListener: LocationListener?) {

        this.locationListener = locationListener

    }

    fun registerListenerForGPS(activity: Activity?) {

        this.activity = activity
        ifGPSIsOffAskUserToTurnOnAndRegisterListenerForGPS(activity)

    }

    fun removeListenerForGPS() {

        if (locationListener != null) {

            locationManager?.removeUpdates(locationListener)
            isLocationUpdateActive = false

        }

    }

    fun registerListenerForGPSWhenReturningFromLocationSettingsPage(activity: Activity?) {

        this.activity = activity

        if (getComingFromLocationSettingsPageFlag() == true) {

            registerListenerForGPS(activity)

        }

    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val states: LocationSettingsStates = LocationSettingsStates.fromIntent(data)
        when (requestCode) {
            WeatherForecastConstants.REQUEST_CHECK_SETTINGS -> {
                currentStatusIsProviderEnabled = isProviderEnabled(LocationManager.GPS_PROVIDER)
                if (currentStatusIsProviderEnabled == true) {
                    registerListenerForGPSWhenGPSIsOn()
                }
            }
        }

    }

    fun checkPermissionAndRegisterListenerForGPS(activity: Activity?) {

        this.activity = activity

        if (context?.let { ContextCompat.
            checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) }
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // No explanation needed; request the permission
            if (activity != null) {
                ActivityCompat.requestPermissions(
                    activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    WeatherForecastConstants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
                )
            }

        } else {

            // Permission has already been granted
            registerListenerForGPS(activity)

        }

    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                   grantResults: IntArray) {

        when (requestCode) {

            WeatherForecastConstants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {

                    // permission was granted! Do the
                    // ACCESS_FINE_LOCATION-related task you need to do.
                    registerListenerForGPS(this.activity)

                } else {

                    // permission denied! Disable the
                    // functionality that depends on this permission.
                }

            }

        }

    }


    private fun getComingFromLocationSettingsPageFlag(): Boolean {

        return comingFromLocationSettingsPageFlag

    }

    private fun setComingFromLocationSettingsPageFlag(comingFromLocationSettingsPageFlag: Boolean) {

        this.comingFromLocationSettingsPageFlag = comingFromLocationSettingsPageFlag

    }

    private fun instantiateAlertDialogAndShow(activity: Activity) {

        alertDialogBuilderToTurnOnGPS = AlertDialog.Builder(activity)
        alertDialogBuilderToTurnOnGPS?.setTitle(R.string.turn_on_gps_dialog_title)
        alertDialogBuilderToTurnOnGPS?.setMessage(R.string.turn_on_gps_dialog_message)

        alertDialogBuilderToTurnOnGPS?.setPositiveButton(
            R.string.turn_on_gps_dialog_positive_button,
            DialogInterface.OnClickListener { dialog, id ->
                comingFromLocationSettingsPageFlag = true
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                activity.startActivity(intent) })

        alertDialogBuilderToTurnOnGPS?.setNegativeButton(
            R.string.turn_on_gps_dialog_negative_button,
            DialogInterface.OnClickListener { dialog, id -> })

        alertDialogToTurnOnGPS = alertDialogBuilderToTurnOnGPS!!.create()
        alertDialogToTurnOnGPS?.show()

    }

    private fun isProviderEnabled(provider: String?): Boolean {

        var retVal = false
        if (locationManager != null) {
            if (locationManager!!.isProviderEnabled(provider)) {
                retVal = true
            }
        }
        return retVal

    }

    private fun ifGPSIsOffAskUserToTurnOnAndRegisterListenerForGPS(activity: Activity?) {

        this.activity = activity
        locationRequestPriorityHighAccuracy = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setSmallestDisplacement(WeatherForecastConstants.SmallestDisplacement)
            .setInterval(WeatherForecastConstants.Interval)
            .setFastestInterval(WeatherForecastConstants.FastestInterval)

        locationSettingsRequestBuilder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequestPriorityHighAccuracy!!)
        locationSettingsRequestBuilder?.setAlwaysShow(true)

        taskLocationSettingsResponse = context?.let {
            LocationServices.getSettingsClient(it).checkLocationSettings(
                locationSettingsRequestBuilder?.build()
            )
        }
        taskLocationSettingsResponse?.addOnCompleteListener(object :
            OnCompleteListener<LocationSettingsResponse?> {
            override fun onComplete(task: Task<LocationSettingsResponse?>) {
                try {
                    previousStatusIsProviderEnabled =
                        isProviderEnabled(LocationManager.GPS_PROVIDER)
                    if (previousStatusIsProviderEnabled == true) {
                        setComingFromLocationSettingsPageFlag(false)
                        registerListenerForGPSWhenGPSIsOn()
                    } else {

                        val response: LocationSettingsResponse? =
                            task.getResult(ApiException::class.java)

                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                    }
                } catch (exception: ApiException) {

                    when (exception.getStatusCode()) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {

                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {

                                // Cast to a resolvable exception.
                                resolvable = exception as ResolvableApiException
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                if (activity != null) {

                                    if (getComingFromLocationSettingsPageFlag() != true) {
                                        instantiateAlertDialogAndShow(activity)
                                    }

                                    setComingFromLocationSettingsPageFlag(false)

                                }

                            } catch (e: ClassCastException) {
                                // Ignore, should be an impossible error.
                            }

                        }

                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        }

                    }
                }
            }
        })
    }

    private fun registerListenerForGPSWhenGPSIsOn() {

        if (context?.let {ContextCompat.checkSelfPermission(it,
                Manifest.permission.ACCESS_FINE_LOCATION)}
            == PackageManager.PERMISSION_GRANTED) {

            // Register the listener with the Location Manager to receive location updates
            if (locationManager != null) {

                isLocationUpdateActive = true
                locationManager!!.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    WeatherForecastConstants.MinTime,
                    WeatherForecastConstants.MinDistance,
                    locationListener
                )

            }

        } else {

            this.activity?.let {ActivityCompat.requestPermissions(it,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    WeatherForecastConstants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) }

        }

    }

}