package com.dmm.beerss.data.network.apis

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface ApiService {
	@GET("{path}")
	fun fetchData(@Path(value = "path", encoded = true) path: String): Call<String>
}