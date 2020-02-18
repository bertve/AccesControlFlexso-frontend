package com.example.flexsame.ui.testNFC

import org.koin.androidx.viewmodel.ext.android.viewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.flexsame.R
import com.example.flexsame.databinding.TestNfcFragmentBinding


class TestNFCFragment : Fragment() {
    val viewModel : TestNFCViewModel by viewModel()
    private lateinit var binding : TestNfcFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.test_nfc_fragment,container,false)
        binding.lifecycleOwner = this
        binding.testNFCViewModel = this.viewModel

        return binding.root
    }


}
