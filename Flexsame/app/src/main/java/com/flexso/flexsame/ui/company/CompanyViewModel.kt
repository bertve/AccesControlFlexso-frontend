package com.flexso.flexsame.ui.company

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flexso.flexsame.models.Address
import com.flexso.flexsame.models.Company
import com.flexso.flexsame.models.Office
import com.flexso.flexsame.repos.CompanyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class CompanyViewModel(private val companyRepository: CompanyRepository) : ViewModel() {
    //coroutines
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    lateinit var company: Company

    //company_users
    private var _offices: MutableLiveData<List<Office>> = companyRepository.offices
    val offices: LiveData<List<Office>> get() = _offices

    //add_succes
    private val _addSucces = MutableLiveData<Boolean>()
    val addSucces: LiveData<Boolean> get() = _addSucces

    //remove_succes
    private val _removeSucces = MutableLiveData<Boolean>()
    val removeSucces: LiveData<Boolean> get() = _removeSucces

    //edit_succes
    private val _editSucces = MutableLiveData<Boolean>()
    val editSucces: LiveData<Boolean> get() = _editSucces

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun setCurrentCompany(company: Company) {
        this.company = company
        getOffices()
    }

    fun getOffices() {
        viewModelScope.launch {
            companyRepository.getOffices(company.companyId)
            _offices = companyRepository.offices
        }
    }

    fun removeOffice(officeId: Long) {
        viewModelScope.launch {
            _removeSucces.postValue(companyRepository.removeOffice(company.companyId, officeId))
            getOffices()
        }
    }

    fun addOffice(a: Address) {
        viewModelScope.launch {
            _addSucces.postValue(companyRepository.addOffice(company.companyId, a))
            getOffices()
        }
    }

    fun editCompanyName(s: String) {
        var helper = Company(company.companyId, s)
        viewModelScope.launch {
            if (companyRepository.updateCompany(helper)) {
                _editSucces.postValue(true)
                company.name = s
            } else {
                _editSucces.postValue(false)
            }
        }
    }

}
