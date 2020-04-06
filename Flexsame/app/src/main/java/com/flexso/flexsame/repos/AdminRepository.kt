package com.flexso.flexsame.repos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.flexso.flexsame.models.Office
import com.flexso.flexsame.models.User
import com.flexso.flexsame.network.AdminService
import java.lang.Exception

class AdminRepository(private val adminService: AdminService) {
    private var _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    suspend fun getCompanyUsers(){
        var getPropertiesDeffered = adminService.getCompanyUsers()
        try{
            var res = getPropertiesDeffered.await()
            Log.i("api",res.toString())
            _users.postValue(res)
        }catch (e : Exception){
            Log.i("api",e.message?:"no message but something went wrong with offices request")
        }
    }
}