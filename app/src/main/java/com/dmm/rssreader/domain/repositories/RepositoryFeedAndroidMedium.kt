package com.dmm.rssreader.domain.repositories

import com.dmm.rssreader.domain.model.Feed
import com.dmm.rssreader.domain.model.FeedUI
import retrofit2.Response

interface RepositoryFeedAndroidMedium {

	suspend fun fetchFeedAndroidMedium(): Response<Feed>
	suspend fun saveFeedAndroidMedium(feedUI: FeedUI)
	suspend fun saveFavouriteFeedAndroidMedium(feedUI: FeedUI)

}