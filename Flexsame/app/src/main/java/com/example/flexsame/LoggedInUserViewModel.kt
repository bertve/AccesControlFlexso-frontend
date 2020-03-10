package com.example.flexsame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flexsame.models.Office
import com.example.flexsame.models.User
import com.example.flexsame.network.AuthInterceptor
import com.example.flexsame.repos.LoggedInUserRepository
import kotlinx.coroutines.*

class LoggedInUserViewModel(private val loggedInUserRepository: LoggedInUserRepository) : ViewModel(){
    var email : String = ""
    var token : String = ""
    var password : String = ""

    //coroutines
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope( viewModelJob + Dispatchers.Main)

    var user : LiveData<User> = loggedInUserRepository.user

    fun setCurrentUser(email :String ,token :String,password : String) {
        AuthInterceptor.setSessionToken(token)
        this.email = email
        this.token = token
        this.password = password
        getCurrentUser()
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                loggedInUserRepository.getCurrentUser(token,password)
            }
        }
    }

}