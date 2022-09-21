package com.dmm.rssreader.data.repositories

import com.dmm.rssreader.data.network.apis.AndroidBlogsApi
import com.dmm.rssreader.data.persistence.FeedsDao
import com.dmm.rssreader.domain.model.FeedUI
import com.dmm.rssreader.domain.repositories.RepositoryFeedAndroidBlogs
import com.dmm.rssreader.utils.Constants.SOURCE_BLOGS
import com.dmm.rssreader.utils.Resource
import com.dmm.rssreader.utils.Utils.Companion.handleResponse
import javax.inject.Inject

class RepositoryFeedAndroidBlogsImpl @Inject constructor(
	private val androidApi: AndroidBlogsApi,
	private val feedsDao: FeedsDao
) : RepositoryFeedAndroidBlogs {

	override suspend fun fetchFeedAndroidBlogs(): Resource<List<FeedUI>?> {
		var result = feedsDao.getFeedsList(SOURCE_BLOGS)
		if(result.isEmpty()) {
			result = handleResponse(androidApi.fetchDeveloperAndroidBlogs()).data!!
			saveFeedAndroidBlogs(result)
		}
		return Resource.Success(result)
	}

	override suspend fun saveFeedAndroidBlogs(feedsUI: List<FeedUI>) {
		feedsDao.insertFeeds(feedsUI)
	}

	override suspend fun saveFavouriteFeedAndroidBlogs(feedUI: FeedUI) {
		TODO("Not yet implemented")
	}
}