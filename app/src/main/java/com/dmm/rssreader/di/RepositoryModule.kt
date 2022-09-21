package com.dmm.rssreader.di

import com.dmm.rssreader.data.network.RssClient
import com.dmm.rssreader.data.persistence.FeedsDao
import com.dmm.rssreader.data.persistence.UserDao
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
	fun provideMainRepository(rssClient: RssClient, userSettingsDao: UserDao, feedsDao: FeedsDao) : MainRepository {
		return MainRepository(rssClient, userSettingsDao, feedsDao)
	}

}
