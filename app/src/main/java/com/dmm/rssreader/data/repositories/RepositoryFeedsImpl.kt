package com.dmm.rssreader.data.repositories

import android.util.Log
import com.dmm.rssreader.data.network.apis.*
import com.dmm.rssreader.data.persistence.FeedsDao
import com.dmm.rssreader.domain.model.FeedUI
import com.dmm.rssreader.domain.repositories.RepositoryFeeds
import com.dmm.rssreader.utils.Constants
import com.dmm.rssreader.utils.FeedParser
import com.dmm.rssreader.utils.Resource
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.toList
import retrofit2.Response
import javax.inject.Inject

class RepositoryFeedsImpl @Inject constructor(
	private val feedsDao: FeedsDao,
	private val docRef: CollectionReference,
	private val serviceAndroidBlogs: ServiceAndroidBlogs,
	private val serviceAndroidDevelopers: ServiceAndroidDevelopers,
	private val serviceDanLew: ServiceDanLew,
	private val serviceDevsMedium: ServiceDevsMedium,
	private val serviceKotlinWeekly: ServiceKotlinWeekly,
) : RepositoryFeeds {

	override suspend fun fetchFeeds(source: String): Resource<List<FeedUI>?> {
		var result = feedsDao.getFeedsList(source)
		if(result.isEmpty()) {
			when(source) {
				Constants.SOURCE_ANDROID_BLOGS -> result = handleResponse(serviceAndroidBlogs.fetchData(), source)
				Constants.SOURCE_ANDROID_MEDIUM -> result = handleResponse(serviceDevsMedium.fetchData(), source)
				Constants.SOURCE_DEVELOPER_CO -> result = handleResponse(serviceAndroidDevelopers.fetchData(), source)
				Constants.SOURCE_DANLEW_BLOG -> result = handleResponse(serviceDanLew.fetchData(), source)
				Constants.SOURCE_KOTLIN_WEEKLY -> result = handleResponse(serviceKotlinWeekly.fetchData(), source)
			}
			saveDataLocal(result)
		}
		val favouriteFeeds = feedsDao.getFavouriteFeed()
		result.forEach {
			it.favourite = favouriteFeeds.contains(it)
		}
		return Resource.Success(result)
	}

	suspend fun <T> Flow<List<T>>.flattenToList() =
		flatMapConcat { it.asFlow() }.toList()

	override suspend fun saveDataLocal(feedUI: List<FeedUI>) {
		feedsDao.insertFeeds(feedUI)
	}

	override fun getFavouriteFeeds(): Flow<List<FeedUI>> {
		return feedsDao.getFavouriteFeeds()
	}

	override fun updateFavouritesFeedsFireBase(favouriteFeeds: List<FeedUI>, documentPath: String) {
		docRef.document(documentPath).update(mapOf(
			"favouritesFeeds" to favouriteFeeds
		))
	}

	override suspend fun saveFavouriteFeed(feedUI: FeedUI) {
		feedsDao.saveFavouriteFeed(feedUI)
	}

	override suspend fun updateFeed(favorite: Boolean, title: String) {
		feedsDao.updateFeed(favorite, title)
	}

	override suspend fun deleteTable() {
		feedsDao.deleteTable()
	}

	override suspend fun deleteFeeds(sourceFeed: String) {
		feedsDao.deleteFeedsByFeedSource(sourceFeed)
	}

	private fun handleResponse(response: Response<String>, source: String): List<FeedUI> {
		var result: List<FeedUI> = listOf()
		if(response!!.isSuccessful) {
			result = FeedParser().parse(response.body()!!, source)
		}
		return result
	}
}