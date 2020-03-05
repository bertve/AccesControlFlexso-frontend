package com.example.flexsame.models.dto.auth

data class ApiResponse(var success: Boolean, var message: String) {
    override fun toString(): String {
        return "ApiResponse(success=$success, message='$message')"
    }
}