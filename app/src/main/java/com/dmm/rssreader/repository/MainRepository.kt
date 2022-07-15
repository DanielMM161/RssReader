package com.dmm.rssreader.repository

import android.util.Log
import com.dmm.rssreader.model.Feed
import com.dmm.rssreader.model.UserSettings
import com.dmm.rssreader.model.feedandroidblogs.FeedAndroidBlogs
import com.dmm.rssreader.network.RssClient
import com.dmm.rssreader.persistence.UserSettingsDao
import com.dmm.rssreader.utils.Resource
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(
	private val rssClient: RssClient,
	private val userSettingsDao: UserSettingsDao
) {


//	suspend fun fetchDeveloperApple() = handleResponse(rssClient.fetchDeveloperApple())
//	suspend fun fetchDeveloperAndroidNews() = handleResponse(rssClient.fetchDeveloperAndroidNews())
	suspend fun fetchDeveloperAndroidBlogs() = handleResponse(rssClient.fetchDeveloperAndroidBlogs())
	suspend fun setUserSettings(userSettings: UserSettings) = userSettingsDao.insertUserSettings(userSettings)

	private fun handleResponse(response: Response<FeedAndroidBlogs>) : Resource<FeedAndroidBlogs?> {
		if(response.isSuccessful) {
			response.body().let { result ->
				return Resource.Success(result)
			}
		}
		return Resource.Error(response.message())
	}

}