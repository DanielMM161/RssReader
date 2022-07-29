package com.dmm.rssreader.di

import com.dmm.rssreader.network.RssClient
import com.dmm.rssreader.network.RssService
import com.dmm.rssreader.utils.Constants.DEVELOPER_ANDROID_BLOG
import com.dmm.rssreader.utils.HostSelectionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

	@Provides
	@Singleton
	fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
		return Retrofit.Builder()
			.baseUrl(DEVELOPER_ANDROID_BLOG)
			.client(okHttpClient)
			.addConverterFactory(SimpleXmlConverterFactory.create())
			.build()
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
			.connectTimeout(200, TimeUnit.SECONDS)
			.readTimeout(200, TimeUnit.SECONDS)
			.build()
	}

	@Provides
	@Singleton
	fun provideRssService(retrofit: Retrofit) : RssService{
		return retrofit.create(RssService::class.java)
	}

	@Provides
	@Singleton
	fun provideRssClient(rssService: RssService): RssClient {
		return RssClient(rssService)
	}

}