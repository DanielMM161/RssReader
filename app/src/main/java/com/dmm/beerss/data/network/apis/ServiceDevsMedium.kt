package com.dmm.beerss.data.network.apis

import retrofit2.Response
import retrofit2.http.GET

interface ServiceDevsMedium {
	@GET("feed/androiddevelopers")
	suspend fun fetchData(): Response<String>
}