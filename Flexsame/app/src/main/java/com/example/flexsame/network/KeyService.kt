package com.example.flexsame.network

import com.example.flexsame.models.Office
import com.example.flexsame.models.User
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface KeyService {
    @GET("users/{id}/offices")
    fun getOffices(
        @Path("id") id:Long
    ): Deferred<List<Office>>

    @GET("users/me")
    fun getCurrentUser():Deferred<User>
}
