package com.prime.latticewhether.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prime.latticewhether.repository.CommonRepository

class PincodeViewModelFactory(
    val commonRepository: CommonRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        PincodeViewModel(commonRepository) as T

}