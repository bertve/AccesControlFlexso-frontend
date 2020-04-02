package com.flexso.flexsame.models

class Address(val street: String, val houseNumber: String, val postalCode: String, val town: String, val country: String) {

    override fun toString(): String {
        return "Address(street='$street', houseNumber='$houseNumber', postalCode='$postalCode', town='$town', country='$country')"
    }


}