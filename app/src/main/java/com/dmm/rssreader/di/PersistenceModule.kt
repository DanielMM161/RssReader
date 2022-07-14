package com.dmm.rssreader.di

import android.app.Application
import androidx.room.Room
import com.dmm.rssreader.persistence.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

	@Provides
	@Singleton
	fun provideAppDatabase(application: Application): AppDatabase {
		return Room
			.databaseBuilder(application, AppDatabase::class.java, "rssReader.db")
			.fallbackToDestructiveMigration()
			.build()
	}
}