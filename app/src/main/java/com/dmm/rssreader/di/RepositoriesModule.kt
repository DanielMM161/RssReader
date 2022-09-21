package com.dmm.rssreader.di

import com.dmm.rssreader.data.repositories.RepositoryAuthImpl
import com.dmm.rssreader.data.repositories.RepositoryFeedAndroidBlogsImpl
import com.dmm.rssreader.data.repositories.RepositoryFeedAppleImpl
import com.dmm.rssreader.domain.repositories.RepositoryAuth
import com.dmm.rssreader.domain.repositories.RepositoryFeedAndroidBlogs
import com.dmm.rssreader.domain.repositories.RepositoryFeedApple
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoriesModule {

	@Binds
	abstract fun bindRepositoryFeedAndroidBlogs(
		repoFeedAndroidBlogs: RepositoryFeedAndroidBlogsImpl
	): RepositoryFeedAndroidBlogs

	@Binds
	abstract fun bindRepositoryFeedApple(
		repoFeedAndroid: RepositoryFeedAppleImpl
	) : RepositoryFeedApple

	@Binds
	abstract fun bindRepositoryAuth(
		repoAuth: RepositoryAuthImpl
	) : RepositoryAuth


}