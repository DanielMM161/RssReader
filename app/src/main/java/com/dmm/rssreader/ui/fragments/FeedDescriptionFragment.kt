package com.dmm.rssreader.ui.fragments

import android.text.method.LinkMovementMethod
import androidx.core.text.HtmlCompat
import com.dmm.rssreader.databinding.FeedDescriptionFragmentBinding
import com.dmm.rssreader.utils.ImageGetter

class FeedDescriptionFragment : BaseFragment<FeedDescriptionFragmentBinding>(
	FeedDescriptionFragmentBinding::inflate
) {

	override fun setupUI() {
		super.setupUI()
		viewModel.feedSelected?.description.let {
			if(it != null) {
				displayHtml(it)
			}
		}
	}

	private fun displayHtml(html: String) {
		// Creating object of ImageGetter class you just created
		val imageGetter = ImageGetter(resources, binding.htmlViewer, requireContext())

		// Using Html framework to parse html
		val styledText =
			HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY, imageGetter,null)

		// to enable image/link clicking
		binding.htmlViewer.movementMethod = LinkMovementMethod.getInstance()

		// setting the text after formatting html and downloading and setting images
		binding.htmlViewer.text = styledText
	}
}