package com.flexso.flexsame.network

import com.flexso.flexsame.models.Address
import com.flexso.flexsame.models.Company
import com.flexso.flexsame.models.Office
import com.flexso.flexsame.models.User
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface CompanyService {
    //comany_frag
    @GET("companies/{companyId}/offices")
    fun getOffices(@Path("companyId") companyId:Long): Deferred<List<Office>>
    @DELETE("companies/{id}/offices/{officeId}")
    fun removeOffice(@Path("id") companyId:Long ,@Path("officeId") officeId: Long): Deferred<Boolean>
    @POST("companies/{id}/offices")
    fun addOffice(@Path("id")companyId: Long,@Body a: Address): Deferred<Boolean>
    @PUT("companies")
    fun updateCompany(@Body c: Company): Deferred<Boolean>

    //office_frag
    @GET("offices/{officeId}/authorizedPersons")
    fun getAuthorizedPersons(@Path("officeId")officeId: Long):Deferred<List<User>>
    @DELETE("offices/{officeId}/authorizedPersons/{userId}")
    fun deAuthorizePerson(@Path("officeId")officeId: Long,@Path("userId") userId: Long): Deferred<Boolean>
    @POST("offices/{officeId}/authorizedPersons/{userId}")
    fun authorizePerson(@Path("officeId")officeId: Long,@Path("userId") userId: Long): Deferred<Boolean>
    @PUT("offices")
    fun updateOffice(@Body o: Office): Deferred<Boolean>
    @GET("offices/{officeId}/unAuthorizedPersons")
    fun getUnAuthorizedPersons(@Path("officeId")officeId: Long):Deferred<List<User>>
}