package com.example.flexsame.ui.testNFC

import android.nfc.NfcAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.flexsame.MainActivity
import com.example.flexsame.R
import com.example.flexsame.databinding.TestNfcFragmentBinding
import com.example.flexsame.utils.OutcomingNfcManager
import com.example.flexsame.utils.OutcomingNfcManager.NfcActivity


class TestNFCFragment : Fragment(), NfcActivity{
    val viewModel : TestNFCViewModel by viewModel()
    private lateinit var binding : TestNfcFragmentBinding
    private var nfcAdapter: NfcAdapter? = null
    private var isNfcSupported : Boolean = false
    private lateinit var outcomingNfcCallback: OutcomingNfcManager
    private var outComingMessage:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.test_nfc_fragment,container,false)
        binding.lifecycleOwner = this
        binding.testNFCViewModel = this.viewModel
        setupNFC()
        binding.sendButton.setOnClickListener{ sendMessage()}
        return binding.root
    }

    private fun setupNFC(){
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(activity)
        this.isNfcSupported = this.nfcAdapter != null
        this.outcomingNfcCallback = OutcomingNfcManager(this)
        this.nfcAdapter?.setOnNdefPushCompleteCallback(outcomingNfcCallback, activity)
        this.nfcAdapter?.setNdefPushMessageCallback(outcomingNfcCallback, activity)

    }

    private fun sendMessage() {
        if (!isNfcSupported) {
            Toast.makeText(activity, "Nfc is not supported on this device", Toast.LENGTH_SHORT).show()
        } else if (!nfcAdapter!!.isEnabled) {
            Toast.makeText(
                activity,
                "NFC disabled on this device. Turn on to proceed",
                Toast.LENGTH_SHORT
            ).show()

        } else {
            Log.i("nfc","message send")
            setOutGoingMessage()
        }
        binding.messageInput.setText("")
        (activity as MainActivity).hideKeyboard(this.view!!)
    }

    private fun setOutGoingMessage() {
        this.outComingMessage = binding.messageInput.text.toString()
    }

    override fun getOutcomingMessage(): String = this.outComingMessage

    override fun signalResult() {
        activity!!.runOnUiThread {
            Toast.makeText(activity, R.string.message_beaming_complete, Toast.LENGTH_SHORT).show()
        }    }


}