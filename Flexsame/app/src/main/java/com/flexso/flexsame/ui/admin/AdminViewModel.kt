package com.flexso.flexsame.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flexso.flexsame.models.Office
import com.flexso.flexsame.repos.KeyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob


class AdminViewModel(private val keyRepository: KeyRepository) : ViewModel() {
    //coroutines
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope( viewModelJob + Dispatchers.Main)


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
