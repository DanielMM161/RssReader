package com.dmm.rssreader.ui.fragments

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmm.rssreader.R
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
		setUpRecyclerView()
		onRefreshListener()
	}


	private fun setUpRecyclerView() = binding.rvFeeds.apply {
		feedAdapter = FeedAdapter()
		adapter = feedAdapter
		layoutManager = LinearLayoutManager(requireContext())
		itemClickListener()
		readLaterItemClickListener()
	}

	private fun onRefreshListener() {
		binding.swipeRefresh.setOnRefreshListener {
			viewModel.resetResponse()
			viewModel.fetchFeedsDeveloper()
		}
	}

	private suspend fun subscribeObservableDeveloperFeeds() {
		viewModel.developerFeeds.collect {
			when (it) {
				is Resource.Loading -> {
					binding.swipeRefresh.isRefreshing = true
				}
				is Resource.Success -> {
					binding.swipeRefresh.isRefreshing = false
					it.data?.let { feeds ->
						binding.totalArticles = feeds.size
						val feedsSorted = feeds.sortedByDescending { it ->
							LocalDate.parse(it.published, DateTimeFormatter.ofPattern(DATE_PATTERN_OUTPUT))
						}
						feedAdapter.differ.submitList(feedsSorted)
					}
				}
				is Resource.Error -> {

				}
			}
		}
	}

	private fun itemClickListener() = feedAdapter.setOnItemClickListener {
		viewModel.feedSelected = it
		findNavController().navigate(R.id.action_homeFragment_to_feedDescriptionFragment)
	}

	private fun readLaterItemClickListener() = feedAdapter.setReadLaterOnItemClickListener { feedUI ->
		viewModel.insertFeed(feedUI)
	}

}