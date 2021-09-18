package com.prime.latticewhether.utils

//A Wrapper class, which wrap's api response

sealed class Resource<T>(
    val data: T? =null,
    val message : String? = null
) {
    class Success<T>(data:T) : Resource<T>(data)
    class Error<T>(message: String) : Resource<T>(message=message)
    class Loading<T> : Resource<T>()
}