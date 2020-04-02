package com.flexso.flexsame.ui.home

import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    private val _connection = MutableLiveData<Boolean>()

    val connection: LiveData<Boolean>
        get() = _connection

    fun checkConnectivity(connectivityManager: ConnectivityManager) {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network?) {
                _connection.postValue(false)
            }

            override fun onAvailable(network: Network?) {
                _connection.postValue(true)
            }
        }
        val onStartUpConnection = connectivityManager.activeNetworkInfo?.isConnectedOrConnecting == true
        _connection.postValue(onStartUpConnection)
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }
}
