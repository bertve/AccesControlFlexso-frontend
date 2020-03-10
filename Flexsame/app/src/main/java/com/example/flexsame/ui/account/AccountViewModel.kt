package com.example.flexsame.ui.account

import androidx.lifecycle.ViewModel
import com.example.flexsame.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class AccountViewModel : ViewModel() {
    private lateinit var user : User

    //coroutines
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope( viewModelJob + Dispatchers.Main)

    fun setUser(currentUser: User) {
        this.user = currentUser
    }

    fun getUser() : User{
        return this.user
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
