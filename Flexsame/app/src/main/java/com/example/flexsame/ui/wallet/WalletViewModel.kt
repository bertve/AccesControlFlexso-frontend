package com.example.flexsame.ui.wallet

import android.util.Log
import androidx.lifecycle.*
import com.example.flexsame.models.Office
import com.example.flexsame.repos.KeyRepository
import kotlinx.coroutines.*

class WalletViewModel(private val keyRepository : KeyRepository) : ViewModel() {
    //user
    private var userId: Long = 0

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

    init {
        viewModelScope.launch {
            initOffices()
        }

    }


    private suspend fun initOffices() {
        withContext(Dispatchers.IO){
            keyRepository.getOffices(userId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun setUserId(userId: Long) {
        this.userId = userId
    }

    fun test(){
        viewModelScope.launch {
            initOffices()
        }
        Log.i("api",offices.value.toString())
    }


}
