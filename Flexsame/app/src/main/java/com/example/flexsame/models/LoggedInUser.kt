package com.example.flexsame.models

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val userId: Long,
    val displayName: String
) {
    override fun toString(): String {
        return "LoggedInUser(userId=$userId, displayName='$displayName')"
    }
}
