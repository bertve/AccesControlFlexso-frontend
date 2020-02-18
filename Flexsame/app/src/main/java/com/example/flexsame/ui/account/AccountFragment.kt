package com.example.flexsame.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import org.koin.androidx.viewmodel.ext.android.viewModel

import com.example.flexsame.R
import com.example.flexsame.databinding.AccountFragmentBinding

class AccountFragment : Fragment() {

    val viewModel: AccountViewModel by viewModel()
    lateinit var binding : AccountFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.account_fragment, container, false)
        binding.lifecycleOwner = this
        binding.accountViewModel = viewModel

        return binding.root
    }



}
