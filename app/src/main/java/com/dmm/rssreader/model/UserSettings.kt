package com.dmm.rssreader.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_settings")
data class UserSettings(
	@PrimaryKey(autoGenerate = true)
	val id: Int,
	@ColumnInfo(name = "theme")
	val theme: String,
	@ColumnInfo(name = "feeds")
	val feeds: String
)
