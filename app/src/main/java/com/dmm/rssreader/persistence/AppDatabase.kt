package com.dmm.rssreader.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dmm.rssreader.model.FeedUI
import com.dmm.rssreader.model.UserSettings
import com.dmm.rssreader.persistence.converters.ConverterList

@Database(entities = [UserSettings::class, FeedUI::class], version = 1, exportSchema = false)
@TypeConverters(value = [ConverterList::class])
abstract class AppDatabase : RoomDatabase() {

  abstract fun userSettingsDao(): UserSettingsDao
  abstract fun feedsDao(): FeedsDao
}