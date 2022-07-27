package com.dmm.rssreader.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmm.rssreader.model.Feed
import com.dmm.rssreader.model.FeedUI
import com.dmm.rssreader.model.UserSettings
import com.dmm.rssreader.repository.MainRepository
import com.dmm.rssreader.utils.Constants.DEVELOPER_ANDROID_BLOG
import com.dmm.rssreader.utils.Constants.DEVELOPER_APPEL
import com.dmm.rssreader.utils.Constants.FEED_ANDROID_BLOGS
import com.dmm.rssreader.utils.Constants.FEED_APPLE_NEWS
import com.dmm.rssreader.utils.Constants.SOURCE_APPLE
import com.dmm.rssreader.utils.Constants.SOURCE_BLOGS
import com.dmm.rssreader.utils.HostSelectionInterceptor
import com.dmm.rssreader.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
		val userSettings = userSettings.first()
		userSettings.feeds.forEach { it ->
			_developerFeeds.value = Resource.Loading()
				when(it) {
					FEED_ANDROID_BLOGS -> {
						setBaseUrl(DEVELOPER_ANDROID_BLOG)
						setDeveloperFeeds( mainRepository.fetchDeveloperAndroidBlogs())
					}
					FEED_APPLE_NEWS -> {
						setBaseUrl(DEVELOPER_APPEL)
						setDeveloperFeeds( mainRepository.fetchDeveloperApple())
					}
				}
		}
	}

	fun setDeveloperFeeds(data: Resource<List<FeedUI>?>) = viewModelScope.launch {
		_developerFeeds.value = data
	}

	fun setBaseUrl(baseUrl: String) {
		hostSelectionInterceptor.setHostBaseUrl(baseUrl)
	}

	fun getUserSettings() = viewModelScope.async {
		var userSettings = mainRepository.getUserSettings()
		if(userSettings == null) userSettings = UserSettings()
		_userSettings.value = userSettings
	}

	fun setTheme(theme: String) = viewModelScope.launch {
		val userSetting = userSettings.first()
		userSetting.theme = theme
		mainRepository.insertUserSettings(userSetting)
	}

	fun setFeed(feedName: String) = viewModelScope.launch {
		val userSetting = userSettings.first()
		if(userSetting.feeds.contains(feedName)) {
			userSetting.feeds.remove(feedName)
		} else {
			userSetting.feeds.add(feedName)
		}
		mainRepository.insertUserSettings(userSetting)
	}

	fun insertFeed(feedUI: FeedUI) = viewModelScope.launch {
		mainRepository.insertFeed(feedUI)
	}

	fun deleteFeedUI(feedUI: FeedUI) = viewModelScope.launch {
		mainRepository.deleteFeed(feedUI)
	}

	fun getFeedList() = mainRepository.getFeedList()
}