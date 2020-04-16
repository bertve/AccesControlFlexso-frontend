package com.flexso.flexsame.repos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.flexso.flexsame.models.Office
import com.flexso.flexsame.network.AdminService
import com.flexso.flexsame.network.CompanyService
import com.flexso.flexsame.network.KeyService

class KeyRepository(private val keyService: KeyService, private val adminService: AdminService, private val companyService: CompanyService) {
    var offices = MutableLiveData<List<Office>>()

    suspend fun getOffices(userId: Long) {
        var getPropertiesDeffered = keyService.getOffices(userId)
        try {
            var res = getPropertiesDeffered.await()
            Log.i("api", res.toString())
            offices.postValue(res)
        } catch (e: Exception) {
            Log.i("api", e.message ?: "no message but something went wrong with offices request")
        }
    }

    suspend fun getAllOffices() {
        var getPropertiesDeffered = adminService.getAllOffices()
        try {
            var res = getPropertiesDeffered.await()
            Log.i("api", res.toString())
            offices.postValue(res)
        } catch (e: Exception) {
            Log.i("api", e.message ?: "no message but something went wrong with offices request")
        }
    }

    suspend fun getAllOfficesFromCompany(companyId: Long) {
        var getPropertiesDeffered = companyService.getOffices(companyId)
        try {
            var res = getPropertiesDeffered.await()
            Log.i("api", res.toString())
            offices.postValue(res)
        } catch (e: Exception) {
            Log.i("api", e.message ?: "no message but something went wrong with offices request")
        }
    }
}