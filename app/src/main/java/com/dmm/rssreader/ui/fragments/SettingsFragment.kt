package com.dmm.rssreader.ui.fragments
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dmm.rssreader.R
import com.dmm.rssreader.databinding.SettingsFragmentBinding
import com.dmm.rssreader.utils.Constants.FEED_ANDROID_BLOGS
import com.dmm.rssreader.utils.Constants.FEED_ANDROID_NEWS
import com.dmm.rssreader.utils.Constants.FEED_APPLE_NEWS
import com.dmm.rssreader.utils.Constants.THEME_AUTO
import com.dmm.rssreader.utils.Constants.THEME_DAY
import com.dmm.rssreader.utils.Constants.THEME_NIGHT
import kotlinx.coroutines.launch

class SettingsFragment : BaseFragment<SettingsFragmentBinding>(
	SettingsFragmentBinding::inflate
) {

	override fun setupUI() {
		super.setupUI()
		selectedTheme()
		selectedFeeds()
		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				launch {
					autoSelectedTheme()
				}
				launch {
					autoSelectedFeed()
				}
			}
		}
	}

	fun selectedFeeds() {
		binding.layoutFeeds.children.forEach { view ->
			val switch = (view as Switch)
			switch.setOnCheckedChangeListener { compoundButton, isChecked ->
				when(compoundButton.text) {
					getString(R.string.android_developer_blogs) -> { viewModel.setFeed(FEED_ANDROID_BLOGS) }
					getString(R.string.android_developer_news) -> { viewModel.setFeed(FEED_ANDROID_NEWS) }
					getString(R.string.apple_developers_news) -> { viewModel.setFeed(FEED_APPLE_NEWS) }
				}

			}
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
			if(it != null) {
				when(it.theme) {
					THEME_DAY -> { selectedView(binding.layoutDay, true) }
					THEME_NIGHT -> { selectedView(binding.layoutNight, true) }
					THEME_AUTO -> {	selectedView(binding.layoutAuto, true) }
				}
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
		(view as ViewGroup).forEach { view ->
				view.isSelected = selected
		}
	}


}