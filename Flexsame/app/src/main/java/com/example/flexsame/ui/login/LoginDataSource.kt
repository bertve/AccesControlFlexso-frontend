package com.example.flexsame.ui.login

import android.util.Log
import com.example.flexsame.models.LoggedInUser
import com.example.flexsame.models.Result
import com.example.flexsame.models.dto.auth.JwtAuthenticationResponse
import com.example.flexsame.models.dto.auth.LoginRequest
import com.example.flexsame.network.AuthInterceptor
import com.example.flexsame.network.AuthService
import com.example.flexsame.network.KeyService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource(private val authService : AuthService) {

   suspend fun login(username: String, password: String): Result<LoggedInUser> {
       try{
           Log.i("login","datasource")
           Log.i("login",username)
           Log.i("login",password)
            val jwtResponse = authService.login(LoginRequest(username,password))
            val data = jwtResponse.await()
            Log.i("login",data.toString())
            val userId = 1L
            val fakeUser = LoggedInUser(userId, username)
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
           Log.i("login",e.message!!)
           return Result.Error(IOException("Error logging in", e))
       }
    }

    fun logout() {
        // TODO: revoke authentication
    }

    fun updateHeader(token: String) {
        AuthInterceptor.setSessionToken(token)
        Log.i("token", "token: ${AuthInterceptor.getToken()}")

    }
}

