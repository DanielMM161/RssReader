package com.dmm.rssreader.data.repositories

import com.dmm.rssreader.data.persistence.FeedsDao
import com.dmm.rssreader.domain.model.FeedUI
import com.dmm.rssreader.domain.repositories.RepositoryFavouriteFeeds
import javax.inject.Inject

class RepositoryFavouriteFeedsImpl @Inject constructor(
    private val feedsDao: FeedsDao
) : RepositoryFavouriteFeeds {

  override suspend fun getFavouriteFeeds(): List<FeedUI> {
    return feedsDao.getFavouriteFeeds()
  }

  override fun saveFavouriteFeed(feedUI: FeedUI) {
    feedsDao.saveFavouriteFeed(feedUI)
  }
}