package com.flexso.flexsame.models

import android.os.Parcelable
import com.flexso.flexsame.utils.Utilities
import kotlinx.android.parcel.Parcelize
@Parcelize
data class User (val userId :Long
                ,var firstName :String
                ,var lastName :String
                ,var email: String
                ,var password: String
                ,var token : String
                 ,var roles : Set<Role> = setOf<Role>(Role(-1L,RoleName.ROLE_USER,"default"))
                 ,var company: Company? = null) :Parcelable{

    fun getFullName(): String {
        return Utilities.capitalizeWords(this.firstName + " " + this.lastName)
    }

    override fun toString(): String {
        if (company == null){
            return "User(userId=$userId, firstName='$firstName', lastName='$lastName', email='$email', password='$password', token='$token', roles=$roles)"

        }
        return "User(userId=$userId, firstName='$firstName', lastName='$lastName', email='$email', password='$password', token='$token', roles=$roles, company=$company)"
    }

    fun isCompany():Boolean{
        return this.roles.any { it.roleName == RoleName.ROLE_COMPANY }
    }

    fun isAdmin():Boolean{
        return this.roles.any { it.roleName == RoleName.ROLE_ADMIN }
    }


}