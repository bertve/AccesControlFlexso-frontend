package com.flexso.flexsame.ui.login

import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flexso.flexsame.R
import com.flexso.flexsame.models.LoginSucces
import com.flexso.flexsame.models.Result
import com.flexso.flexsame.repos.LoginRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository, private val connectivityManager: ConnectivityManager) : ViewModel() {

    //coroutines
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _connection = MutableLiveData<Boolean>()

    val connection: LiveData<Boolean>
        get() = _connection

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        viewModelScope.launch {
            val result = loginRepository.login(username, password)
            Log.i("login", result.toString())
            if (result is Result.Success) {
                _loginResult.value =
                        LoginResult(success = LoginSucces(token = result.data.token, email = result.data.email, password = result.data.password))
            } else {
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }

        }

    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches()
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun checkConnectivity(): Boolean {
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
        return onStartUpConnection
    }
}
