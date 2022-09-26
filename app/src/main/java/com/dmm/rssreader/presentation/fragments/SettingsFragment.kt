package com.dmm.rssreader.presentation.fragments

import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.children
import androidx.core.view.forEach
import com.dmm.rssreader.R
import com.dmm.rssreader.databinding.SettingsFragmentBinding
import com.dmm.rssreader.utils.Constants.FEED_ANDROID_BLOGS
import com.dmm.rssreader.utils.Constants.FEED_ANDROID_MEDIUM
import com.dmm.rssreader.utils.Constants.FEED_APPLE_NEWS
import com.dmm.rssreader.utils.Constants.THEME_DAY
import com.dmm.rssreader.utils.Constants.THEME_NIGHT
import com.dmm.rssreader.utils.Resource

class SettingsFragment : BaseFragment<SettingsFragmentBinding>(
  SettingsFragmentBinding::inflate
) {

  override fun setupUI() {
    super.setupUI()

//		autoSelectedTheme()
//		autoSelectedFeed()
//		selectedFeeds()
//    selectedTheme()
    themesOption()
  }

//  private fun selectedFeeds() {
//    binding.layoutFeeds.children.forEach { view ->
//      val switch = (view as Switch)
//      switch.setOnCheckedChangeListener { compoundButton, isChecked ->
//        when (compoundButton.text) {
//          getString(R.string.android_developer_blogs) -> {
//            setFeed(FEED_ANDROID_BLOGS)
//          }
//          getString(R.string.android_developer_medium) -> {
//            setFeed(FEED_ANDROID_MEDIUM)
//          }
//          getString(R.string.apple_developers_news) -> {
//            setFeed(FEED_APPLE_NEWS)
//          }
//        }
//
//      }
//    }
//  }
//
//
//  private fun autoSelectedFeed() {
//    viewModel.userProfile.feeds.forEach { feed ->
//      when (feed) {
//        FEED_ANDROID_BLOGS -> {
//          binding.switchBlogs.isChecked = true
//        }
//        FEED_APPLE_NEWS -> {
//          binding.switchApple.isChecked = true
//        }
//      }
//    }
//  }
//
//  private fun setFeed(feed: String) {
//    viewModel.setFeed(feed).observe(this) {
//      when(it) {
//        is Resource.Success -> {
//          if(!it.data!!) {
//            // GIVE FEEDBACK TO USER
//          }
//        }
//      }
//    }
//  }

  private fun feedSourcesOption() {
    binding.userFeedsLayout.setOnClickListener {

    }
  }

  private fun themesOption() {
    binding.userThemeLayout.setOnClickListener {
      val themeDialogFragment = ThemeDialogFragment()
      themeDialogFragment.show(parentFragmentManager, themeDialogFragment.tag)
    }
  }
}