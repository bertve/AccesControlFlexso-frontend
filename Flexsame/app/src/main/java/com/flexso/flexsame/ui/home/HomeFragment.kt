package com.flexso.flexsame.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.flexso.flexsame.R
import com.flexso.flexsame.databinding.HomeFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {

    val viewModel: HomeViewModel by viewModel()
    lateinit var binding : HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.home_fragment,container,false)
        binding.lifecycleOwner = this
        binding.homeViewModel = this.viewModel
        return binding.root
    }



}
