package com.flexso.flexsame.ui.login

import com.flexso.flexsame.models.LoginSucces

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: LoginSucces? = null,
    val error: Int? = null
)
