package com.dmm.rssreader.ui.fragments

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmm.rssreader.databinding.HomeFragmentBinding
import com.dmm.rssreader.ui.adapters.FeedAdapter
import com.dmm.rssreader.utils.Constants.DATE_PATTERN_OUTPUT
import com.dmm.rssreader.utils.Resource
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
					it.data?.let { feeds ->
						binding.totalArticles = feeds.size
						val feedsSorted = feeds.sortedByDescending { it ->
							LocalDate.parse(it.published, DateTimeFormatter.ofPattern(DATE_PATTERN_OUTPUT))
						}
						feedAdapter.differ.submitList(feedsSorted)
					}
				}
				is Resource.Error -> {
					val a = it
				}
			}
		}
	}

}