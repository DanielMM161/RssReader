package com.dmm.beerss.utils

import androidx.annotation.StringRes

data class ValidationResult(
	val successful: Boolean,
	@StringRes val resId: Int? = null
)