package com.dmm.rssreader.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmm.rssreader.model.Feed
import com.dmm.rssreader.model.FeedUI
import com.dmm.rssreader.model.UserSettings
import com.dmm.rssreader.repository.MainRepository
import com.dmm.rssreader.utils.Constants
import com.dmm.rssreader.utils.Constants.DEVELOPER_ANDROID_BLOG
import com.dmm.rssreader.utils.Constants.DEVELOPER_APPEL
import com.dmm.rssreader.utils.Constants.FEED_ANDROID_BLOGS
import com.dmm.rssreader.utils.Constants.FEED_ANDROID_MEDIUM
import com.dmm.rssreader.utils.Constants.FEED_APPLE_NEWS
import com.dmm.rssreader.utils.Constants.MEDIUM_ANDROID
import com.dmm.rssreader.utils.Constants.SOURCE_APPLE
import com.dmm.rssreader.utils.Constants.SOURCE_BLOGS
import com.dmm.rssreader.utils.Constants.THEME_AUTO
import com.dmm.rssreader.utils.HostSelectionInterceptor
import com.dmm.rssreader.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	private val mainRepository: MainRepository,
	private val hostSelectionInterceptor: HostSelectionInterceptor
) : ViewModel() {

	init {
		viewModelScope.async {
			getUserSettings().await().let {
				fetchFeedsDeveloper()
			}
		}
	}

	private var _userSettings = MutableStateFlow(UserSettings())
	val userSettings = _userSettings.asStateFlow()

	private var _developerFeeds = MutableStateFlow<Resource<List<FeedUI>?>>(Resource.Loading())
	val developerFeeds = _developerFeeds.asStateFlow()

	lateinit var feedSelected: FeedUI

	fun fetchFeedsDeveloper() = viewModelScope.launch {
		if (mainRepository.feedsResponse == null) {
			_developerFeeds.value = Resource.Loading()
			val userSettings = userSettings.first()
			var data: Resource<List<FeedUI>?> = Resource.Loading()

			userSettings.feeds.forEach { it ->
				when (it) {
					FEED_ANDROID_BLOGS -> {
						setBaseUrl(DEVELOPER_ANDROID_BLOG)
						data = mainRepository.fetchDeveloperAndroidBlogs()
					}
					FEED_APPLE_NEWS -> {
						setBaseUrl(DEVELOPER_APPEL)
						data = mainRepository.fetchDeveloperApple()
					}
				}
			}
			setDeveloperFeeds(data)
		}
	}

	fun setDeveloperFeeds(data: Resource<List<FeedUI>?>) = viewModelScope.launch {
		if (data.data != null) {
			_developerFeeds.value = sortedFeed(data.data)
		} else {
			_developerFeeds.value = Resource.Success(listOf<FeedUI>())
		}
	}

	fun setBaseUrl(baseUrl: String) {
		hostSelectionInterceptor.setHostBaseUrl(baseUrl)
	}

	fun getUserSettings() = viewModelScope.async {
		var userSettings = mainRepository.getUserSettings()
		if (userSettings == null) {
			userSettings = UserSettings(
				theme = THEME_AUTO,
				feeds = mutableListOf(FEED_ANDROID_MEDIUM, FEED_ANDROID_BLOGS, FEED_APPLE_NEWS)
			)
		}
		_userSettings.value = userSettings
	}

	fun setTheme(theme: String) = viewModelScope.launch {
		val userSetting = userSettings.first()
		userSetting.theme = theme
		mainRepository.insertUserSettings(userSetting)
	}

	fun setFeed(feedName: String) = viewModelScope.launch {
		val userSetting = userSettings.first()
		if (userSetting.feeds.contains(feedName)) {
			userSetting.feeds.remove(feedName)
		} else {
			userSetting.feeds.add(feedName)
		}
		mainRepository.insertUserSettings(userSetting)
	}

	fun insertFeed(feedUI: FeedUI) = viewModelScope.launch {
		mainRepository.insertFeed(feedUI)
	}

	fun getFeedList() = mainRepository.getFeedList()

	fun resetResponse() {
		mainRepository.resetResponse()
	}

	fun sortedFeed(feeds: List<FeedUI>?): Resource<List<FeedUI>?> {
		return Resource.Success(feeds!!.sortedByDescending { it ->
			LocalDate.parse(it.published, DateTimeFormatter.ofPattern(Constants.DATE_PATTERN_OUTPUT))
		})
	}
}