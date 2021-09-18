package com.prime.latticewhether.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prime.latticewhether.models.pincode.PincodeResponse
import com.prime.latticewhether.repository.CommonRepository
import com.prime.latticewhether.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class PincodeViewModel(
    private val commonRepository: CommonRepository
) : ViewModel() {

    val districtAndState : MutableLiveData<Resource<PincodeResponse>> = MutableLiveData()

    fun getDistrictAndState(pincode : String) = viewModelScope.launch {
        districtAndState.postValue(Resource.Loading())
        val response = commonRepository.getDistrictAndState(pincode)
        districtAndState.postValue(handlePincodeResponse(response))
    }

    private fun handlePincodeResponse(response: Response<List<PincodeResponse>>): Resource<PincodeResponse> {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse[0])
            }
        }
        return  Resource.Error(response.message())
    }
}