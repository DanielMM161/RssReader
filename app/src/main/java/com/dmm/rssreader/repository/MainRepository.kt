package com.dmm.rssreader.repository

import com.dmm.rssreader.model.FeedUI
import com.dmm.rssreader.model.UserSettings
import com.dmm.rssreader.network.RssClient
import com.dmm.rssreader.persistence.FeedsDao
import com.dmm.rssreader.persistence.UserSettingsDao
import com.dmm.rssreader.utils.Utils
import com.dmm.rssreader.utils.Resource
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(
	private val rssClient: RssClient,
	private val userSettingsDao: UserSettingsDao,
	private val feedsDao: FeedsDao
) {

	private var developerFeedsResponse: MutableList<FeedUI>? = null

	//Network
	suspend fun fetchDeveloperApple() = handleResponse(rssClient.fetchDeveloperApple())
	suspend fun fetchDeveloperAndroidNews() = handleResponse(rssClient.fetchDeveloperAndroidNews())
	suspend fun fetchDeveloperAndroidBlogs() = handleResponse(rssClient.fetchDeveloperAndroidBlogs())
	//DB
	suspend fun insertUserSettings(userSettings: UserSettings) = userSettingsDao.insertUserSettings(userSettings)
	suspend fun getUserSettings(): UserSettings = userSettingsDao.getUserSettings()
	suspend fun insertFeed(feedUI: FeedUI) = feedsDao.insertFeed(feedUI)
	fun getFeedList() = feedsDao.getFeedList()
	suspend fun deleteFeed(feedUI: FeedUI) = feedsDao.deleteFeed(feedUI)


	private fun <T> handleResponse(response: Response<T>) : Resource<List<FeedUI>?> {
		if(response.isSuccessful) {
			response.body().let { result ->
				val data: List<FeedUI> = Utils.MapResponse((result))
				if(developerFeedsResponse == null) {
					developerFeedsResponse = data.toMutableList()
				} else {
					developerFeedsResponse?.addAll(data)
				}
				return Resource.Success(developerFeedsResponse)
			}
		}
		return Resource.Error(response.message())
	}

}