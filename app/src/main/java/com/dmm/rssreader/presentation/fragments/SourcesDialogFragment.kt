package com.dmm.rssreader.presentation.fragments

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
}