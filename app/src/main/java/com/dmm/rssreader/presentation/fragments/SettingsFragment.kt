package com.dmm.rssreader.presentation.fragments

import com.dmm.rssreader.databinding.SettingsFragmentBinding

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
    feedSourcesOption()
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
      val sourcesDialogFragment = SourcesDialogFragment()
      sourcesDialogFragment.show(parentFragmentManager, sourcesDialogFragment.tag)
    }
  }

  private fun themesOption() {
    binding.userThemeLayout.setOnClickListener {
      val themeDialogFragment = ThemeDialogFragment()
      themeDialogFragment.show(parentFragmentManager, themeDialogFragment.tag)
    }
  }
}