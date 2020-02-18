package com.example.flexsame.network

import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface KeyService {
    //spots
    @GET("Keys")
    fun getKeys(): Deferred<List<String>>
}