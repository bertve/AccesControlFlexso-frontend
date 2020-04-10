package com.flexso.flexsame.network

import com.flexso.flexsame.models.Address
import com.flexso.flexsame.models.Company
import com.flexso.flexsame.models.Office
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface CompanyService {
    @GET("companies/{companyId}/offices")
    fun getOffices(@Path("companyId") companyId:Long): Deferred<List<Office>>
    @DELETE("companies/{id}/offices/{officeId}")
    fun removeOffice(@Path("id") companyId:Long ,@Path("officeId") officeId: Long): Deferred<Boolean>
    @POST("companies/{id}/offices")
    fun addOffice(@Path("id")companyId: Long,@Body a: Address): Deferred<Boolean>
    @PUT("companies")
    fun updateCompany(@Body c: Company): Deferred<Boolean>
}