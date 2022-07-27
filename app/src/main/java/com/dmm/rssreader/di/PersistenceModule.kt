package com.dmm.rssreader.di

import android.app.Application
import androidx.room.Room
import com.dmm.rssreader.persistence.AppDatabase
import com.dmm.rssreader.persistence.FeedsDao
import com.dmm.rssreader.persistence.UserSettingsDao
import com.dmm.rssreader.persistence.converters.ConverterList
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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
	fun provideMoshi(): Moshi {
		return Moshi.Builder()
			.addLast(KotlinJsonAdapterFactory())
			.build()
	}

	@Provides
	@Singleton
	fun provideAppDatabase(application: Application, converterList: ConverterList): AppDatabase {
		return Room
			.databaseBuilder(application, AppDatabase::class.java, "rssReader.db")
			.fallbackToDestructiveMigration()
			.addTypeConverter(converterList)
			.build()
	}

	@Provides
	@Singleton
	fun provideUserSettingsDao(appDatabase: AppDatabase): UserSettingsDao {
		return appDatabase.userSettingsDao()
	}

	@Provides
	@Singleton
	fun provideFeedsDao(appDatabase: AppDatabase): FeedsDao {
		return appDatabase.feedsDao()
	}

	@Provides
	@Singleton
	fun provideConverterList(moshi: Moshi): ConverterList {
		return ConverterList(moshi)
	}
}