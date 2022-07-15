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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	private val mainRepository: MainRepository,
	private val hostSelectionInterceptor: HostSelectionInterceptor
) : ViewModel() {

	init {

	}

	private var _userSettings: Flow<UserSettings> = mainRepository.getUserSettings()
	val userSettings = _userSettings

	private var _metalInjectionFeed = MutableStateFlow<Resource<FeedAndroidBlogs?>>(Resource.Loading())
	val metalInjectionFeed = _metalInjectionFeed.asStateFlow()


	fun getAppelDeveloper()	= viewModelScope.launch {
		Log.e("getMetalInjection", " ------ getMetalInjection --------")
		_metalInjectionFeed.value = mainRepository.fetchDeveloperAndroidBlogs()
	}

	fun setBaseUrl(baseUrl: String) {
		hostSelectionInterceptor.setHostBaseUrl(baseUrl)
	}

	fun setTheme(theme: String) = viewModelScope.launch {
		val userSetting = UserSettings(theme = theme, feeds = listOf(""))
		mainRepository.setUserSettings(userSetting)
	}

//	fun getUserSettings() = viewModelScope.launch {
//		_userSettings = mainRepository.getUserSettings()
//	}

}