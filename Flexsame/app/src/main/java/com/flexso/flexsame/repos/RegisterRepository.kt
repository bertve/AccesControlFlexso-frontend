package com.flexso.flexsame.repos

import android.util.Log
import com.flexso.flexsame.models.dto.auth.ApiResponse
import com.flexso.flexsame.models.dto.auth.SignUpRequest
import com.flexso.flexsame.network.AuthService

class RegisterRepository(val authService: AuthService) {
    suspend fun register(firstName: String, lastName: String, email: String, password: String): ApiResponse {
        try {
            val apiResponse = authService.register(SignUpRequest(firstName, lastName, email, password))
            val data = apiResponse.await()
            Log.i("register", data.toString())
            return data

        } catch (e: Throwable) {
            Log.i("register", e.message
                    ?: "no message but something went wrong with register request")
            return ApiResponse(false, "something went wrong with the request")
        }
    }
}