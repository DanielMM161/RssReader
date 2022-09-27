package com.dmm.rssreader.presentation.fragments

import android.widget.Switch
import com.dmm.rssreader.R
import com.dmm.rssreader.data.local.ContentResources
import com.dmm.rssreader.data.local.ContentResources.contentResources
import com.dmm.rssreader.databinding.SourcesDialogFragmentBinding
import com.dmm.rssreader.presentation.adapters.SourcesAdapter
import com.dmm.rssreader.utils.Resource

class SourcesDialogFragment : BaseBottomSheetDialogFragment<SourcesDialogFragmentBinding>(
	SourcesDialogFragmentBinding::inflate
) {

	override fun setupUI() {
		super.setupUI()
		binding.listSources.apply {
			adapter = SourcesAdapter(contentResources, viewModel.userProfile.feeds) {
				setFeed(it)
			}
		}
	}

  private fun setFeed(feed: String) {
    viewModel.setFeed(feed).observe(this) {
      when(it) {
        is Resource.Success -> {
          if(!it.data!!) {
            // GIVE FEEDBACK TO USER
          }
        }
      }
    }
  }

//	  private fun selectedFeeds() {
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
}