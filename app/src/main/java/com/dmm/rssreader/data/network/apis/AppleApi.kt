package com.dmm.rssreader.data.network.apis

import com.dmm.rssreader.domain.model.Feed
import retrofit2.Response
import retrofit2.http.GET

interface AppleApi {
	@GET("news/rss/news.rss")
	suspend fun fetchDeveloperApple() : Response<Feed>
}