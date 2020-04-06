package com.flexso.flexsame.network

import com.flexso.flexsame.models.Office
import com.flexso.flexsame.models.User
import com.flexso.flexsame.models.dto.auth.ApiResponse
import com.flexso.flexsame.models.dto.auth.SignUpRequestCompany
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface AdminService {
    @GET("users/companies")
    fun getCompanyUsers(): Deferred<List<User>>
    @GET("offices")
    fun getAllOffices():Deferred<List<Office>>
    @DELETE("users/{id}")
    fun deleteCompany(  @Path("id") id:Long): Deferred<Boolean>
    @POST("auth/company/signup")
    fun addCompany(@Body singUpRequestCompany: SignUpRequestCompany): Deferred<ApiResponse>

}