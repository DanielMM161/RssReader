package com.dmm.rssreader.ui.fragments

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dmm.rssreader.R
import com.dmm.rssreader.databinding.ReadLaterFragmentBinding
import com.dmm.rssreader.ui.adapters.FeedAdapter
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
				viewModel.getFeedList().collect {
					feedAdapter.differ.submitList(it.filter { it -> it.saved })
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
				viewModel.insertFeed(feed.copy(saved = false))
				Snackbar.make(binding.root, getString(R.string.delete_feed, feed.title), Snackbar.LENGTH_LONG).apply {
					setAction(getString(R.string.undo)) {
						viewModel.insertFeed(feed.copy(saved = true))
					}
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
	}
}