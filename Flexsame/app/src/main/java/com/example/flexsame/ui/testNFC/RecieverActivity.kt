package com.example.flexsame.ui.testNFC

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.flexsame.R

class RecieverActivity : AppCompatActivity() {
    private var nfcAdapter: NfcAdapter? = null

    private var isNfcSupported: Boolean = false
    private var incomingMessage: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reciever)
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
        val action = intent.action
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == action) {
            val parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            with(parcelables) {
                val inNdefMessage = this[0] as NdefMessage
                val inNdefRecords = inNdefMessage.records
                val ndefRecord_0 = inNdefRecords[0]

                val inMessage = String(ndefRecord_0.payload)
                incomingMessage?.text = inMessage
            }
        }
    }


    // Foreground dispatch holds the highest priority for capturing NFC intents
    // then go activities with these intent filters:
    // 1) ACTION_NDEF_DISCOVERED
    // 2) ACTION_TECH_DISCOVERED
    // 3) ACTION_TAG_DISCOVERED

    // always try to match the one with the highest priority, cause ACTION_TAG_DISCOVERED is the most
    // general case and might be intercepted by some other apps installed on your device as well

    // When several apps can match the same intent Android OS will bring up an app chooser dialog
    // which is undesirable, because user will most likely have to move his device from the tag or another
    // NFC device thus breaking a connection, as it's a short range

    private fun enableForegroundDispatch(activity: AppCompatActivity, adapter: NfcAdapter?) {

        // here we are setting up receiving activity for a foreground dispatch
        // thus if activity is already started it will take precedence over any other activity or app
        // with the same intent filters

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
