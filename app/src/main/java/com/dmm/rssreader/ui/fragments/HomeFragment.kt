package com.dmm.rssreader.ui.fragments

import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.dmm.rssreader.databinding.HomeFragmentBinding
import com.dmm.rssreader.utils.Resource
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<HomeFragmentBinding>(
	HomeFragmentBinding::inflate
) {

	override fun setupUI() {
		super.setupUI()

		viewLifecycleOwner.lifecycleScope.launch {
			//viewModel.getAppelDeveloper()
			getMetalInjection()
		}

	}

	private suspend fun getMetalInjection() {
		viewModel.metalInjectionFeed.collect {
			when(it) {
				is Resource.Loading -> {
					val a = it
				}
				is Resource.Success -> {
					val a = it
					Log.e("getMetalInjection ---> ", "${it.data?.title}")
				}
				is Resource.Error -> {
					val a = it
				}
			}
		}
	}

}