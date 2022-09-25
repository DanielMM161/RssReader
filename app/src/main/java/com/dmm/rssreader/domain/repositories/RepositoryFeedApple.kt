package com.dmm.rssreader.domain.repositories

import com.dmm.rssreader.domain.model.FeedUI
import com.dmm.rssreader.utils.Resource

interface RepositoryFeedApple {
	suspend fun fetchFeedApple(): Resource<List<FeedUI>?>
	suspend fun saveFeedApple(feedUI: List<FeedUI>)
	suspend fun saveFavouriteFeedApple(feedUI: FeedUI)
}