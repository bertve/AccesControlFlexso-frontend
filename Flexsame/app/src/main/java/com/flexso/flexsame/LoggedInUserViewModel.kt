package com.flexso.flexsame

import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flexso.flexsame.models.User
import com.flexso.flexsame.network.AuthInterceptor
import com.flexso.flexsame.repos.LoggedInUserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class LoggedInUserViewModel(private val loggedInUserRepository: LoggedInUserRepository,private val connectivityManager: ConnectivityManager) : ViewModel() {
    var email: String = ""
    var token: String = ""
    var password: String = ""

    //coroutines
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    //currentUser succes
    private val _currentUserSucces = MutableLiveData<Boolean>()
    val currentUserSucces: LiveData<Boolean> get() = _currentUserSucces

    //update succes
    private val _updateSucces = MutableLiveData<Boolean>()
    val updateSucces: LiveData<Boolean> get() = _updateSucces

    var user: LiveData<User> = loggedInUserRepository.user

    fun setCurrentUser(email: String, token: String, password: String) {
        AuthInterceptor.setSessionToken(token)
        this.email = email
        this.token = token
        this.password = password
        getCurrentUser()
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            _currentUserSucces.value = loggedInUserRepository.getCurrentUser(token, password)

        }
    }

    fun update(firstName: String, lastName: String, email: String, password: String) {
        val helper = User(user.value!!.userId, firstName, lastName, email, password, token)
        viewModelScope.launch {
            _updateSucces.value = loggedInUserRepository.update(helper)
        }
    }

    //connection
    private val _connection = MutableLiveData<Boolean>()

    val connection: LiveData<Boolean>
        get() = _connection

    fun checkConnectivity() {
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