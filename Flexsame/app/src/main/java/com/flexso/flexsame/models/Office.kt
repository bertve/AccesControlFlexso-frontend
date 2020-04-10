package com.flexso.flexsame.models

class Office(val officeId: Long, val company: Company, val address: Address) {

    override fun toString(): String {
        return "Office(officeId=$officeId, address=$address, company=$company)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Office

        if (officeId != other.officeId) return false
        if (address != other.address) return false
        if (company != other.company) return false

        return true
    }

    override fun hashCode(): Int {
        var result = officeId.hashCode()
        result = 31 * result + address.hashCode()
        result = 31 * result + company.hashCode()
        return result
    }

    fun streetHouseNumberString() : String{
        return this.address.street + " " + this.address.houseNumber
    }

    fun postalCodeCity() : String{
        return this.address.postalCode + " " + this.address.town
    }

    fun filterString() : String{
        return this.address.street + this.address.houseNumber + this.address.postalCode + this.address.town + this.address.country
    }

}
