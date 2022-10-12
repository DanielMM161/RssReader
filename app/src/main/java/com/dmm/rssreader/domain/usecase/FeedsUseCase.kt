package com.dmm.rssreader.domain.usecase

import com.dmm.rssreader.domain.model.FeedUI
import com.dmm.rssreader.domain.repositories.RepositoryFeeds
import com.dmm.rssreader.utils.Resource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FeedsUseCase @Inject constructor(
	private val repository: RepositoryFeeds
) {

	suspend fun fetchFeeds(source: String): Resource<List<FeedUI>?> = GlobalScope.async {
		return@async repository.fetchFeeds(source)
	}.await()

	fun getFavouriteFeeds(): Flow<List<FeedUI>> {
		return repository.getFavouriteFeeds()
	}

	suspend fun saveFavouriteFeed(feedUI: FeedUI) {
		repository.saveFavouriteFeed(feedUI)
	}

	suspend fun updateFavouriteFeed(favorite: Boolean,title: String) {
		repository.updateFeed(favorite, title)
	}

	fun updateFavouritesFeedsFireBase(avouriteFeeds: List<FeedUI>, documentPath: String) {
		repository.updateFavouritesFeedsFireBase(avouriteFeeds, documentPath)
	}

	suspend fun deleteTable() {
		repository.deleteTable()
	}
}