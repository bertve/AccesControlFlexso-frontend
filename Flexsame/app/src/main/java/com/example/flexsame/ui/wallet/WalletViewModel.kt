package com.example.flexsame.ui.wallet

import android.util.Log
import androidx.lifecycle.*
import com.example.flexsame.models.Office
import com.example.flexsame.models.User
import com.example.flexsame.repos.KeyRepository
import kotlinx.coroutines.*

class WalletViewModel(private val keyRepository : KeyRepository) : ViewModel() {
    //user
    private lateinit var user: User

    //coroutines
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope( viewModelJob + Dispatchers.Main)

    //offices
    var offices : LiveData<List<Office>> = keyRepository.offices
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
        viewModelScope.launch {
            initOffices()
        }
    }


}
