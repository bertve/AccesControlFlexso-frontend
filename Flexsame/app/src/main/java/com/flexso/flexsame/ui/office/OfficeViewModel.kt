package com.flexso.flexsame.ui.office

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flexso.flexsame.models.Address
import com.flexso.flexsame.models.Office
import com.flexso.flexsame.models.User
import com.flexso.flexsame.repos.OfficeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class OfficeViewModel(private val officeRepository: OfficeRepository) : ViewModel() {

    //coroutines
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope( viewModelJob + Dispatchers.Main)

    lateinit var office : Office

    //checkedPersons
    private var _checkedPersons : MutableLiveData<MutableList<Long>> = MutableLiveData()
    val checkedPersons : LiveData<MutableList<Long>> get() = _checkedPersons
    //auth_persons
    private var _authorizedPersons : MutableLiveData<List<User>> = officeRepository.authorizedPersons
    val authorizedPersons : LiveData<List<User>> get() = _authorizedPersons

    //unauth_persons
    private var _unAuthorizedPersons : MutableLiveData<List<User>> = officeRepository.unAuthorizedPersons
    val unAuthorizedPersons : LiveData<List<User>> get() = _unAuthorizedPersons


    //add_succes
    private val _addSucces = MutableLiveData<Boolean>()
    val addSucces : LiveData<Boolean> get() = _addSucces

    //remove_succes
    private val _removeSucces = MutableLiveData<Boolean>()
    val removeSucces : LiveData<Boolean> get() = _removeSucces

    //edit_succes
    private val _editSucces = MutableLiveData<Boolean>()
    val editSucces : LiveData<Boolean> get() = _editSucces

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        _checkedPersons.value = mutableListOf()
    }

    fun setCurrentOffice(currentOffice: Office) {
        this.office = currentOffice
        getAuthorizedPersons()
        getUnAuthorizedPersons()
    }

    fun getUnAuthorizedPersons() {
        viewModelScope.launch {
            officeRepository.getUnAuthorizedPersons(office.officeId)
            _unAuthorizedPersons = officeRepository.unAuthorizedPersons
        }
    }

    fun deAuthorizeUserFromOffice(userId: Long) {
        viewModelScope.launch {
            _removeSucces.postValue(officeRepository.deAuthorizePerson(office.officeId,userId))
            getAuthorizedPersons()
            getUnAuthorizedPersons()
        }
    }

    fun addCheckedPersons(){
        this._checkedPersons.value!!.forEach {
            this.authorizePerson(it)
        }
    }

    fun authorizePerson(userId : Long){
        viewModelScope.launch {
            _addSucces.postValue(officeRepository.authorizePerson(office.officeId,userId))
            getAuthorizedPersons()
            getUnAuthorizedPersons()
        }
    }

    fun getAuthorizedPersons() {
        viewModelScope.launch {
            officeRepository.getAuthorizedPersons(office.officeId)
            _authorizedPersons = officeRepository.authorizedPersons
        }
    }

    fun editOfficeAddress(a : Address){
        var helper = Office(office.officeId,office.company,a)
        viewModelScope.launch {
            if(officeRepository.updateOffice(helper)){
                _editSucces.postValue(true)
                office.address = a
            }else{
                _editSucces.postValue(false)
            }
        }
    }

    fun removeFromCheckedList(userId: Long) {
        val res :MutableList<Long> = this._checkedPersons.value!!
        res.remove(userId)
        this._checkedPersons.value = res
        Log.i("checked_min",_checkedPersons.value.toString())
    }

    fun addToCheckedList(userId: Long) {
        val res :MutableList<Long> = this._checkedPersons.value!!
        res.add(userId)
        this._checkedPersons.value = res
        Log.i("checked_plus",_checkedPersons.value.toString())
    }

    fun resetCheckedList(){
        val res :MutableList<Long> = this._checkedPersons.value!!
        res.clear()
        this._checkedPersons.value = res
        Log.i("checked_reset",_checkedPersons.value.toString())
    }

}
