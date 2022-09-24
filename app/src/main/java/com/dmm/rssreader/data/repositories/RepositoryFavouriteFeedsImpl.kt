package com.dmm.rssreader.data.repositories

import com.dmm.rssreader.data.persistence.FeedsDao
import com.dmm.rssreader.domain.model.FeedUI
import com.dmm.rssreader.domain.repositories.RepositoryFavouriteFeeds
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryFavouriteFeedsImpl @Inject constructor(
    private val feedsDao: FeedsDao
) : RepositoryFavouriteFeeds {

  override fun getFavouriteFeeds(): Flow<List<FeedUI>> {
    return feedsDao.getFavouriteFeeds()
  }

  override suspend fun saveFavouriteFeed(feedUI: FeedUI) {
    feedsDao.saveFavouriteFeed(feedUI)
  }

  override suspend fun updateFeed(favorite: Boolean, title: String) {
    feedsDao.updateFeed(favorite, title)
  }
}