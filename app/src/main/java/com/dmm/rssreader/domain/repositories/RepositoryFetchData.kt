package com.dmm.rssreader.domain.repositories

import com.dmm.rssreader.domain.model.FeedUI
import com.dmm.rssreader.utils.Resource

interface RepositoryFetchData {
	suspend fun fetchDataNetwork(source: String): Resource<List<FeedUI>?>
	suspend fun saveDataLocal(feedUI: List<FeedUI>)
}