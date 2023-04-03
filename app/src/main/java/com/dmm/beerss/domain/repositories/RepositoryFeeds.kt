package com.dmm.beerss.domain.repositories

import com.dmm.beerss.domain.model.FeedUI
import com.dmm.beerss.utils.Resource
import kotlinx.coroutines.flow.Flow

interface RepositoryFeeds {
	suspend fun fetchFeeds(source: String): Resource<List<FeedUI>?>
	suspend fun saveDataLocal(feedUI: List<FeedUI>)
	suspend fun updateFeed(favorite: Boolean, title: String)
	suspend fun deleteTable()
	suspend fun deleteFeeds(sourceFeed: String)
	fun getFavouriteFeeds(): Flow<List<FeedUI>>
	fun updateFavouritesFeedsFireBase(favouriteFeeds: List<FeedUI>, documentPath: String)
}