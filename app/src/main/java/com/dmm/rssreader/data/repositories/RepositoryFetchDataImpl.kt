package com.dmm.rssreader.data.repositories

import androidx.lifecycle.MutableLiveData
import com.dmm.rssreader.data.network.apis.*
import com.dmm.rssreader.data.persistence.FeedsDao
import com.dmm.rssreader.domain.model.FeedUI
import com.dmm.rssreader.domain.repositories.RepositoryFetchData
import com.dmm.rssreader.utils.Constants.SOURCE_ANDROID_BLOGS
import com.dmm.rssreader.utils.Constants.SOURCE_ANDROID_MEDIUM
import com.dmm.rssreader.utils.Constants.SOURCE_DANLEW_BLOG
import com.dmm.rssreader.utils.Constants.SOURCE_DEVELOPER_CO
import com.dmm.rssreader.utils.Constants.SOURCE_KOTLIN_WEEKLY
import com.dmm.rssreader.utils.FeedParser
import com.dmm.rssreader.utils.Resource
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import retrofit2.Response
import javax.inject.Inject

class RepositoryFetchDataImpl @Inject constructor(
	private val serviceAndroidBlogs: ServiceAndroidBlogs,
	private val serviceAndroidDevelopers: ServiceAndroidDevelopers,
	private val serviceDanLew: ServiceDanLew,
	private val serviceDevsMedium: ServiceDevsMedium,
	private val serviceKotlinWeekly: ServiceKotlinWeekly,
	private val feedsDao: FeedsDao
) : RepositoryFetchData {

	override suspend fun fetchDataNetwork(source: String): Resource<List<FeedUI>?> {
		var result = feedsDao.getFeedsList(source)
		if(result.isEmpty()) {
			when(source) {
				SOURCE_ANDROID_BLOGS  -> result = handleResponse(serviceAndroidBlogs.fetchData())
				SOURCE_ANDROID_MEDIUM -> result = handleResponse(serviceDevsMedium.fetchData())
				SOURCE_DEVELOPER_CO   -> result = handleResponse(serviceAndroidDevelopers.fetchData())
				SOURCE_DANLEW_BLOG    -> result = handleResponse(serviceDanLew.fetchData())
				SOURCE_KOTLIN_WEEKLY  -> result = handleResponse(serviceKotlinWeekly.fetchData())
			}
			saveDataLocal(result)
		}
		return Resource.Success(result)
	}

	override suspend fun saveDataLocal(feedUI: List<FeedUI>) {
		feedsDao.insertFeeds(feedUI)
	}

	private fun handleResponse(response: Response<String>): List<FeedUI> {
		var result: List<FeedUI> = listOf()
		if(response!!.isSuccessful) {
			result = FeedParser().parse(response.body()!!)
		}
		return result
	}
}