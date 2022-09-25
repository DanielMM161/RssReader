package com.dmm.rssreader.data.network.apis

import com.dmm.rssreader.domain.model.feedandroidblogs.FeedAndroidBlogs
import retrofit2.Response
import retrofit2.http.GET

interface AndroidBlogsApi {
	@GET("blogspot/hsDu")
	suspend fun fetchDeveloperAndroidBlogs() : Response<FeedAndroidBlogs>
}