package com.dmm.beerss.domain.usecase

import com.dmm.beerss.data.repositories.RepositorySourceImpl
import com.dmm.beerss.domain.model.Source
import com.dmm.beerss.domain.repositories.RepositorySource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SourceUseCase @Inject constructor(
	private val repo: RepositorySource
) {

	suspend fun fetchSources(): Flow<List<Source>> {
		return repo.fetchSources()
	}
}