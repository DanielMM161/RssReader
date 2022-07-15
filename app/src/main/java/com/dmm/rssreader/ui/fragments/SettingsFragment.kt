package com.dmm.rssreader.ui.fragments

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.core.view.forEachIndexed
import androidx.lifecycle.lifecycleScope
import com.dmm.rssreader.databinding.SettingsFragmentBinding
import com.dmm.rssreader.utils.Constants.FEED_ANDROID_BLOGS
import com.dmm.rssreader.utils.Constants.FEED_ANDROID_NEWS
import com.dmm.rssreader.utils.Constants.FEED_APPLE_NEWS
import com.dmm.rssreader.utils.Constants.THEME_AUTO
import com.dmm.rssreader.utils.Constants.THEME_DAY
import com.dmm.rssreader.utils.Constants.THEME_NIGHT
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class SettingsFragment : BaseFragment<SettingsFragmentBinding>(
	SettingsFragmentBinding::inflate
) {

	override fun setupUI() {
		super.setupUI()
		selectedTheme()
		setFeeds()
		viewLifecycleOwner.lifecycleScope.launch {
			autoSelectedTheme()
			autoSelectedFeed()
		}

	}

	fun setFeeds() {
		binding.switchNews.setOnCheckedChangeListener { compoundButton, isChecked ->
			Log.e("SET FEEDS ---> ", "$isChecked")
		}
	}

	suspend fun autoSelectedFeed() {
		viewModel.userSettings.collect() {
			it.feeds.forEach { feed ->
				when(feed) {
					FEED_ANDROID_BLOGS -> { binding.switchBlogs.isChecked = true }
					FEED_ANDROID_NEWS -> { binding.switchNews.isChecked = true }
					FEED_APPLE_NEWS -> { binding.switchApple.isChecked = true }
				}
			}
		}
	}

	suspend fun autoSelectedTheme() {
		viewModel.userSettings.collect() {
			when(it.theme) {
				THEME_DAY -> { selectedView(binding.layoutDay, true) }
				THEME_NIGHT -> { selectedView(binding.layoutNight, true) }
				THEME_AUTO -> {	selectedView(binding.layoutAuto, true) }
			}
		}
	}

	fun selectedTheme() {
		binding.layoutDay.setOnClickListener {
			selectedView(binding.layoutDay, true)
			selectedView(binding.layoutNight, false)
			selectedView(binding.layoutAuto, false)
			viewModel.setTheme(THEME_DAY)
		}

		binding.layoutNight.setOnClickListener {
			selectedView(binding.layoutNight, true)
			selectedView(binding.layoutDay, false)
			selectedView(binding.layoutAuto, false)
			viewModel.setTheme(THEME_NIGHT)
		}

		binding.layoutAuto.setOnClickListener {
			selectedView(binding.layoutAuto, true)
			selectedView(binding.layoutDay, false)
			selectedView(binding.layoutNight, false)
			viewModel.setTheme(THEME_AUTO)
		}
	}

	fun selectedView(view: View, selected: Boolean) {
		(view as ViewGroup).forEachIndexed { index, view ->
				view.isSelected = selected
		}
	}


}