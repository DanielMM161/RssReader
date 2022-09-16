package com.dmm.rssreader.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dmm.rssreader.model.FeedUI
import com.dmm.rssreader.model.UserProfile
import com.dmm.rssreader.persistence.converters.ConverterList

@Database(entities = [UserProfile::class, FeedUI::class], version = 1, exportSchema = false)
@TypeConverters(value = [ConverterList::class])
abstract class AppDatabase : RoomDatabase() {

  abstract fun userDao(): UserDao
  abstract fun feedsDao(): FeedsDao
}