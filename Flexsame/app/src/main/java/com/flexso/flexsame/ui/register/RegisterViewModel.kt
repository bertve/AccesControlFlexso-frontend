package com.flexso.flexsame.ui.register


import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.flexso.flexsame.models.dto.auth.ApiResponse
import com.flexso.flexsame.repos.RegisterRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class RegisterViewModel(val registerRepository: RegisterRepository) : ViewModel() {

    //coroutines
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _apiResponse = MutableLiveData<ApiResponse>()
    val apiResponse: LiveData<ApiResponse> = _apiResponse

    fun register(firstName: String, lastName: String, email: String, password: String, password_confirm: String) {
            viewModelScope.launch {
                _apiResponse.value = registerRepository.register(firstName, lastName, email, password)
            }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
