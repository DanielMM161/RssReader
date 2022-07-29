package com.dmm.rssreader.binding

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.dmm.rssreader.R
import com.dmm.rssreader.model.FeedUI

object ViewBinding {

	@BindingAdapter("visibility")
	@JvmStatic
	fun totalArticles(view: View, value: Boolean) {
		view.visibility = if(value) View.VISIBLE else View.GONE
	}


	@BindingAdapter("isSelected")
	@JvmStatic
	fun bindIsSelected(imageView: ImageView, value: Boolean) {
		imageView.isSelected = value
		if(value) {
			imageView.setImageResource(R.drawable.readlater_filled)
		} else {
			imageView.setImageResource(R.drawable.readlater)
		}
	}

	@JvmStatic
	@BindingAdapter("loadImage")
	fun bindLoadImage(view: ImageView, feed: FeedUI) {
		val context = view.context
		Glide.with(context)
			.load(feed.image)
			.placeholder(R.drawable.loading_animation)
			.error(R.drawable.ic_baseline_broken_image_24)
			.centerCrop()
			.into(view)
	}
}