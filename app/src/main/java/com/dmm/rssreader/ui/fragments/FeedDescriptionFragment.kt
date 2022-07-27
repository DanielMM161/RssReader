package com.dmm.rssreader.ui.fragments

import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.text.HtmlCompat
import com.dmm.rssreader.R
import com.dmm.rssreader.databinding.FeedDescriptionFragmentBinding
import com.dmm.rssreader.model.FeedUI
import com.dmm.rssreader.utils.ImageGetter

class FeedDescriptionFragment : BaseFragment<FeedDescriptionFragmentBinding>(
	FeedDescriptionFragmentBinding::inflate
) {

	override fun setupUI() {
		super.setupUI()
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
		when(item.itemId) {
			R.id.saved -> {
				item.setIcon( R.drawable.bookmark_add_fill)
				viewModel.insertFeed(viewModel.feedSelected.copy(saved = true))
			}
			R.id.saved_fill -> {
				item.setIcon( R.drawable.bookmark_add)
				viewModel.insertFeed(viewModel.feedSelected.copy(saved = false))
			}
		}
		return super.onOptionsItemSelected(item)
	}

	private fun displayHtml(html: String) {
		// Creating object of ImageGetter class you just created
		val imageGetter = ImageGetter(resources, binding.htmlViewer, requireContext())

		// Using Html framework to parse html
		val styledText =
			HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY, imageGetter, null)

		// to enable image/link clicking
		binding.htmlViewer.movementMethod = LinkMovementMethod.getInstance()

		// setting the text after formatting html and downloading and setting images
		binding.htmlViewer.text = styledText
	}
}