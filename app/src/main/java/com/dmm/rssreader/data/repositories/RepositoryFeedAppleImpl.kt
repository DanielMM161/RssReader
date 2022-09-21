package com.dmm.rssreader.data.repositories

import com.dmm.rssreader.data.network.apis.AppleApi
import com.dmm.rssreader.data.persistence.FeedsDao
import com.dmm.rssreader.domain.model.FeedUI
import com.dmm.rssreader.domain.repositories.RepositoryFeedApple
import com.dmm.rssreader.utils.Constants.SOURCE_APPLE
import com.dmm.rssreader.utils.Resource
import com.dmm.rssreader.utils.Utils.Companion.handleResponse
import javax.inject.Inject

class RepositoryFeedAppleImpl @Inject constructor(
	private val appleApi: AppleApi,
	private val feedsDao: FeedsDao
) : RepositoryFeedApple{
	override suspend fun fetchFeedApple(): Resource<List<FeedUI>?> {
		var result = feedsDao.getFeedsList(SOURCE_APPLE)
		if(result.isEmpty()) {
			result = handleResponse(appleApi.fetchDeveloperApple()).data!!
			saveFeedApple(result)
		}
		return Resource.Success(result)
	}

	override suspend fun saveFeedApple(feedUI: List<FeedUI>) {
		feedsDao.insertFeeds(feedUI)
	}

	override suspend fun saveFavouriteFeedApple(feedUI: FeedUI) {
		TODO("Not yet implemented")
	}
}