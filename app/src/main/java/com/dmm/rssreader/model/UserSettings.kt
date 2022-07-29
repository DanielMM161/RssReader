package com.dmm.rssreader.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_settings")
data class UserSettings(
	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "id")
	val id: Int = 1,
	@ColumnInfo(name = "theme")
	var theme: String = "",
	@ColumnInfo(name = "feeds")
	var feeds: MutableList<String> = mutableListOf()
)
