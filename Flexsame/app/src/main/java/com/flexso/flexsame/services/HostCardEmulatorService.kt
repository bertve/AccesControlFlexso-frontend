package com.flexso.flexsame.services

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log
import com.flexso.flexsame.utils.Utilities


class HostCardEmulatorService : HostApduService() {

    override fun onDeactivated(reason: Int) {
        Log.i("apdu", "Deactivated: " + reason)
    }


    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        Log.i("apdu","apdu_process")
        return "YEET".toByteArray().plus(Utilities.hexStringToByteArray(STATUS_SUCCESS))

        /*
        if (commandApdu == null) {
            Log.i("apdu", "apdu null")
            return Utilities.hexStringToByteArray(STATUS_FAILED)
        }

        val hexCommandApdu = Utilities.toHex(commandApdu)
        Log.i("apdu", hexCommandApdu)

        if (hexCommandApdu.length < MIN_APDU_LENGTH) {
            Log.i("apdu","nosucces")
            return Utilities.hexStringToByteArray(STATUS_FAILED)
        }

        if (hexCommandApdu.substring(0, 2) != DEFAULT_CLA) {
            Log.i("apdu","nosucces_2")

            return Utilities.hexStringToByteArray(CLA_NOT_SUPPORTED)
        }

        if (hexCommandApdu.substring(2, 4) != SELECT_INS) {
            Log.i("apdu","nosucces_3")

            return Utilities.hexStringToByteArray(INS_NOT_SUPPORTED)
        }
        var aidLength = hexCommandApdu.substring(8,10).toInt()

        if (hexCommandApdu.substring(10, 10+ aidLength*2) == AID) {
            Log.i("apdu","succes")
            return "YEET".toByteArray().plus(Utilities.hexStringToByteArray(STATUS_SUCCESS))

        } else {
            return Utilities.hexStringToByteArray(STATUS_FAILED)
        }
        */


    }

    companion object {
        val STATUS_SUCCESS = "9000"
        val STATUS_FAILED = "6F00"
        val CLA_NOT_SUPPORTED = "6E00"
        val INS_NOT_SUPPORTED = "6D00"
        val AID = "F0060504030201"
        val SELECT_INS = "A4"
        val DEFAULT_CLA = "00"
        val MIN_APDU_LENGTH = 12

    }
}

