package com.dmm.rssreader.data.network.apis

import com.dmm.rssreader.domain.model.Feed
import com.dmm.rssreader.domain.model.feedandroidblogs.FeedAndroidBlogs
import retrofit2.Response
import retrofit2.http.GET

interface ServiceAndroidBlogs {
	@GET("blogspot/hsDu")
	suspend fun fetchData() : Response<String>
}