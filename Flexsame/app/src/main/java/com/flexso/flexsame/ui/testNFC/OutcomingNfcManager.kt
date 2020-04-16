package com.flexso.flexsame.ui.testNFC

import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.NfcEvent
import android.util.Log

class OutcomingNfcManager(
        private val nfcActivity: NfcActivity
) :
        NfcAdapter.CreateNdefMessageCallback,
        NfcAdapter.OnNdefPushCompleteCallback {

    override fun createNdefMessage(event: NfcEvent): NdefMessage {
        val outString = nfcActivity.getOutcomingMessage()
        Log.i("nfc", "manager 17 createNdefMes " + outString)

        with(outString) {
            val outBytes = this.toByteArray()
            val outRecord = NdefRecord.createMime("text/plain", outBytes)
            return NdefMessage(outRecord)
        }
    }

    override fun onNdefPushComplete(event: NfcEvent) {
        Log.i("nfc", "mangager 27 ondefpushcomp")
        nfcActivity.signalResult()
    }

    interface NfcActivity {
        fun getOutcomingMessage(): String

        fun signalResult()
    }

}