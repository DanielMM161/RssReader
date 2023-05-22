package com.dmm.beerss.domain.usecase

import com.dmm.beerss.domain.model.FeedUI
import com.dmm.beerss.domain.model.Source
import com.dmm.beerss.domain.repositories.RepositoryFeeds
import com.dmm.beerss.utils.Resource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FeedsUseCase @Inject constructor(
	private val repository: RepositoryFeeds
) {

	suspend fun fetchFeeds(baseUrl: String, route: String, sourceTitle: String): Resource<List<FeedUI>?> = GlobalScope.async {
		return@async repository.fetchFeeds(baseUrl, route, sourceTitle)
	}.await()

	fun getFavouriteFeeds(): Flow<List<FeedUI>> {
		return repository.getFavouriteFeeds()
	}

	suspend fun updateFavouriteFeed(favorite: Boolean,title: String) {
		repository.updateFeed(favorite, title)
	}

	fun updateFavouritesFeedsFireBase(favouriteFeeds: List<FeedUI>, documentPath: String) {
		repository.updateFavouritesFeedsFireBase(favouriteFeeds, documentPath)
	}

	suspend fun deleteTable() {
		repository.deleteTable()
	}

	suspend fun deleteFeeds(sourceFeed: String) {
		repository.deleteFeeds(sourceFeed)
	}
}