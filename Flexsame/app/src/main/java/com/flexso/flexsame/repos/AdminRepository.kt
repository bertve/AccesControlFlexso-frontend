package com.flexso.flexsame.repos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.flexso.flexsame.models.Office
import com.flexso.flexsame.models.User
import com.flexso.flexsame.models.dto.UpdateRequest
import com.flexso.flexsame.models.dto.auth.ApiResponse
import com.flexso.flexsame.models.dto.auth.SignUpRequestCompany
import com.flexso.flexsame.network.AdminService
import java.lang.Exception

class AdminRepository(private val adminService: AdminService) {
    var _users = MutableLiveData<List<User>>()

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

    suspend fun addCompany(signUpRequestCompany: SignUpRequestCompany): Boolean{
        val response = adminService.addCompany(signUpRequestCompany)
        try {
            val data : ApiResponse = response.await()
            Log.i("add_company",data.toString())
            return data.success
        }catch(e: Throwable) {
            Log.i("add_company","something went wrong with the request")
            Log.i("add_company",e.message!!)
            return false
        }
    }

    suspend fun removeCompany(userId: Long): Boolean {
        val response = adminService.deleteCompany(userId)
        try {
            val data : Boolean = response.await()
            Log.i("remove_company",data.toString())
            return data
        }catch(e: Throwable) {
            Log.i("remove_company","something went wrong with the request")
            Log.i("remove_company",e.message!!)
            return false
        }
    }
}