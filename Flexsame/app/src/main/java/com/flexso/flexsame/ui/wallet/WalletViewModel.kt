package com.flexso.flexsame.ui.wallet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flexso.flexsame.models.*
import com.flexso.flexsame.repos.KeyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class WalletViewModel(private val keyRepository: KeyRepository) : ViewModel() {
    //current selected office
    var selectedOffice : Office
            = Office(0L, Company(0L,""),
            Address("Select office","","before you","try","accessing the gate"))

    //user
    private lateinit var user: User

    //coroutines
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    //offices
    var offices: MutableLiveData<List<Office>> = keyRepository.offices
    var _filteredOffices: MutableLiveData<List<Office>> = MutableLiveData()
    val filteredOffices: LiveData<List<Office>> = _filteredOffices

    fun filterOffices(filter: String?) {
        val sortedList: List<Office> = offices.value!!.sortedWith(
                compareBy({ it.company.name }
                        , { it.address.country }
                        , { it.address.town }
                        , { it.address.postalCode }
                        , { it.address.street }
                        , { it.address.houseNumber }
                ))
        if (filter == null || filter.trim() == "" || filter.trim() == "All") {
            this._filteredOffices.value = sortedList

        } else {
            this._filteredOffices.value = sortedList.filter {
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


    fun setUser(current: User) {
        this.user = current
        if (user.roles.any { r -> r.roleName == RoleName.ROLE_ADMIN }) {
            viewModelScope.launch {
                initAdminOffices()
            }
        } else if (user.roles.any { r -> r.roleName == RoleName.ROLE_COMPANY }) {
            viewModelScope.launch {
                initCompanyOffices()
            }
        } else {
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

    fun setCurrentOffice(office: Office) {
        this.selectedOffice = office
        //make key...
    }


}
