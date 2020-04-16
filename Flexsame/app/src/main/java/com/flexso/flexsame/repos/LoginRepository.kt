package com.flexso.flexsame.repos

import com.flexso.flexsame.models.LoginSucces
import com.flexso.flexsame.models.Result
import com.flexso.flexsame.ui.login.LoginDataSource

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    var loginSucces: LoginSucces? = null
        private set

    init {
        loginSucces = null
    }


    suspend fun login(username: String, password: String): Result<LoginSucces> {
        // handle login
        val result = dataSource.login(username, password)

        if (result is Result.Success) {
            setLoginSucces(result.data)
        }

        return result
    }

    private fun setLoginSucces(loginSucces: LoginSucces) {
        this.loginSucces = loginSucces

    }
}
