package com.flexso.flexsame.ui.wallet

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flexso.flexsame.models.*
import com.flexso.flexsame.repos.KeyRepository
import com.flexso.flexsame.services.CurrentKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class WalletViewModel(private val keyRepository: KeyRepository) : ViewModel() {

    //keyToken
    private var _keyToken : MutableLiveData<String> = keyRepository.keyToken
    val keyToken : LiveData<String> get() = _keyToken

    //current selected office
   private var _selectedOffice : MutableLiveData<Office> = MutableLiveData()
   val selectedOffice : LiveData<Office> get() = _selectedOffice

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
                initSelectedOffice()
            }
        } else if (user.roles.any { r -> r.roleName == RoleName.ROLE_COMPANY }) {
            viewModelScope.launch {
                initCompanyOffices()
                initSelectedOffice()
            }
        } else {
            viewModelScope.launch {
                initOffices()
                initSelectedOffice()
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
        Log.i("current_office",office.toString())
        this._selectedOffice.value = office
        CurrentKey.officeId = office.officeId
        this.generateKey()
    }

    private fun generateKey() {
        viewModelScope.launch {
            keyRepository.generateKey()
        }
    }

    private fun initSelectedOffice(){
        if (CurrentKey.officeId != -1L){

            var o : Office? = offices.value!!.find { o -> o.officeId == CurrentKey.officeId }
            if (o != null){
                this._selectedOffice.postValue(o)
            }
        }
    }

    init {
        this._selectedOffice.value = Office(0L, Company(0L,""),
                Address("Select office","","before you","try","accessing the gate"))
    }
}
