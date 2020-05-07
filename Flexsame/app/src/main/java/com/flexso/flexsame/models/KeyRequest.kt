package com.flexso.flexsame.models


data class KeyRequest(private val userId : Long,private val officeId : Long,private val deviceId : String,private val publicKey: String) {
    override fun toString(): String {
        return "KeyRequest(userId=$userId, officeId=$officeId, deviceId='$deviceId', publicKey='$publicKey')"
    }
}