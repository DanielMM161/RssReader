package com.dmm.rssreader.presentation.fragments

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dmm.rssreader.R
import com.dmm.rssreader.databinding.ReadLaterFragmentBinding
import com.dmm.rssreader.presentation.adapters.FeedAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ReadLaterFragment : BaseFragment<ReadLaterFragmentBinding>(
	ReadLaterFragmentBinding::inflate
) {

	private lateinit var feedAdapter: FeedAdapter

	override fun setupUI() {
		super.setupUI()
		setUpRecyclerView()
		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.getFavouriteFeeds().collect {
					binding.noReadLater.visibility = if(it.isEmpty()) View.VISIBLE else View.GONE
					binding.willBeHere.visibility = if(it.isEmpty()) View.VISIBLE else View.GONE
					feedAdapter.differ.submitList(it)
				}
			}
		}
	}

	private fun deleteItemSwipe() {
		val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
			ItemTouchHelper.UP or ItemTouchHelper.DOWN,
			ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
		) {
			override fun onMove(
				recyclerView: RecyclerView,
				viewHolder: RecyclerView.ViewHolder,
				target: RecyclerView.ViewHolder
			): Boolean {
				return true
			}

			override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
				val position = viewHolder.adapterPosition
				val feed = feedAdapter.differ.currentList[position]
				viewModel.saveFavouriteFeed(feed)
				Snackbar.make(binding.root, getString(R.string.delete_feed, feed.title), Snackbar.LENGTH_LONG).apply {
					setAction(getString(R.string.undo)) {
						viewModel.saveFavouriteFeed(feed)
					}
					setTextColor(resources.getColor(R.color.primary))
					show()
				}
			}
		}

		ItemTouchHelper(itemTouchHelperCallback).apply {
			attachToRecyclerView(binding.rvReadLater)
		}
	}

	private fun setUpRecyclerView() = binding.rvReadLater.apply {
		feedAdapter = FeedAdapter()
		adapter = feedAdapter
		layoutManager = LinearLayoutManager(requireContext())
		deleteItemSwipe()
		itemClickListener()
		readLaterItemClickListener()
	}

	private fun itemClickListener() = feedAdapter.setOnItemClickListener {
		val feedDescriptionDialog = FeedDescriptionDialog(it.copy())
		feedDescriptionDialog.show(parentFragmentManager, feedDescriptionDialog.tag)
	}

	private fun readLaterItemClickListener() = feedAdapter.setReadLaterOnItemClickListener {
		viewModel.saveFavouriteFeed(it)
	}
}