package com.dmm.rssreader.presentation.fragments

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.dmm.rssreader.R
import com.dmm.rssreader.databinding.FeedDescriptionDialogBinding
import com.dmm.rssreader.domain.model.FeedUI
import com.dmm.rssreader.presentation.viewModel.MainViewModel
import com.dmm.rssreader.utils.ImageGetter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FeedDescriptionDialog(private val feedSelected: FeedUI) : BottomSheetDialogFragment() {

	private lateinit var binding: FeedDescriptionDialogBinding
	private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
	private lateinit var dialog: BottomSheetDialog
	private lateinit var viewModel: MainViewModel

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
		//Disabled dragging
		dialog.setOnShowListener {
			val bottomSheet = dialog
				.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)

			if (bottomSheet != null) {
				val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
				behavior.isDraggable = false
			}
		}
		return dialog
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.feed_description_dialog, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding = FeedDescriptionDialogBinding.bind(view)
		viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
		bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
		bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
		val layout = binding.bottomSheetLayout
		if(layout != null) {
			layout?.minimumHeight = Resources.getSystem().displayMetrics.heightPixels
		}
		setUpUI()
		saveFeed()
	}

	private fun setUpUI(){
		setImageResourceImageButton(feedSelected.favourite)
		binding.title.text = feedSelected.title
		feedSelected.description.let {
			if(it != null) {
				displayHtml(it)
			}
		}
	}

	private fun displayHtml(html: String) {
		val imageGetter = ImageGetter(resources, binding.htmlViewer, requireContext())

		val styledText = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY, imageGetter, null)

		binding.htmlViewer.movementMethod = LinkMovementMethod.getInstance()

		binding.htmlViewer.text = styledText
	}

	private fun saveFeed() {
		binding.save.setOnClickListener {
			viewModel.saveFavouriteFeed(feedSelected)
			setImageResourceImageButton(feedSelected.favourite)
		}
	}

	private fun setImageResourceImageButton(favourite: Boolean) {
		if(favourite) {
			binding.save.setImageResource(R.drawable.bookmark_add_fill)
		} else {
			binding.save.setImageResource(R.drawable.bookmark_add)
		}
	}
}