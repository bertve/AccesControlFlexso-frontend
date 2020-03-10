package com.example.flexsame.repos

import android.util.Log
import com.example.flexsame.models.dto.auth.ApiResponse
import com.example.flexsame.models.dto.auth.SignUpRequest
import com.example.flexsame.network.AuthService

class RegisterRepository(val authService: AuthService) {
    suspend fun register(firstName: String, lastName: String, email: String, password :String):ApiResponse {
        try{
            val apiResponse = authService.register(SignUpRequest(firstName,lastName,email,password))
            val data = apiResponse.await()
            Log.i("register",data.toString())
            return data

        } catch (e: Throwable) {
            Log.i("register","something went wrong with the request")
            Log.i("register",e.message!!)
            return ApiResponse(false,"something went wrong with the request")
        }
    }
}