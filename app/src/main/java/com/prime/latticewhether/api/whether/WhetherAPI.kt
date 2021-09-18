package com.prime.latticewhether.api.whether

import com.prime.latticewhether.models.pincode.PincodeResponse
import com.prime.latticewhether.models.whether.WhetherResponse
import com.prime.latticewhether.utils.Constants.Companion.WHETHER_API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WhetherAPI {
    @GET("/v1/current.json")
    suspend fun getWhetherReport(
        @Query("key")
        key : String = WHETHER_API_KEY,
        @Query("q")
        cityName : String,
        @Query("aqi")
        aqi : String = "no"
    ) : Response<WhetherResponse>
}




