package com.example.flexsame.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class User (val userId :Long
                ,var firstName :String
                ,var lastName :String
                ,var email: String
                ,var password: String
                ,var token : String) :Parcelable{

}