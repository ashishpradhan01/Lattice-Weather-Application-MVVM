package com.prime.latticewhether.models.whether

import com.google.gson.annotations.SerializedName

data class Current(
	@SerializedName("temp_c")
	val tempC: Double,
	@SerializedName("temp_f")
	val tempF: Double,
	@SerializedName("wind_kph")
	val windKph: Double,
	@SerializedName("condition")
	val condition: Condition,
	@SerializedName("humidity")
	val humidity: Int,
)
