package com.flexso.flexsame.models

/**
 * User details post authentication that is exposed to the UI
 */
data class LoginSucces(
    val token : String,
    val email : String,
    val password : String
    //... other data fields that may be accessible to the UI
)
