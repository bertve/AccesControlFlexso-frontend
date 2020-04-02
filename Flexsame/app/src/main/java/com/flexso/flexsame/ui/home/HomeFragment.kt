package com.flexso.flexsame.ui.home

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.airbnb.lottie.LottieAnimationView
import com.flexso.flexsame.R
import com.flexso.flexsame.databinding.HomeFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModel()
    private lateinit var binding : HomeFragmentBinding
    private lateinit var connectionRex : LottieAnimationView
    private lateinit var logoFlexso : ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.home_fragment,container,false)
        binding.lifecycleOwner = this
        binding.homeViewModel = this.viewModel
        setupUi()
        checkConnection()
        setupObservers()
        return binding.root
    }

    private fun setupObservers() {
        viewModel.connection.observe(this.activity!!, Observer { connect ->
            connect?.let {
                if (connect) {
                    Log.i("connection","connected")
                    setVisibilityHomeScreen(true)

                } else {
                    Log.i("connection","no connection")
                    setVisibilityHomeScreen(false)
                }
            }
        })

    }

    private fun setupUi() {
        connectionRex = binding.noConnection
        connectionRex.visibility = View.GONE
        logoFlexso = binding.logoFlexso
    }

    private fun setVisibilityHomeScreen(visible : Boolean) {
        //true ->  homescreen visible
        //false -> TREX visible
        var visibility = View.GONE
        if(visible){
            visibility = View.VISIBLE
            connectionRex.visibility = View.GONE
            connectionRex.cancelAnimation()
        }else{
            connectionRex.playAnimation()
            connectionRex.visibility = View.VISIBLE
        }
        logoFlexso.visibility = visibility
    }

    private fun checkConnection() {
        viewModel.checkConnectivity()
    }
}
