package com.dmm.rssreader.domain.repositories

import com.dmm.rssreader.domain.model.FeedUI

interface RepositoryFavouriteFeeds {
  suspend fun getFavouriteFeeds(): List<FeedUI>
  fun saveFavouriteFeed(feedUI: FeedUI)
}