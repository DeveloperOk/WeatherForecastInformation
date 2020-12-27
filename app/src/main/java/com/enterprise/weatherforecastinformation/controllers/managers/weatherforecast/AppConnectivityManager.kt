package com.enterprise.weatherforecastinformation.controllers.managers.weatherforecast

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.enterprise.weatherforecastinformation.R

class AppConnectivityManager {

    companion object {

        fun isConnectedToInternet(context: Context): Boolean {

            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

            return isConnected

        }

        fun showAlertDialogForNoInternetConnection(context: Context) {

            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setTitle(R.string.no_internet_dialog_title)
            alertDialogBuilder.setMessage(R.string.no_internet_dialog_message)
            alertDialogBuilder.setNeutralButton(R.string.no_internet_dialog_neutral_button,
                DialogInterface.OnClickListener { dialog, id -> })
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()

        }


    }

}