package com.dmm.rssreader.ui.fragments

import android.content.Context
import android.content.Intent
import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dmm.rssreader.R
import com.dmm.rssreader.databinding.FeedDescriptionFragmentBinding
import com.dmm.rssreader.model.FeedUI
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
					if(feed.title == viewModel.feedSelected.title) {
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
		when(item.itemId) {
			R.id.saved -> {
				item.setIcon( R.drawable.bookmark_add_fill)
				viewModel.insertFeed(viewModel.feedSelected.copy(saved = true))
			}
			R.id.saved_fill -> {
				item.setIcon( R.drawable.bookmark_add)
				viewModel.insertFeed(viewModel.feedSelected.copy(saved = false))
			}
			R.id.share -> {

			}
			android.R.id.home -> {
				findNavController().navigate(R.id.action_feedDescriptionFragment_to_homeFragment)
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