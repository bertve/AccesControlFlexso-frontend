package com.example.flexsame.ui.wallet

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.flexsame.models.Office
import com.example.flexsame.repos.KeyRepository
import kotlinx.coroutines.*

class WalletViewModel(private val keyRepository : KeyRepository) : ViewModel() {
    //user
    private var userId: Long = 0

    //coroutines
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope( viewModelJob + Dispatchers.Main)

    //spots
    private var offices : LiveData<List<Office>> = keyRepository.offices

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
