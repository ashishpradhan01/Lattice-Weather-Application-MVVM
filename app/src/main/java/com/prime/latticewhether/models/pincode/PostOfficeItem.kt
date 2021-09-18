package com.prime.latticewhether.models.pincode

import com.google.gson.annotations.SerializedName

data class PostOfficeItem(
    @SerializedName("District")
    val district: String,
    @SerializedName("State")
    val state: String
)
