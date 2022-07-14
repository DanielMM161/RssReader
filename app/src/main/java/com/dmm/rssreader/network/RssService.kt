package com.dmm.rssreader.network

import com.dmm.rssreader.model.Feed
import com.dmm.rssreader.model.feedandroidblogs.FeedAndroidBlogs
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