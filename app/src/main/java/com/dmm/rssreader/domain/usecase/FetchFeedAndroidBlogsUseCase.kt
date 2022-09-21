package com.dmm.rssreader.domain.usecase

import com.dmm.rssreader.domain.model.FeedUI
import com.dmm.rssreader.domain.repositories.RepositoryFeedAndroidBlogs
import com.dmm.rssreader.utils.Resource
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import javax.inject.Inject

class FetchFeedAndroidBlogsUseCase @Inject constructor(
	private val repository: RepositoryFeedAndroidBlogs
) {
	suspend operator fun invoke(): Deferred<Resource<List<FeedUI>?>> = GlobalScope.async {
		return@async repository.fetchFeedAndroidBlogs()
	}
}