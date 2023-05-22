package com.dmm.beerss.domain.model

import androidx.annotation.DrawableRes

data class SingleSources(
	val title: String,
	@DrawableRes var imageRes: Int
)
