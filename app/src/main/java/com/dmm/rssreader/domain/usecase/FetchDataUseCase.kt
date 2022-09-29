package com.dmm.rssreader.domain.usecase

import com.dmm.rssreader.domain.model.FeedUI
import com.dmm.rssreader.domain.repositories.RepositoryFetchData
import com.dmm.rssreader.utils.Resource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import javax.inject.Inject

class FetchDataUseCase @Inject constructor(
	private val repository: RepositoryFetchData
) {
	suspend fun fetchData(source: String): Resource<List<FeedUI>?> = GlobalScope.async {
		return@async repository.fetchDataNetwork(source)
	}.await()
}