package com.example.flexsame.models

class Office {

    val officeId: Long
    val address: Address
    val company: Company

    constructor(id :Long, company:Company, address: Address){
        this.officeId = id
        this.company = company
        this.address = address
    }

    override fun toString(): String {
        return "Office(officeId=$officeId, address=$address, company=$company)"
    }


}
