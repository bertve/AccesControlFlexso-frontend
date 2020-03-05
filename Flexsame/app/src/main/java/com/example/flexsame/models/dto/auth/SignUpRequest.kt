package com.example.flexsame.models.dto.auth

data class SignUpRequest (var firstName: String,
                         var lastName: String,
                         var email: String,
                         var password: String){
    override fun toString(): String {
        return "SignUpRequest(firstName='$firstName', lastName='$lastName', email='$email', password='$password')"
    }
}