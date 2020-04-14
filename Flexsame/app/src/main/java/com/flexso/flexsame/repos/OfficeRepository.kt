package com.flexso.flexsame.repos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.flexso.flexsame.models.Office
import com.flexso.flexsame.models.User
import com.flexso.flexsame.network.CompanyService
import java.lang.Exception

class OfficeRepository (private val companyService : CompanyService) {

    val authorizedPersons = MutableLiveData<List<User>>()
    val unAuthorizedPersons = MutableLiveData<List<User>>()

    suspend fun getAuthorizedPersons(officeId: Long) {
        val response = companyService.getAuthorizedPersons(officeId)
        try {
            val data = response.await()
            Log.i("api",data.toString())
            authorizedPersons.postValue(data)
        }catch (e : Exception){
            Log.i("api",e.message?:"no message but something went wrong with AuthorizedPersons request")
        }
    }

    suspend fun deAuthorizePerson(officeId : Long,userId: Long): Boolean {
        val response = companyService.deAuthorizePerson(officeId,userId)
        try {
            val data : Boolean = response.await()
            Log.i("api",data.toString())
            return data
        }catch (e : Exception){
            Log.i("api",e.message?:"no message but something went wrong with AuthorizedPersons request")
            return false
        }
    }

    suspend fun authorizePerson(officeId: Long, userId : Long): Boolean {
        val response = companyService.authorizePerson(officeId,userId)
        try {
            val data : Boolean = response.await()
            Log.i("api",data.toString())
            return data
        }catch (e : Exception){
            Log.i("api",e.message?:"no message but something went wrong with AuthorizedPersons request")
            return false
        }
    }

    suspend fun updateOffice(o : Office): Boolean {
        val response = companyService.updateOffice(o)
        try {
            val data : Boolean = response.await()
            Log.i("api",data.toString())
            return data
        }catch (e : Exception){
            Log.i("api",e.message?:"no message but something went wrong with AuthorizedPersons request")
            return false
        }
    }

    suspend fun getUnAuthorizedPersons(officeId: Long){
        val response = companyService.getUnAuthorizedPersons(officeId)
        try {
            val data = response.await()
            Log.i("unAuth",data.toString())
            unAuthorizedPersons.postValue(data)
        }catch (e : Exception){
            Log.i("unAuth",e.message?:"no message but something went wrong with deAuthorizedPersons request")
        }
    }


}