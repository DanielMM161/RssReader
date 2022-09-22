package com.dmm.rssreader.domain.usecase

import com.dmm.rssreader.domain.model.FeedUI
import com.dmm.rssreader.domain.repositories.RepositoryFavouriteFeeds
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FavouriteFeedsUseCase @Inject constructor(
  private val repository: RepositoryFavouriteFeeds
) {
  suspend fun getFavouriteFeeds() = callbackFlow {
    trySend(repository.getFavouriteFeeds())
  }

  fun saveFavouriteFeed(feedUI: FeedUI) {
    repository.saveFavouriteFeed(feedUI)
  }
}