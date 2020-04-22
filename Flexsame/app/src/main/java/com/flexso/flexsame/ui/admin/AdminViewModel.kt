package com.flexso.flexsame.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flexso.flexsame.models.User
import com.flexso.flexsame.models.dto.auth.SignUpRequestCompany
import com.flexso.flexsame.repos.AdminRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class AdminViewModel(private val adminRepository: AdminRepository) : ViewModel() {
    //coroutines
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    //company_users
    private var _users: MutableLiveData<List<User>> = adminRepository._users
    val users: LiveData<List<User>> get() = _users

    //add_succes
    private val _addSucces = MutableLiveData<Boolean>()
    val addSucces: LiveData<Boolean> get() = _addSucces

    //remove_succes
    private val _removeSucces = MutableLiveData<Boolean>()
    val removeSucces: LiveData<Boolean> get() = _removeSucces


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        getCompanyUsers()
    }

    fun getCompanyUsers() {
        viewModelScope.launch {
            adminRepository.getCompanyUsers()
            _users = adminRepository._users
        }
    }

    fun addCompany(signUpRequestCompany: SignUpRequestCompany) {
        viewModelScope.launch {
            _addSucces.postValue(adminRepository.addCompany(signUpRequestCompany))
            getCompanyUsers()
        }
    }

    fun removeCompany(userId: Long) {
        viewModelScope.launch {
            _removeSucces.postValue(adminRepository.removeCompany(userId))
            getCompanyUsers()
        }
    }

}
