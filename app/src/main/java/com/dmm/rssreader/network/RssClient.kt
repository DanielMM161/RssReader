package com.dmm.rssreader.network

import com.dmm.rssreader.model.Feed
import com.dmm.rssreader.model.feedandroidblogs.FeedAndroidBlogs
import retrofit2.Response
import javax.inject.Inject

class RssClient @Inject constructor(
	private val rssService: RssService
) {

	suspend fun fetchDeveloperApple() : Response<Feed> = rssService.fetchDeveloperApple()
	suspend fun fetchDeveloperAndroidNews() : Response<Feed> = rssService.fetchDeveloperAndroidNews()
	suspend fun fetchDeveloperAndroidBlogs() : Response<FeedAndroidBlogs> = rssService.fetchDeveloperAndroidBlogs()

}