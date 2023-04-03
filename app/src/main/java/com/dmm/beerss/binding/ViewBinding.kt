package com.dmm.beerss.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.dmm.beerss.R
import com.dmm.beerss.data.local.ContentResources.contentResources
import com.dmm.beerss.domain.model.FeedUI

object ViewBinding {

	@JvmStatic
	@BindingAdapter("loadImage")
	fun bindLoadImage(view: ImageView, feed: FeedUI) {
		var errorImage: Int = R.drawable.ic_baseline_broken_image_24
		contentResources.forEach {
			if(it.title.equals(feed.feedSource) ) {
				errorImage = it.imageRes
			}
		}
		val context = view.context
		Glide.with(context)
			.load(feed.image)
			.placeholder(R.drawable.loading_animation)
			.error(errorImage)
			.centerCrop()
			.into(view)
	}
}