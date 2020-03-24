package com.example.flexsame.repos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.flexsame.models.Office
import com.example.flexsame.network.KeyService
import java.lang.Exception

class KeyRepository(private val keyService : KeyService){
    private var _offices = MutableLiveData<List<Office>>()
    val offices:LiveData<List<Office>> get() = _offices

    suspend fun getOffices(userId : Long){
        var getPropertiesDeffered = keyService.getOffices(userId)
        try{
            var res = getPropertiesDeffered.await()
            Log.i("api",res.toString())
            _offices.postValue(res)
        }catch (e :Exception){
            Log.i("api",e.message?:"no message but something went wrong with offices request")
        }
    }
}