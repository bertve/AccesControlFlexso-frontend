package com.flexso.flexsame.models


data class LoginSucces(
        val token: String,
        val email: String,
        val password: String
        //... other data fields that may be accessible to the UI
)
