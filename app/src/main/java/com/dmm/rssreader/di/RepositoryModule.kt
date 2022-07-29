package com.dmm.rssreader.di

import com.dmm.rssreader.network.RssClient
import com.dmm.rssreader.persistence.FeedsDao
import com.dmm.rssreader.persistence.UserSettingsDao
import com.dmm.rssreader.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

	@Provides
	@ViewModelScoped
	fun provideMainRepository(rssClient: RssClient, userSettingsDao: UserSettingsDao, feedsDao: FeedsDao) : MainRepository {
		return MainRepository(rssClient, userSettingsDao, feedsDao)
	}

}
