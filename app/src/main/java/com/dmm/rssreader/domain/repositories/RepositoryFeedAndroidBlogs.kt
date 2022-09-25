package com.dmm.rssreader.domain.repositories

import com.dmm.rssreader.domain.model.FeedUI
import com.dmm.rssreader.utils.Resource

interface RepositoryFeedAndroidBlogs {
	suspend fun fetchFeedAndroidBlogs(): Resource<List<FeedUI>?>
	suspend fun saveFeedAndroidBlogs(feedUI: List<FeedUI>)
}