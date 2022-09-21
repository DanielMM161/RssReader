package com.dmm.rssreader.domain.usecase

import com.dmm.rssreader.data.repositories.RepositoryFireBaseImpl
import javax.inject.Inject

class FireBaseUseCase @Inject constructor(
	private val repository: RepositoryFireBaseImpl
) {
}