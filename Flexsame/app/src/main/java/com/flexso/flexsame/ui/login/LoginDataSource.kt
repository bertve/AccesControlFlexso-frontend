package com.flexso.flexsame.ui.login

import android.util.Log
import com.flexso.flexsame.models.LoginSucces
import com.flexso.flexsame.models.Result
import com.flexso.flexsame.models.dto.auth.LoginRequest
import com.flexso.flexsame.network.AuthService
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource(private val authService: AuthService) {

    suspend fun login(username: String, password: String): Result<LoginSucces> {
        try {
            val jwtResponse = authService.login(LoginRequest(username, password))
            val data = jwtResponse.await()
            Log.i("login", data.toString())
            val token = LoginSucces(data.accessToken, username, password)
            return Result.Success(token)
        } catch (e: Throwable) {
            Log.i("login", e.message!!)
            return Result.Error(IOException("Error logging in", e))
        }
    }


}

