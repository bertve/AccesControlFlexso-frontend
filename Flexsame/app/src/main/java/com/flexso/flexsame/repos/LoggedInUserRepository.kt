package com.flexso.flexsame.repos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.flexso.flexsame.models.User
import com.flexso.flexsame.models.dto.UpdateAccountRequest
import com.flexso.flexsame.network.KeyService

class LoggedInUserRepository(private val keyService: KeyService) {
    private var _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    suspend fun getCurrentUser(token: String, password: String): Boolean {
        try {
            val getPropertiesDeffered = keyService.getCurrentUser()
            var res = getPropertiesDeffered.await()
            res.token = token
            res.password = password
            Log.i("currentUser", res.toString())
            _user.postValue(res)
            return true
        } catch (e: Exception) {
            Log.i("currentUser", e.message
                    ?: "no message but something went wrong with current user request")
            return false
        }
    }

    suspend fun update(user: User): Boolean {
        try {
            val response = keyService.updateUser(UpdateAccountRequest(user.userId, user.firstName, user.lastName, user.email, user.password))
            val data = response.await()
            Log.i("update_currentUser", data.toString())
            _user.postValue(data)
            return true
        } catch (e: Throwable) {
            Log.i("currentUser", e.message
                    ?: "no message but something went wrong with current user update request")
            return false
        }
    }
}