package com.flexso.flexsame.network

import com.flexso.flexsame.models.KeyRequest
import com.flexso.flexsame.models.Office
import com.flexso.flexsame.models.User
import com.flexso.flexsame.models.dto.UpdateAccountRequest
import com.flexso.flexsame.models.dto.auth.JwtAuthenticationResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface KeyService {

    @GET("users/{id}/offices")
    fun getOffices(
            @Path("id") id: Long
    ): Deferred<List<Office>>

    @GET("users/me")
    fun getCurrentUser(): Deferred<User>

    @PUT("users")
    fun updateUser(@Body updateAccountRequest: UpdateAccountRequest): Deferred<User>

    @POST("gate/genKey")
    fun generateKey(@Body keyRequest: KeyRequest): Deferred<JwtAuthenticationResponse>

}
