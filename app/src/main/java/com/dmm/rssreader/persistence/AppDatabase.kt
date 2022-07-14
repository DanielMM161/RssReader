package com.dmm.rssreader.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dmm.rssreader.model.UserSettings

@Database(entities = [UserSettings::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
}