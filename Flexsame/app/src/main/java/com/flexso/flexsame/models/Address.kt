package com.flexso.flexsame.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Address(val street: String, val houseNumber: String, val postalCode: String, val town: String, val country: String) : Parcelable {

    override fun toString(): String {
        return "Address(street='$street', houseNumber='$houseNumber', postalCode='$postalCode', town='$town', country='$country')"
    }


}