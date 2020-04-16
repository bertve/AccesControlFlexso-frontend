package com.flexso.flexsame.network

import com.flexso.flexsame.models.Office
import com.flexso.flexsame.models.User
import com.flexso.flexsame.models.dto.UpdateAccountRequest
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface KeyService {
    @GET("users/{id}/offices")
    fun getOffices(
            @Path("id") id: Long
    ): Deferred<List<Office>>

    @GET("users/me")
    fun getCurrentUser(): Deferred<User>

    @PUT("users")
    fun updateUser(@Body updateAccountRequest: UpdateAccountRequest): Deferred<User>
}
