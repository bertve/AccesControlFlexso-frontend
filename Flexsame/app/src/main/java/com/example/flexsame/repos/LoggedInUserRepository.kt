package com.example.flexsame.repos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.flexsame.models.User
import com.example.flexsame.models.dto.UpdateRequest
import com.example.flexsame.network.AuthInterceptor
import com.example.flexsame.network.KeyService
import java.lang.Exception

class LoggedInUserRepository(private val keyService: KeyService) {
    private var _user = MutableLiveData<User>()
    val user:LiveData<User> get() = _user

    suspend fun getCurrentUser(token: String,password:String) : Boolean{
        Log.i("currentUser","AUTHINT: "+AuthInterceptor.getToken())
        var getPropertiesDeffered = keyService.getCurrentUser()
       try{
            var res = getPropertiesDeffered.await()
           res.token = token
           res.password = password
            Log.i("currentUser",res.toString())
            _user.postValue(res)
           return true
        }catch (e : Exception){
        Log.i("currentUser",e.message)
           return false
        }
    }

    suspend fun update(user : User) : Boolean{
        try {
            val response = keyService.updateUser(UpdateRequest(user.userId,user.firstName,user.lastName,user.email,user.password))
            val data = response.await()
            Log.i("update",data.toString())
            _user.postValue(data)
            return true
        }catch(e: Throwable) {
            Log.i("update","something went wrong with the request")
            Log.i("update",e.message!!)
            return false
        }
    }
}