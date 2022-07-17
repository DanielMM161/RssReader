package com.dmm.rssreader.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmm.rssreader.model.Feed
import com.dmm.rssreader.model.UserSettings
import com.dmm.rssreader.model.feedandroidblogs.FeedAndroidBlogs
import com.dmm.rssreader.repository.MainRepository
import com.dmm.rssreader.utils.HostSelectionInterceptor
import com.dmm.rssreader.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	private val mainRepository: MainRepository,
	private val hostSelectionInterceptor: HostSelectionInterceptor
) : ViewModel() {

	init {
		getUserSettings()
	}

	private var _userSettings = MutableStateFlow(UserSettings())
	val userSettings = _userSettings.asStateFlow()

	private var _metalInjectionFeed = MutableStateFlow<Resource<FeedAndroidBlogs?>>(Resource.Loading())
	val metalInjectionFeed = _metalInjectionFeed.asStateFlow()


	fun getAppelDeveloper()	= viewModelScope.launch {
		Log.e("getMetalInjection", " ------ getMetalInjection --------")
		_metalInjectionFeed.value = mainRepository.fetchDeveloperAndroidBlogs()
	}

	fun setBaseUrl(baseUrl: String) {
		hostSelectionInterceptor.setHostBaseUrl(baseUrl)
	}

	fun getUserSettings() = viewModelScope.launch {
		var userSettings = mainRepository.getUserSettings()
		if(userSettings == null) userSettings = UserSettings()
		_userSettings.value = userSettings
	}

	fun setTheme(theme: String) = viewModelScope.launch {
		val userSetting = UserSettings(theme = theme)
		mainRepository.setUserSettings(userSetting)
	}

	fun setFeed(feedName: String) = viewModelScope.launch {
		val userSetting = userSettings.first()
		if(userSetting.feeds.contains(feedName)) {
			userSetting.feeds.remove(feedName)
		} else {
			userSetting.feeds.add(feedName)
		}
		mainRepository.setUserSettings(userSetting)
	}

}