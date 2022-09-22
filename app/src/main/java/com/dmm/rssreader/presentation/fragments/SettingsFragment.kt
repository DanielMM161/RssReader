package com.dmm.rssreader.presentation.fragments

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dmm.rssreader.R
import com.dmm.rssreader.databinding.SettingsFragmentBinding
import com.dmm.rssreader.utils.Constants.FEED_ANDROID_BLOGS
import com.dmm.rssreader.utils.Constants.FEED_ANDROID_MEDIUM
import com.dmm.rssreader.utils.Constants.FEED_APPLE_NEWS
import com.dmm.rssreader.utils.Constants.THEME_DAY
import com.dmm.rssreader.utils.Constants.THEME_NIGHT
import kotlinx.coroutines.launch

class SettingsFragment : BaseFragment<SettingsFragmentBinding>(
  SettingsFragmentBinding::inflate
) {

  override fun setupUI() {
    super.setupUI()

		autoSelectedTheme()
		autoSelectedFeed()
		selectedFeeds()
    selectedTheme()

    Log.e("Soy setting fragment", "En SetupUI")
  }

  fun selectedFeeds() {
    binding.layoutFeeds.children.forEach { view ->
      val switch = (view as Switch)
      switch.setOnCheckedChangeListener { compoundButton, isChecked ->
        when (compoundButton.text) {
          getString(R.string.android_developer_blogs) -> {
            viewModel.setFeed(FEED_ANDROID_BLOGS)
          }
          getString(R.string.android_developer_medium) -> {
            viewModel.setFeed(FEED_ANDROID_MEDIUM)
          }
          getString(R.string.apple_developers_news) -> {
            viewModel.setFeed(FEED_APPLE_NEWS)
          }
        }

      }
    }
  }

  fun selectedTheme() {
    binding.layoutDay.setOnClickListener {
      selectedView(binding.layoutDay, true)
      selectedView(binding.layoutNight, false)
      viewModel.setTheme(THEME_DAY)
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    binding.layoutNight.setOnClickListener {
      selectedView(binding.layoutNight, true)
      selectedView(binding.layoutDay, false)
      viewModel.setTheme(THEME_NIGHT)
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
  }

	fun autoSelectedFeed() {
    viewModel.userProfile.feeds.forEach { feed ->
      when (feed) {
        FEED_ANDROID_BLOGS -> {
          binding.switchBlogs.isChecked = true
        }
        FEED_APPLE_NEWS -> {
          binding.switchApple.isChecked = true
        }
      }
    }
  }

	fun autoSelectedTheme() {
    when (viewModel.userProfile.theme) {
			THEME_DAY -> {
				selectedView(binding.layoutDay, true)
			}
			THEME_NIGHT -> {
				selectedView(binding.layoutNight, true)
			}
		}
  }

  fun selectedView(view: View, selected: Boolean) {
    (view as ViewGroup).forEach { view ->
      view.isSelected = selected
    }
  }


}