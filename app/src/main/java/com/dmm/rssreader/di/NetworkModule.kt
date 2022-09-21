package com.dmm.rssreader.di

import com.dmm.rssreader.data.network.RssClient
import com.dmm.rssreader.data.network.RssService
import com.dmm.rssreader.data.network.apis.AndroidBlogsApi
import com.dmm.rssreader.data.network.apis.AppleApi
import com.dmm.rssreader.utils.Constants.DEVELOPER_ANDROID_BLOG
import com.dmm.rssreader.utils.Constants.DEVELOPER_APPEL
import com.dmm.rssreader.utils.HostSelectionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
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
	@Named("AndroidBlogs")
	fun provideRetrofitAndroidBlogs(okHttpClient: OkHttpClient): Retrofit {
		return Retrofit.Builder()
			.baseUrl(DEVELOPER_ANDROID_BLOG)
			.client(okHttpClient)
			.addConverterFactory(SimpleXmlConverterFactory.create())
			.build()
	}

	@Provides
	@Singleton
	@Named("Apple")
	fun provideRetrofitApple(okHttpClient: OkHttpClient): Retrofit {
		return Retrofit.Builder()
			.baseUrl(DEVELOPER_APPEL)
			.client(okHttpClient)
			.addConverterFactory(SimpleXmlConverterFactory.create())
			.build()
	}

//	@Provides
//	@Singleton
//	@Named("AndroidMedium")
//	fun provideRetrofitAndroidMedium(okHttpClient: OkHttpClient): Retrofit {
//		return Retrofit.Builder()
//			.baseUrl(DEVELOPER_ANDROID_BLOG)
//			.client(okHttpClient)
//			.addConverterFactory(SimpleXmlConverterFactory.create())
//			.build()
//	}

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
	fun provideAndroidBlogsApi(@Named("AndroidBlogs") retrofit: Retrofit) : AndroidBlogsApi {
		return retrofit.create(AndroidBlogsApi::class.java)
	}

	@Provides
	@Singleton
	fun provideAndroidAppleApi(@Named("Apple") retrofit: Retrofit) : AppleApi {
		return retrofit.create(AppleApi::class.java)
	}

	@Provides
	@Singleton
	fun provideRssService(retrofit: Retrofit) : RssService {
		return retrofit.create(RssService::class.java)
	}

	@Provides
	@Singleton
	fun provideRssClient(rssService: RssService): RssClient {
		return RssClient(rssService)
	}

}