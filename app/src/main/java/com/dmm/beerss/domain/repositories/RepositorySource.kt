package com.dmm.beerss.domain.repositories

import com.dmm.beerss.domain.model.Source
import kotlinx.coroutines.flow.Flow

interface RepositorySource {
	suspend fun fetchSources(): Flow<List<Source>>
}