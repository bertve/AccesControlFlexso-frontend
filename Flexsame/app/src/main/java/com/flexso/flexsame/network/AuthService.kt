package com.flexso.flexsame.network

import com.flexso.flexsame.models.dto.auth.ApiResponse
import com.flexso.flexsame.models.dto.auth.JwtAuthenticationResponse
import com.flexso.flexsame.models.dto.auth.LoginRequest
import com.flexso.flexsame.models.dto.auth.SignUpRequest
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/signup")
    fun register(@Body signUpRequest: SignUpRequest): Deferred<ApiResponse>

    @POST("auth/signin")
    fun login(@Body loginRequest: LoginRequest): Deferred<JwtAuthenticationResponse>

}