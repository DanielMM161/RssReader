package com.dmm.rssreader.ui.fragments

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmm.rssreader.R
import com.dmm.rssreader.databinding.HomeFragmentBinding
import com.dmm.rssreader.ui.adapters.FeedAdapter
import com.dmm.rssreader.utils.Resource
import com.dmm.rssreader.utils.Utils
import com.dmm.rssreader.utils.Utils.Companion.isNightMode
import com.dmm.rssreader.utils.Utils.Companion.showToast
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<HomeFragmentBinding>(
	HomeFragmentBinding::inflate
) {

	private lateinit var feedAdapter: FeedAdapter
	var firstTime = true

	override fun setupUI() {
		super.setupUI()

		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				launch {
					subscribeObservableDeveloperFeeds()
				}
				launch {
					viewModel.fetchFeedsDeveloper()
				}
			}
		}
		setUpRecyclerView()
		onRefreshListener()
		setColorSwipeRefresh()
	}

	private fun setColorSwipeRefresh() {
		when(isNightMode(resources)) {
			true -> {
				binding.swipeRefresh.setProgressBackgroundColorSchemeResource(R.color.primary)
				binding.swipeRefresh.setColorSchemeResources(R.color.black)
			}
			false -> {
				binding.swipeRefresh.setColorSchemeResources(R.color.primary)
			}
		}
	}


	private fun setUpRecyclerView() = binding.rvFeeds.apply {
		feedAdapter = FeedAdapter()
		adapter = feedAdapter
		layoutManager = LinearLayoutManager(requireContext())
		itemClickListener()
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
					it.data?.let { feeds ->
						binding.totalArticles = feeds.size
						binding.swipeRefresh.isRefreshing = false
						feedAdapter.differ.submitList(feeds)
					}
				}
				is Resource.Error -> {
					binding.swipeRefresh.isRefreshing = false
					it.message.let { message ->
						showToast(requireContext(), message)
					}
				}
				is Resource.ErrorCaught -> {
					binding.swipeRefresh.isRefreshing = false
					val message = it.asString(requireContext())
					showToast(requireContext(), message)
				}
			}
		}
	}

	private fun itemClickListener() = feedAdapter.setOnItemClickListener {
		viewModel.feedSelected = it
		findNavController().navigate(R.id.action_homeFragment_to_feedDescriptionFragment)
	}
}