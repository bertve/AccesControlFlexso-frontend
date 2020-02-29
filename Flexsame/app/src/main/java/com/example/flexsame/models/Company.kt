package com.example.flexsame.models

class Company {
    val companyId: Long
    val name: String

    constructor(id:Long,name: String){
        this.companyId = id
        this.name = name
    }

    override fun toString(): String {
        return "Company(companyId=$companyId, name='$name')"
    }


}
