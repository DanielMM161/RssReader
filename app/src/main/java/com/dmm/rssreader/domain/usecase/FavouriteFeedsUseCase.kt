package com.dmm.rssreader.domain.usecase

import com.dmm.rssreader.domain.model.FeedUI
import com.dmm.rssreader.domain.repositories.RepositoryFavouriteFeeds
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FavouriteFeedsUseCase @Inject constructor(
  private val repository: RepositoryFavouriteFeeds
) {
  fun getFavouriteFeeds(): Flow<List<FeedUI>> {
    return repository.getFavouriteFeeds()
  }

  suspend fun saveFavouriteFeed(feedUI: FeedUI) {
    repository.saveFavouriteFeed(feedUI)
  }

  suspend fun updateFavouriteFeed(favorite: Boolean,title: String) {
    repository.updateFeed(favorite, title)
  }
}