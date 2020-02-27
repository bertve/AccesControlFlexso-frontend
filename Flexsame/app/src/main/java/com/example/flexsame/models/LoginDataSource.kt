package com.example.flexsame.models

import com.example.flexsame.models.LoggedInUser
import com.example.flexsame.models.Result
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
       try {
            // TODO: handle loggedInUser authentication
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

