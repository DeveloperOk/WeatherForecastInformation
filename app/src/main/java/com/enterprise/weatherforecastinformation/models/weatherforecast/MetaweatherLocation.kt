package com.enterprise.weatherforecastinformation.models.weatherforecast

import android.os.Parcel
import android.os.Parcelable

open class MetaweatherLocation() : Parcelable {

    var distance: Int? = null
    var title: String? = null
    var location_type: String? = null
    var woeid: Int?= null
    var latt_long: String? = null

    constructor(parcel: Parcel) : this() {
        distance = parcel.readValue(Int::class.java.classLoader) as? Int
        title = parcel.readString()
        location_type = parcel.readString()
        woeid = parcel.readValue(Int::class.java.classLoader) as? Int
        latt_long = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(distance)
        parcel.writeString(title)
        parcel.writeString(location_type)
        parcel.writeValue(woeid)
        parcel.writeString(latt_long)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MetaweatherLocation> {
        override fun createFromParcel(parcel: Parcel): MetaweatherLocation {
            return MetaweatherLocation(parcel)
        }

        override fun newArray(size: Int): Array<MetaweatherLocation?> {
            return arrayOfNulls(size)
        }
    }

}