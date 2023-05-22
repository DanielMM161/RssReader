package com.dmm.beerss.utils

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.jetbrains.annotations.NotNull
import java.io.IOException
import java.net.URISyntaxException
import javax.inject.Singleton

@Singleton
class HostSelectionInterceptor() : Interceptor {
	@Volatile
	private var dynamicUrl = "";

	fun setDynamicUrl(url: String) {
		dynamicUrl = url;
	}

	@NotNull
	@Throws(IOException::class)
	override fun intercept(chain: Interceptor.Chain): Response {
		val originalRequest = chain.request()
		val modifieRequest = originalRequest.newBuilder()
			.url(dynamicUrl + originalRequest.url.encodedPath)
			.build()
		return chain.proceed(modifieRequest)
//		if (host != null) {
//			var newUrl: HttpUrl? = null
//			try {
//				newUrl = request.url.newBuilder()
//					.scheme(host!!.scheme)
//					.host(host!!.toUrl().toURI().host)
//					.build()
//			} catch (e: URISyntaxException) {
//				e.printStackTrace()
//			}
//			assert(newUrl != null)
//			request = request.newBuilder()
//				.url(newUrl!!)
//				.build()
//		}
//		return chain.proceed(request)
	}
}