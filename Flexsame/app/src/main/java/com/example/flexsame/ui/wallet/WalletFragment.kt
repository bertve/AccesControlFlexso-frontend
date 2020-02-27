package com.example.flexsame.ui.wallet

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.flexsame.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


import com.example.flexsame.R
import com.example.flexsame.databinding.WalletFragmentBinding

class WalletFragment : Fragment() {

    val viewModel: WalletViewModel by viewModel()
    lateinit var binding : WalletFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.wallet_fragment,container,false)

        setupViewModel()
        return binding.root
    }

    private fun setupViewModel() {
        binding.lifecycleOwner = this
        binding.walletViewModel = viewModel
        viewModel.setUserId((activity as MainActivity).getUserId())
    }


}
