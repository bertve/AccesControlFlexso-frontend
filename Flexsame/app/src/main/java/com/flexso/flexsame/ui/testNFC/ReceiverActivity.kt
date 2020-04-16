package com.flexso.flexsame.ui.testNFC

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.flexso.flexsame.R

class ReceiverActivity : AppCompatActivity() {
    private var nfcAdapter: NfcAdapter? = null

    private var isNfcSupported: Boolean = false
    private var incomingMessage: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("nfc", "receiver activity created")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiver)
        setupNFC()
        this.incomingMessage = findViewById(R.id.incoming_message)
    }

    private fun setupNFC() {
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        this.isNfcSupported = this.nfcAdapter != null

        if (!isNfcSupported) {
            Toast.makeText(this, "Nfc is not supported on this device", Toast.LENGTH_SHORT).show()
            finish()
        }

        if (!nfcAdapter!!.isEnabled) {
            Toast.makeText(
                    this,
                    "NFC disabled on this device. Turn on to proceed",
                    Toast.LENGTH_SHORT
            ).show()
        }

    }

    override fun onNewIntent(intent: Intent) {
        // also reading NFC message from here in case this activity is already started in order
        // not to start another instance of this activity
        super.onNewIntent(intent)
        receiveMessageFromDevice(intent)
    }

    override fun onResume() {
        super.onResume()

        // foreground dispatch should be enabled here, as onResume is the guaranteed place where app
        // is in the foreground
        enableForegroundDispatch(this, this.nfcAdapter)
        receiveMessageFromDevice(intent)
    }

    override fun onPause() {
        super.onPause()
        disableForegroundDispatch(this, this.nfcAdapter)
    }

    private fun receiveMessageFromDevice(intent: Intent) {
        Log.i("nfc", "receiver 68 receiveFromDevice")
        val action = intent.action
        Log.i("nfc", "intent: " + intent.action)
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == action) {
            Log.i("nfc", "ACTION_NDEF_DISCOVERED ")
            handleNdefMessage(intent)
        }
        if (NfcAdapter.ACTION_TECH_DISCOVERED == action) {
            Log.i("nfc", "ACTION_TECH_DISCOVERED  ")
            handleNdefMessage(intent)
        }
        if (NfcAdapter.ACTION_TAG_DISCOVERED == action) {
            Log.i("nfc", "ACTION_TAG_DISCOVERED")
            handleNdefMessage(intent)
        }
    }

    private fun handleNdefMessage(intent: Intent) {
        val parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
        with(parcelables) {
            val inNdefMessage = this[0] as NdefMessage
            val inNdefRecords = inNdefMessage.records
            val ndefRecord_0 = inNdefRecords[0]

            val inMessage = String(ndefRecord_0.payload)
            Log.i("nfc", "receiver 79 " + inMessage)

            incomingMessage?.text = inMessage
        }
    }

    private fun enableForegroundDispatch(activity: AppCompatActivity, adapter: NfcAdapter?) {
        val intent = Intent(activity.applicationContext, activity.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

        val pendingIntent = PendingIntent.getActivity(activity.applicationContext, 0, intent, 0)

        val filters = arrayOfNulls<IntentFilter>(1)
        val techList = arrayOf<Array<String>>()

        filters[0] = IntentFilter()
        with(filters[0]) {
            this?.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
            this?.addCategory(Intent.CATEGORY_DEFAULT)
            try {
                this?.addDataType("text/plain")
            } catch (ex: IntentFilter.MalformedMimeTypeException) {
                throw RuntimeException("Check your MIME type")
            }
        }

        adapter?.enableForegroundDispatch(activity, pendingIntent, filters, techList)
    }

    private fun disableForegroundDispatch(activity: AppCompatActivity, adapter: NfcAdapter?) {
        adapter?.disableForegroundDispatch(activity)
    }

}
