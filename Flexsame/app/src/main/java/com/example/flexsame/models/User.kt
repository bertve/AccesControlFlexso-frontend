package com.example.flexsame.models

import android.os.Parcelable
import com.example.flexsame.utils.Utilities
import kotlinx.android.parcel.Parcelize
@Parcelize
data class User (val userId :Long
                ,var firstName :String
                ,var lastName :String
                ,var email: String
                ,var password: String
                ,var token : String) :Parcelable{

    fun getFullName(): String {
        return Utilities.capitalizeWords(this.firstName + " " + this.lastName)
    }

}