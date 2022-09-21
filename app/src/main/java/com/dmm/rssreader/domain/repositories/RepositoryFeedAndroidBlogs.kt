package com.dmm.rssreader.domain.repositories

import com.dmm.rssreader.domain.model.FeedUI
import com.dmm.rssreader.domain.model.feedandroidblogs.FeedAndroidBlogs
import com.dmm.rssreader.utils.Resource
import retrofit2.Response

interface RepositoryFeedAndroidBlogs {
	suspend fun fetchFeedAndroidBlogs(): Resource<List<FeedUI>?>
	suspend fun saveFeedAndroidBlogs(feedUI: List<FeedUI>)
	suspend fun saveFavouriteFeedAndroidBlogs(feedUI: FeedUI)
}