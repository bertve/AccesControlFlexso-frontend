package com.flexso.flexsame.repos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.flexso.flexsame.models.Address
import com.flexso.flexsame.models.Company
import com.flexso.flexsame.models.Office
import com.flexso.flexsame.network.CompanyService

class CompanyRepository(private val companyService: CompanyService) {

    var offices = MutableLiveData<List<Office>>()

    suspend fun getOffices(companyId: Long) {
        var response = companyService.getOffices(companyId)
        try {
            val data = response.await()
            Log.i("api", data.toString())
            offices.postValue(data)
        } catch (e: Exception) {
            Log.i("api", e.message ?: "no message but something went wrong with offices request")
        }
    }

    suspend fun removeOffice(companyId: Long, officeId: Long): Boolean {
        var response = companyService.removeOffice(companyId, officeId)
        try {
            val data: Boolean = response.await()
            Log.i("api", data.toString())
            return data
        } catch (e: Exception) {
            Log.i("api", e.message ?: "no message but something went wrong with offices request")
            return false
        }
    }

    suspend fun addOffice(companyId: Long, a: Address): Boolean {
        var response = companyService.addOffice(companyId, a)
        try {
            val data: Boolean = response.await()
            Log.i("api", data.toString())
            return data
        } catch (e: Exception) {
            Log.i("api", e.message ?: "no message but something went wrong with offices request")
            return false
        }
    }

    suspend fun updateCompany(c: Company): Boolean {
        var response = companyService.updateCompany(c)
        try {
            val data: Boolean = response.await()
            Log.i("api", data.toString())
            return data
        } catch (e: Exception) {
            Log.i("api", e.message ?: "no message but something went wrong with offices request")
            return false
        }
    }


}