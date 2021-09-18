package com.prime.latticewhether.api.pincode

import com.prime.latticewhether.models.pincode.PincodeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PincodeAPI {
    @GET("/pincode/{pin}")
    suspend fun getDistrictAndState(
        @Path("pin") pincode : String,
    ) : Response<List<PincodeResponse>>
}