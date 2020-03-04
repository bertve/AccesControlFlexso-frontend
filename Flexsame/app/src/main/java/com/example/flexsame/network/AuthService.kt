package com.example.flexsame.network

import com.example.flexsame.models.dto.auth.ApiResponse
import com.example.flexsame.models.dto.auth.JwtAuthenticationResponse
import com.example.flexsame.models.dto.auth.LoginRequest
import com.example.flexsame.models.dto.auth.SignUpRequest
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/signup")
    fun register(@Body signUpRequest: SignUpRequest) : Deferred<ApiResponse>

    @POST("auth/signin")
    fun login(@Body loginRequest: LoginRequest): Deferred<JwtAuthenticationResponse>

}