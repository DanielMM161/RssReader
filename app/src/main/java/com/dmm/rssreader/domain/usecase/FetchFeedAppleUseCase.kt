package com.dmm.rssreader.domain.usecase

import com.dmm.rssreader.domain.model.FeedUI
import com.dmm.rssreader.domain.repositories.RepositoryFeedApple
import com.dmm.rssreader.utils.Resource
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import javax.inject.Inject

class FetchFeedAppleUseCase @Inject constructor(
	private val repository: RepositoryFeedApple
) {

	suspend operator fun invoke(): Resource<List<FeedUI>?> = GlobalScope.async {
		return@async repository.fetchFeedApple()
	}.await()
}