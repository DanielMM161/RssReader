package com.dmm.rssreader.ui.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmm.rssreader.MainApplication
import com.dmm.rssreader.R
import com.dmm.rssreader.model.FeedUI
import com.dmm.rssreader.model.UserSettings
import com.dmm.rssreader.repository.MainRepository
import com.dmm.rssreader.utils.Constants
import com.dmm.rssreader.utils.Constants.DEVELOPER_ANDROID_BLOG
import com.dmm.rssreader.utils.Constants.DEVELOPER_APPEL
import com.dmm.rssreader.utils.Constants.FEED_ANDROID_BLOGS
import com.dmm.rssreader.utils.Constants.FEED_ANDROID_MEDIUM
import com.dmm.rssreader.utils.Constants.FEED_APPLE_NEWS
import com.dmm.rssreader.utils.Constants.THEME_DAY
import com.dmm.rssreader.utils.Constants.THEME_NIGHT
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
	app: Application,
	private val mainRepository: MainRepository,
	private val hostSelectionInterceptor: HostSelectionInterceptor
) : AndroidViewModel(app) {

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
		if(hasInternetConnection()) {
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
		} else {
			 _developerFeeds.value = Resource.ErrorCaught(resId = R.string.offline)
		}
	}

	fun setDeveloperFeeds(data: Resource<List<FeedUI>?>) = viewModelScope.launch {
		if (data.data != null) {
			_developerFeeds.value = sortedFeed(data.data.filter { it -> !it.description!!.isEmpty() }.distinct())
		} else {
			_developerFeeds.value = Resource.Success(listOf())
		}
	}

	fun setBaseUrl(baseUrl: String) {
		hostSelectionInterceptor.setHostBaseUrl(baseUrl)
	}

	fun getUserSettings() = viewModelScope.async {
		var userSettings = mainRepository.getUserSettings()
		if (userSettings == null) {
			userSettings = UserSettings(
				theme = THEME_DAY,
				feeds = mutableListOf(FEED_ANDROID_MEDIUM, FEED_ANDROID_BLOGS, FEED_APPLE_NEWS)
			)
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
		}
		autoSelectedTheme(userSettings)
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

	fun autoSelectedTheme(userSettings: UserSettings) {
		when (userSettings.theme) {
			THEME_DAY -> {
				AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
			}
			THEME_NIGHT -> {
				AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
			}
		}
	}

	fun hasInternetConnection(): Boolean {
		val connectivityManager = getApplication<MainApplication>().getSystemService(
			Context.CONNECTIVITY_SERVICE
		) as ConnectivityManager
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			val activeNetwork = connectivityManager.activeNetwork ?: return false
			val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
			return when {
				capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
				capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
				capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
				else -> return false
			}
		} else {
			connectivityManager.activeNetworkInfo?.run {
				return when(type) {
					ConnectivityManager.TYPE_WIFI -> return true
					ConnectivityManager.TYPE_MOBILE -> return true
					ConnectivityManager.TYPE_ETHERNET -> return true
					else -> return false
				}
			}
		}
		return false
	}
}