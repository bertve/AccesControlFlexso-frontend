package com.example.flexsame.ui.login

import com.example.flexsame.models.LoginSucces

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: LoginSucces? = null,
    val error: Int? = null
)
