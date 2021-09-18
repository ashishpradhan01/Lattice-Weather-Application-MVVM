package com.prime.latticewhether.repository

import android.util.Log
import com.prime.latticewhether.api.pincode.PincodeRetrofitInstance
import com.prime.latticewhether.api.whether.WhetherRetrofitInstance
import com.prime.latticewhether.models.pincode.PincodeResponse
import retrofit2.Response
import retrofit2.Retrofit

class CommonRepository {
    suspend fun getDistrictAndState(pincode:String) =
       PincodeRetrofitInstance.pincodeAPI.getDistrictAndState(pincode)

    suspend fun getWhetherReport(cityName:String) =
        WhetherRetrofitInstance.whetherAPI.getWhetherReport(cityName = cityName)

}