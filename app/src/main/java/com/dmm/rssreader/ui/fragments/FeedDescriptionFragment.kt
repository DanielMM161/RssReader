package com.dmm.rssreader.ui.fragments

import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dmm.rssreader.R
import com.dmm.rssreader.databinding.FeedDescriptionFragmentBinding
import com.dmm.rssreader.utils.ImageGetter
import kotlinx.coroutines.launch

class FeedDescriptionFragment : BaseFragment<FeedDescriptionFragmentBinding>(
	FeedDescriptionFragmentBinding::inflate
) {

	override fun setupUI() {
		super.setupUI()
		viewLifecycleOwner.lifecycleScope.launch {
			viewModel.getFeedList().collect {
				it.forEach { feed ->
					if (feed.title == viewModel.feedSelected.title) {
						viewModel.feedSelected.saved = feed.saved
					}
				}
			}
		}
		viewModel.feedSelected?.description.let {
			if (it != null) {
				displayHtml(it)
			}
		}
	}

	override fun setHasOptionsMenu() {
		super.setHasOptionsMenu()
		setHasOptionsMenu(true)
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		if (viewModel.feedSelected.saved) {
			inflater.inflate(R.menu.feed_description_saved_menu, menu)
		} else {
			inflater.inflate(R.menu.feed_description_menu, menu)
		}
		super.onCreateOptionsMenu(menu, inflater)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.title) {
			getString(R.string.title_saved) -> {
				item.title = getString(R.string.title_saved_fill)
				item.setIcon(R.drawable.bookmark_add_fill)
				item.isChecked = true
				viewModel.insertFeed(viewModel.feedSelected.copy(saved = true))
			}
			getString(R.string.title_saved_fill) -> {
				item.title = getString(R.string.title_saved)
				item.setIcon(R.drawable.bookmark_add)
				item.isChecked = false
				viewModel.insertFeed(viewModel.feedSelected.copy(saved = false))
			}
		}
		return super.onOptionsItemSelected(item)
	}

	private fun displayHtml(html: String) {
		val imageGetter = ImageGetter(resources, binding.htmlViewer, requireContext())

		val styledText = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY, imageGetter, null)

		binding.htmlViewer.movementMethod = LinkMovementMethod.getInstance()

		binding.htmlViewer.text = styledText
	}
}