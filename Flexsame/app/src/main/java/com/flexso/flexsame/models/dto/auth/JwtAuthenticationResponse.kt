package com.flexso.flexsame.models.dto.auth

class JwtAuthenticationResponse(var accessToken: String) {
    var tokenType = "Bearer"
    override fun toString(): String {
        return "JwtAuthenticationResponse(accessToken='$accessToken', tokenType='$tokenType')"
    }

}