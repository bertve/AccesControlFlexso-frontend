package com.flexso.flexsame.ui.company

import androidx.lifecycle.ViewModel
import com.flexso.flexsame.repos.KeyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class CompanyViewModel (private val keyRepository: KeyRepository) : ViewModel() {
    //coroutines
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope( viewModelJob + Dispatchers.Main)


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
