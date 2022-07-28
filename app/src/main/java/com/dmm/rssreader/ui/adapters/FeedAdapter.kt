package com.dmm.rssreader.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dmm.rssreader.R
import com.dmm.rssreader.databinding.ItemFeedBinding
import com.dmm.rssreader.model.FeedUI

class FeedAdapter() : RecyclerView.Adapter<FeedAdapter.FeedAdapterViewHolder>() {

	inner class FeedAdapterViewHolder(private val binding: ItemFeedBinding) : RecyclerView.ViewHolder(binding.root) {
		fun bind(feedUI: FeedUI) {
			binding.feed = feedUI
		}
	}

	private val diffCallback = object: DiffUtil.ItemCallback<FeedUI>() {
		override fun areItemsTheSame(oldItem: FeedUI, newItem: FeedUI): Boolean {
			return oldItem.title == newItem.title
		}

		override fun areContentsTheSame(oldItem: FeedUI, newItem: FeedUI): Boolean {
			return oldItem == newItem
		}
	}

	val differ = AsyncListDiffer(this, diffCallback)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedAdapterViewHolder {
		val binding = ItemFeedBinding.inflate(LayoutInflater.from(parent.context))
		return FeedAdapterViewHolder(binding)
	}

	override fun onBindViewHolder(holder: FeedAdapterViewHolder, position: Int) {
		val item = differ.currentList[position]
		holder.itemView.apply {
			setOnClickListener {
				onItemClickListener?.let { it(item) }
			}
		}
		holder.bind(item)
	}

	private var onItemClickListener: ((FeedUI) -> Unit)? = null
	private var readLaterOnItemClickListener: ((FeedUI) -> Unit)? = null

	fun setOnItemClickListener(listener: (FeedUI) -> Unit) {
		onItemClickListener = listener
	}

	fun setReadLaterOnItemClickListener(listener: (FeedUI) -> Unit) {
		readLaterOnItemClickListener = listener
	}

	override fun getItemCount(): Int {
		return differ.currentList.size
	}
}