package com.flexso.flexsame.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Company(val companyId: Long, var name: String) : Parcelable {
    override fun toString(): String {
        return "Company(companyId=$companyId, name='$name')"
    }
}
