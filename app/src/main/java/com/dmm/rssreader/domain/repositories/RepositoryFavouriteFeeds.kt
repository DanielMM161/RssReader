package com.dmm.rssreader.domain.repositories

import com.dmm.rssreader.domain.model.FeedUI
import kotlinx.coroutines.flow.Flow

interface RepositoryFavouriteFeeds {
  fun getFavouriteFeeds(): Flow<List<FeedUI>>
  suspend fun saveFavouriteFeed(feedUI: FeedUI)
  suspend fun updateFeed(favorite: Boolean, title: String)
}