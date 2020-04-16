package com.flexso.flexsame.repos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.flexso.flexsame.models.User
import com.flexso.flexsame.models.dto.auth.ApiResponse
import com.flexso.flexsame.models.dto.auth.SignUpRequestCompany
import com.flexso.flexsame.network.AdminService

class AdminRepository(private val adminService: AdminService) {
    var _users = MutableLiveData<List<User>>()

    suspend fun getCompanyUsers() {
        var getPropertiesDeffered = adminService.getCompanyUsers()
        try {
            var res = getPropertiesDeffered.await()
            Log.i("api", res.toString())
            _users.postValue(res)
        } catch (e: Exception) {
            Log.i("api", e.message
                    ?: "no message but something went wrong with CompanyUsers request")
        }
    }

    suspend fun addCompany(signUpRequestCompany: SignUpRequestCompany): Boolean {
        val response = adminService.addCompany(signUpRequestCompany)
        try {
            val data: ApiResponse = response.await()
            Log.i("api", data.toString())
            return data.success
        } catch (e: Exception) {
            Log.i("api", e.message
                    ?: "no message but something went wrong with CompanyUsers request")
            return false
        }
    }

    suspend fun removeCompany(userId: Long): Boolean {
        val response = adminService.deleteCompany(userId)
        try {
            val data: Boolean = response.await()
            Log.i("api", data.toString())
            return data
        } catch (e: Exception) {
            Log.i("api", e.message
                    ?: "no message but something went wrong with CompanyUsers request")
            return false
        }
    }
}