package com.dmm.beerss.di

import com.dmm.beerss.data.network.apis.*
import com.dmm.beerss.utils.HostSelectionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

	@Provides
	@Singleton
	fun provideRetrofitApi(okHttpClient: OkHttpClient): Retrofit {
		return Retrofit.Builder()
			.baseUrl("https://thisis.my.fake.url/")
			.client(okHttpClient)
			.addConverterFactory(ScalarsConverterFactory.create())
			.build()
	}

	@Provides
	@Singleton
	fun provideApiService(retrofit: Retrofit): ApiService {
		return retrofit.create(ApiService::class.java)
	}

	@Provides
	@Singleton
	fun provideHostSelectionInterceptor(): HostSelectionInterceptor {
		return HostSelectionInterceptor()
	}

	@Provides
	@Singleton
	fun provideOkHttpClient(hostSelectionInterceptor: HostSelectionInterceptor?): OkHttpClient {
		return OkHttpClient().newBuilder()
			.retryOnConnectionFailure(true)
			.followRedirects(true)
			.followSslRedirects(true)
			.addInterceptor(hostSelectionInterceptor!!)
			.connectTimeout(10, TimeUnit.SECONDS)
			.readTimeout(10, TimeUnit.SECONDS)
			.build()
	}
}