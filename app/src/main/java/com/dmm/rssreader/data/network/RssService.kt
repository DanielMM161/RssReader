package com.dmm.rssreader.data.network

import com.dmm.rssreader.domain.model.Feed
import com.dmm.rssreader.domain.model.feedandroidblogs.FeedAndroidBlogs
import retrofit2.Response
import retrofit2.http.GET

interface RssService {

	@GET("news")
	suspend fun fetchDeveloperAndroidNews() : Response<Feed>

	@GET("blogspot/hsDu")
	suspend fun fetchDeveloperAndroidBlogs() : Response<FeedAndroidBlogs>

	@GET("news/rss/news.rss")
	suspend fun fetchDeveloperApple() : Response<Feed>

}