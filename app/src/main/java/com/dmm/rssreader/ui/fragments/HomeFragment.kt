package com.dmm.rssreader.ui.fragments

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmm.rssreader.databinding.HomeFragmentBinding
import com.dmm.rssreader.ui.adapters.FeedAdapter
import com.dmm.rssreader.utils.Resource
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<HomeFragmentBinding>(
	HomeFragmentBinding::inflate
) {

	private lateinit var feedAdapter: FeedAdapter

	override fun setupUI() {
		super.setupUI()

		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				launch {
					viewModel.fetchFeedsDeveloper()
				}
				launch {
					subscribeObservableDeveloperFeeds()
				}
			}
		}

		binding.rvFeeds.apply {
			feedAdapter = FeedAdapter()
			adapter = feedAdapter
			layoutManager = LinearLayoutManager(requireContext())
		}
	}

	private suspend fun subscribeObservableDeveloperFeeds() {
		viewModel.developerFeeds.collect {
			when(it) {
				is Resource.Loading -> {
					val a = it
				}
				is Resource.Success -> {
					val a = it
					it.data?.let { feeds ->
						binding.totalArticles = feeds.size
						feedAdapter.differ.submitList(feeds)
					}
				}
				is Resource.Error -> {
					val a = it
				}
			}
		}
	}

}