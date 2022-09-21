package com.dmm.rssreader.presentation.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dmm.rssreader.MainApplication
import com.dmm.rssreader.R
import com.dmm.rssreader.domain.model.Feed
import com.dmm.rssreader.domain.model.FeedUI
import com.dmm.rssreader.domain.model.UserProfile
import com.dmm.rssreader.domain.usecase.FetchFeedAndroidBlogsUseCase
import com.dmm.rssreader.domain.usecase.FetchFeedAppleUseCase
import com.dmm.rssreader.repository.MainRepository
import com.dmm.rssreader.utils.Constants
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
	private val fetchFeedAndroidBlogs: FetchFeedAndroidBlogsUseCase,
	private val fetchFeedAppleUseCase: FetchFeedAppleUseCase
) : AndroidViewModel(app) {

	init {
		viewModelScope.async {
			getUserSettings().await().let {
				//fetchFeedsDeveloper()
			}
		}
	}

	private var _userProfile = MutableStateFlow(UserProfile())
	val userSettings = _userProfile.asStateFlow()

	private var _developerFeeds = MutableStateFlow<Resource<List<FeedUI>?>>(Resource.Loading())
	val developerFeeds = _developerFeeds.asStateFlow()

	lateinit var feedSelected: FeedUI

	fun fetchFeedsDeveloper() = viewModelScope.launch {
		if(hasInternetConnection()) {
			_developerFeeds.value = Resource.Loading()
			val userSettings = userSettings.first()
			var listFeed: MutableList<FeedUI> = mutableListOf()

			userSettings.feeds.forEach { it ->
				when (it) {
					FEED_ANDROID_BLOGS -> {
						fetchFeedAndroidBlogs().await().data?.forEach { feedUI ->
							listFeed.add(feedUI)
						}
					}
					FEED_APPLE_NEWS -> {
						fetchFeedAppleUseCase().await().data?.forEach { feedUI ->
							listFeed.add(feedUI)
						}
					}
				}
			}
			setDeveloperFeeds(listFeed)
		} else {
			 _developerFeeds.value = Resource.ErrorCaught(resId = R.string.offline)
		}
	}

	fun setDeveloperFeeds(feedUIList: List<FeedUI>) = viewModelScope.launch {
		if (feedUIList != null) {
			_developerFeeds.value = sortedFeed(feedUIList.filter { it -> !it.description!!.isEmpty() }.distinct())
		} else {
			_developerFeeds.value = Resource.Success(listOf())
		}
	}

	fun getUserSettings() = viewModelScope.async {
//		var userSettings = mainRepository.getUser()
//		if (userSettings == null) {
//			userSettings = UserProfile(
//				theme = THEME_DAY,
//				feeds = mutableListOf(FEED_ANDROID_MEDIUM, FEED_ANDROID_BLOGS, FEED_APPLE_NEWS)
//			)
//			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//		}
//		autoSelectedTheme(userSettings)
//		_userProfile.value = userSettings
	}

	fun setTheme(theme: String) = viewModelScope.launch {
		val userSetting = userSettings.first()
		userSetting.theme = theme
		// SAVE USER HERE
	}

	fun setFeed(feedName: String) = viewModelScope.launch {
		val userSetting = userSettings.first()
		if (userSetting.feeds.contains(feedName)) {
			userSetting.feeds.remove(feedName)
		} else {
			userSetting.feeds.add(feedName)
		}
		// SAVE USER HERE
	}

	fun insertFeed(feedUI: FeedUI) = viewModelScope.launch {
		// INSERT FEED HERE
	}

	fun getFeedList() {
		// GET FEEDS HERE
	}

	fun resetResponse() {
		// RESET RESPONSE
	}

	fun sortedFeed(feeds: List<FeedUI>?): Resource<List<FeedUI>?> {
		return Resource.Success(feeds!!.sortedByDescending { it ->
			LocalDate.parse(it.published, DateTimeFormatter.ofPattern(Constants.DATE_PATTERN_OUTPUT))
		})
	}

	fun autoSelectedTheme(userProfile: UserProfile) {
		when (userProfile.theme) {
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