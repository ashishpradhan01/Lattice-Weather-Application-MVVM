package com.prime.latticewhether.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prime.latticewhether.models.whether.WhetherResponse
import com.prime.latticewhether.repository.CommonRepository
import com.prime.latticewhether.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class WhetherViewModel(
    val  commonRepository: CommonRepository
) : ViewModel() {
    val whetherReport : MutableLiveData<Resource<WhetherResponse>> = MutableLiveData()

    fun getWhetherReport(cityName : String) = viewModelScope.launch {
        whetherReport.postValue(Resource.Loading())
        val response = commonRepository.getWhetherReport(cityName)
        whetherReport.postValue(handleWhetherResponse(response))
    }

    private fun handleWhetherResponse(response: Response<WhetherResponse>): Resource<WhetherResponse> {
        if (response.isSuccessful){
            Log.d("viewmodel", "handleWhetherResponse: ${response.body()}")
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}