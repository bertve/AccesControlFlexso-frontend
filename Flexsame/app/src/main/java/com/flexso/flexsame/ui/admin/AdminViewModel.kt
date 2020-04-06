package com.flexso.flexsame.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flexso.flexsame.models.Office
import com.flexso.flexsame.models.User
import com.flexso.flexsame.repos.AdminRepository
import com.flexso.flexsame.repos.KeyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class AdminViewModel(private val adminRepository: AdminRepository) : ViewModel() {
    //coroutines
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope( viewModelJob + Dispatchers.Main)

    //company_users
    var users : LiveData<List<User>> = adminRepository.users

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        viewModelScope.launch {
            initCompanyUsers()
        }
    }

    private suspend fun initCompanyUsers(){
            adminRepository.getCompanyUsers()
    }

}
