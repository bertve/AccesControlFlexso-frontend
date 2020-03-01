package com.example.flexsame.ui.wallet

import android.graphics.drawable.ClipDrawable.HORIZONTAL
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
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
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        val adapter = WalletAdapter(context!!)
        binding.walletList.adapter = adapter

        val divider = DividerItemDecoration(context,HORIZONTAL)
        binding.walletList.addItemDecoration(divider)

        viewModel.offices.observe(this, Observer {
            it?.let {
                adapter.data = it
            }
        })
    }

    private fun setupViewModel() {
        binding.lifecycleOwner = this
        binding.walletViewModel = viewModel
        viewModel.setUserId((activity as MainActivity).getUserId())
    }


}
