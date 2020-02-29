package com.example.flexsame.models

import java.util.concurrent.locks.AbstractOwnableSynchronizer

class Address {
    val street : String
    val houseNumber : String
    val postalCode : String
    val town : String
    val country : String

    constructor(street : String,houseNumber :String,postalCode :String,town :String,country : String){
        this.street = street
        this.houseNumber = houseNumber
        this.postalCode = postalCode
        this.town = town
        this.country = country
    }

    override fun toString(): String {
        return "Address(street='$street', houseNumber='$houseNumber', postalCode='$postalCode', town='$town', country='$country')"
    }


}