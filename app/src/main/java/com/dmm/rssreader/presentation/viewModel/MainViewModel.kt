package com.dmm.rssreader.presentation.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dmm.rssreader.MainApplication
import com.dmm.rssreader.R
import com.dmm.rssreader.domain.model.FeedUI
import com.dmm.rssreader.domain.model.UserProfile
import com.dmm.rssreader.domain.usecase.*
import com.dmm.rssreader.utils.Constants
import com.dmm.rssreader.utils.Constants.THEME_DAY
import com.dmm.rssreader.utils.Constants.THEME_NIGHT
import com.dmm.rssreader.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	app: Application,
	private val feedsUseCase: FeedsUseCase,
	private val fireBaseUseCase: FireBaseUseCase,
	private val authUseCase: AuthUseCase,
) : AndroidViewModel(app) {

	lateinit var userProfile: UserProfile
	private var _developerFeeds = MutableStateFlow<Resource<List<FeedUI>?>>(Resource.Loading())
	val developerFeeds = _developerFeeds.asStateFlow()

	private var _favouritesFeeds = MutableStateFlow<List<FeedUI>>(mutableListOf())
	val favouritesFeeds = _favouritesFeeds.asStateFlow()

	var searchText: String = ""

	fun userProfileInitialized(): Boolean {
		return this::userProfile.isInitialized
	}

	fun fetchFeedsDeveloper() = viewModelScope.launch {
		if (hasInternetConnection()) {
			_developerFeeds.value = Resource.Loading()
			var listFeed: MutableList<FeedUI> = mutableListOf()

			userProfile.feeds.forEach { feed ->
				feedsUseCase.fetchFeeds(feed).data?.forEach { feedUI ->
					listFeed.add(feedUI)
				}
			}
			saveFavouriteFeedsInLocal(listFeed)
			setDeveloperFeeds(listFeed)
		} else {
			_developerFeeds.value = Resource.ErrorCaught(resId = R.string.offline)
		}
	}

	fun findFeed(text: String): List<FeedUI>? {
		return _developerFeeds.value.data?.filter {
			it.title.lowercase().contains(text.lowercase())
		}
	}

	private suspend fun saveFavouriteFeedsInLocal(listFeed: MutableList<FeedUI>) {
		userProfile.favouritesFeeds.forEach { favouriteFeed ->
			val feed = listFeed.find { it.title == favouriteFeed.title }
			if (feed != null) {
				feed.favourite = true
				feedsUseCase.updateFavouriteFeed(feed.favourite, feed.title)
			}
		}
	}

	private fun setDeveloperFeeds(feedUIList: List<FeedUI>?) {
		if (feedUIList != null) {
			_developerFeeds.value = sortedFeed(feedUIList.filter { it -> !it.description!!.isEmpty() }.distinct())
		} else {
			_developerFeeds.value = Resource.Success(listOf())
		}
	}

	fun setTheme(theme: String): MutableLiveData<Resource<Boolean>> {
		userProfile.theme = theme
		return fireBaseUseCase.saveUser(userProfile)
	}

	fun setFeed(feedName: String): MutableLiveData<Resource<Boolean>> {
		if (userProfile.feeds.contains(feedName)) {
			userProfile.feeds.remove(feedName)
		} else {
			userProfile.feeds.add(feedName)
		}
		return fireBaseUseCase.saveUser(userProfile)
	}

	fun saveFavouriteFeed(feedSelected: FeedUI) = viewModelScope.launch {
		updateFavouritesFeedsFireBase(feedSelected)
		feedSelected.favourite = !feedSelected.favourite
		feedsUseCase.updateFavouriteFeed(feedSelected.favourite, feedSelected.title)
		getFavouriteFeeds()
	}

	fun updateFavouritesFeedsFireBase(feedSelected: FeedUI) {
		if (userProfile.favouritesFeeds.contains(feedSelected)) {
			userProfile.favouritesFeeds.remove(feedSelected)
		} else {
			userProfile.favouritesFeeds.add(feedSelected.copy(favourite = true))
		}
		feedsUseCase.updateFavouritesFeedsFireBase(
			userProfile.favouritesFeeds,
			userProfile.email
		)
	}

	fun getFavouriteFeeds() = viewModelScope.launch {
		feedsUseCase.getFavouriteFeeds().collect{
			_favouritesFeeds.value = it
		}
	}

	private fun sortedFeed(feeds: List<FeedUI>?): Resource<List<FeedUI>?> {
		val dateEmptyList = feeds?.filter { it.published!!.isEmpty()  }
		val dateNoEmptyList = feeds?.filter { it.published!!.isNotEmpty()  }
		val sortedFeeds = dateNoEmptyList!!.sortedByDescending {
			LocalDate.parse(it.published, DateTimeFormatter.ofPattern(Constants.DATE_PATTERN_OUTPUT))
		}.toMutableList()
		dateEmptyList?.forEach {
			sortedFeeds.add(it)
		}
		return Resource.Success(sortedFeeds.toList())
	}

	fun autoSelectedTheme() {
		when (userProfile.theme) {
			THEME_DAY -> {
				AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
			}
			THEME_NIGHT -> {
				AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
			}
		}
	}

	fun signOut() {
		authUseCase.signOut()
	}

	fun deleteTable() = viewModelScope.launch {
		feedsUseCase.deleteTable()
	}

	fun deleteFeeds(sourceFeed: String) = viewModelScope.launch {
		feedsUseCase.deleteFeeds(sourceFeed)
	}

	private fun hasInternetConnection(): Boolean {
		val connectivityManager = getApplication<MainApplication>().getSystemService(
			Context.CONNECTIVITY_SERVICE
		) as ConnectivityManager
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
				return when (type) {
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