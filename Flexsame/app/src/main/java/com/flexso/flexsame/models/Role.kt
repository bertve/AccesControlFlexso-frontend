package com.flexso.flexsame.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Role(val roleId: Long, val roleName: RoleName, val description: String) :Parcelable{
    override fun toString(): String {
        return "Role(roleId=$roleId, roleName=$roleName, description='$description')"
    }
}