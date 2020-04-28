package com.flexso.flexsame.services

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log
import com.flexso.flexsame.utils.ByteArrayHexUtil
import java.nio.ByteBuffer


class HostCardEmulatorService : HostApduService() {

    override fun onDeactivated(reason: Int) {
        Log.d(TAG, "Deactivated: " + reason)
    }

    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        Log.d(TAG, "APDU process command")

        if (commandApdu == null) {
            return ByteArrayHexUtil.hexStringToByteArray(STATUS_FAILED)
        }
        Log.i(TAG, commandApdu.toString())

        val hexCommandApdu = ByteArrayHexUtil.toHex(commandApdu)


        if (hexCommandApdu.length < MIN_APDU_LENGTH) {
            return ByteArrayHexUtil.hexStringToByteArray(STATUS_FAILED)
        }

        if (hexCommandApdu.substring(0, 2) != DEFAULT_CLA) {
            return ByteArrayHexUtil.hexStringToByteArray(CLA_NOT_SUPPORTED)
        }

        if (hexCommandApdu.substring(2, 4) != SELECT_INS) {
            return ByteArrayHexUtil.hexStringToByteArray(INS_NOT_SUPPORTED)
        }

        if (hexCommandApdu.substring(10, 24) == AID) {
            // we wont return success 90 00, we respond with our uid
            // return ByteArrayHexUtil.hexStringToByteArray(STATUS_SUCCESS)
            //val res =  CurrentKey.userId.toString().toByteArray().plus(ByteArrayHexUtil.hexStringToByteArray(STATUS_SUCCESS))
            var currentUserId = CurrentKey.userId.toString()
            var currentOfficeId = CurrentKey.officeId.toString()

            val res =  currentUserId.toByteArray()
                    .plus(";".toByteArray())
                    .plus(currentOfficeId.toByteArray())
                    .plus(ByteArrayHexUtil.hexStringToByteArray(STATUS_SUCCESS))

            Log.i(TAG, "userId = $currentUserId ; officeId = $currentOfficeId")
            return res
        } else {
            return ByteArrayHexUtil.hexStringToByteArray(STATUS_FAILED)
        }
    }

    companion object {
        val TAG = "Host Card Emulator"
        val STATUS_SUCCESS = "9000"
        val STATUS_FAILED = "6F00"
        val CLA_NOT_SUPPORTED = "6E00"
        val INS_NOT_SUPPORTED = "6D00"
        val AID = "A0000001020304"
        val SELECT_INS = "A4"
        val DEFAULT_CLA = "00"
        val MIN_APDU_LENGTH = 12
    }
}

