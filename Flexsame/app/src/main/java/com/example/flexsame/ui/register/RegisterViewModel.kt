package com.example.flexsame.ui.register


import android.transition.Visibility
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.flexsame.models.dto.auth.ApiResponse
import com.example.flexsame.repos.RegisterRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class RegisterViewModel (val registerRepository: RegisterRepository): ViewModel() {

    //coroutines
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope( viewModelJob + Dispatchers.Main)

    private val _apiResponse = MutableLiveData<ApiResponse>()
    val apiResponse : LiveData<ApiResponse> = _apiResponse


    init {
        _apiResponse.value = ApiResponse(true,"")
    }


    val error = Transformations.map(_apiResponse){
        if (it.success ){
            View.GONE
        }else{
                View.VISIBLE
        }

    }

    val errorMessage =  Transformations.map(_apiResponse){
        if(_apiResponse.value != null){
            _apiResponse.value!!.message
        }else{
            ""
        }
    }

    fun register(firstName: String, lastName: String, email: String, password :String, password_confirm: String) {

        if(password == password_confirm){
            Log.i("register","password and password confirmation match")
            viewModelScope.launch {
              _apiResponse.value =  registerRepository.register(firstName,lastName,email,password)
            }
        }else{
            Log.i("register","password and password confirmation don't match")
            this._apiResponse.value = ApiResponse(false,"Password confirmation doesn't match")
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
