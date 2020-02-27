package com.example.flexsame.network

import com.example.flexsame.models.Office
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface KeyService {
    @GET("authorizedPersons/{id}/offices")
    fun getOffices(
        @Path("id") id:Long
    ): Deferred<List<Office>>
}
