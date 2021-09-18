package com.prime.latticewhether.models.pincode

import com.google.gson.annotations.SerializedName

data class PincodeResponse(
	@SerializedName("Message")
	val message: String,
	@SerializedName("Status")
	val status: String,
	@SerializedName("PostOffice")
	val postOffice: List<PostOfficeItem>
)

