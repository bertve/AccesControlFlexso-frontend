package com.flexso.flexsame.ui.wallet

import androidx.lifecycle.*
import com.flexso.flexsame.models.Office
import com.flexso.flexsame.models.RoleName
import com.flexso.flexsame.models.User
import com.flexso.flexsame.repos.AdminRepository
import com.flexso.flexsame.repos.KeyRepository
import kotlinx.coroutines.*

class WalletViewModel(private val keyRepository : KeyRepository) : ViewModel() {
    //user
    private lateinit var user: User

    //coroutines
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope( viewModelJob + Dispatchers.Main)

    //offices
    var offices : MutableLiveData<List<Office>> = keyRepository.offices
    val filteredOffices : MutableLiveData<List<Office>> = MutableLiveData()

    fun filterOffices(filter : String?){
        val sortedList : List<Office> = offices.value!!.sortedWith(
            compareBy({ it.company.name }
                ,{ it.address.country}
                ,{ it.address.town}
                ,{ it.address.postalCode}
                ,{ it.address.street }
                ,{ it.address.houseNumber}
                ))
        if( filter == null || filter.trim() == "" || filter.trim() == "All") {
            this.filteredOffices.value = sortedList

        }else{
            this.filteredOffices.value = sortedList.filter {
                it.company.name == filter

            }
        }
    }
    private suspend fun initOffices() {
            keyRepository.getOffices(user.userId)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    fun  setUser(current : User) {
        this.user = current
        if(user.roles.any { r -> r.roleName == RoleName.ROLE_ADMIN }){
            viewModelScope.launch {
                initAdminOffices()
            }
        }else if(user.roles.any {r -> r.roleName == RoleName.ROLE_COMPANY }){
            viewModelScope.launch {
                initCompanyOffices()
            }
        }else{
            viewModelScope.launch {
                initOffices()
            }
        }
    }

    private suspend fun initCompanyOffices() {
        keyRepository.getAllOfficesFromCompany(user.company!!.companyId)
    }

    private suspend fun initAdminOffices() {
        keyRepository.getAllOffices()
    }


}
