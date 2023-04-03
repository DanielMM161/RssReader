package com.dmm.beerss.data.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dmm.beerss.domain.model.FeedUI
import com.dmm.beerss.data.persistence.converters.ConverterList

@Database(entities = [FeedUI::class], version = 1, exportSchema = false)
@TypeConverters(value = [ConverterList::class])
abstract class AppDatabase : RoomDatabase() {

  abstract fun feedsDao(): FeedsDao
}