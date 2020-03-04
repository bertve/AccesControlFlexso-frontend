package com.example.flexsame.models

import android.util.Log
import com.example.flexsame.models.LoggedInUser
import com.example.flexsame.models.Result
import com.example.flexsame.models.dto.auth.JwtAuthenticationResponse
import com.example.flexsame.models.dto.auth.LoginRequest
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

   fun login(username: String, password: String): Result<LoggedInUser> {

       try {
           val response : Call<JwtAuthenticationResponse> =  authService.login(LoginRequest(username,password))
           var isSucces = false
           var error  = Throwable("kaka")
           val call : Unit = response.enqueue(object : Callback<JwtAuthenticationResponse>{
                override fun onFailure(call: Call<JwtAuthenticationResponse>, t: Throwable) {
                    isSucces = false
                    error = t
                }

                override fun onResponse(
                    call: Call<JwtAuthenticationResponse>,
                    response: Response<JwtAuthenticationResponse>
                ) {
                    isSucces = true
                }

            })

           if(!isSucces){
               Log.i("login","oeioeioei")
               Log.i("login",error.message!!)
               return Result.Error(IOException("Error logging in", error))
           }
            val userId_int = 1
            val userId = userId_int.toLong()
            val fakeUser = LoggedInUser(userId, username)
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
          return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}

